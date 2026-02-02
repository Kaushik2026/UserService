package com.backendlld.userservice.security.customfilter;

import com.backendlld.userservice.models.User;
import com.backendlld.userservice.repositories.UserRepository;
import com.backendlld.userservice.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            log.info("incomming request: {}", request.getRequestURI());
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = header.substring(7).trim();

//            isTokenValid checks signature and expiration,for case like if token expired  we cant able to set isLoggedin
//            false and for the second time when we try to login it will say already logged in and won't allow us.
            if (!jwtUtil.isTokenValid(token)) {
                log.warn("Invalid/expired token");
                String email = jwtUtil.getEmailFromToken(token); // Safe even for expired
                if (email != null) {
                    User user = userRepository.findByEmail(email).orElse(null);
                    if (user != null && user.isLoggedIn()) {
                        user.setLoggedIn(false);  // Cleanup expired session
                        userRepository.save(user);
                        log.info("Auto-logged out expired session for: {}", email);
                    }
                }
                response.setStatus(401);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid or expired token\"}");
                return;
            }

            String email = jwtUtil.getEmailFromToken(token);

            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Here you would typically load the user details from your user service or repository
                // and create an Authentication object to set in the SecurityContext.
                User user = userRepository.findByEmail(email).orElseThrow();
                // CHECK BUSINESS LOGIN STATUS
                if (!user.isLoggedIn()) {
                    log.warn("User {} session expired", email);
                    response.setStatus(401);
                    response.getWriter().write("{\"error\": \"Session expired. Please login again\"}");
                    return;
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        }catch(Exception ex){
            throw new ServletException(ex);
        }
    }
}
