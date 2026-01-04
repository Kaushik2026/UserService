package com.backendlld.userservice.security.config;

import org.springframework.security.oauth2.server.authorization.jackson.OAuth2AuthorizationServerJacksonModule;
import tools.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.jackson.SecurityJacksonModules;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class JacksonConfig {

    @Bean
//    @Primary
    public ObjectMapper objectMapper() {
        ClassLoader classLoader = getClass().getClassLoader();
        return JsonMapper.builder()
                .addModules(SecurityJacksonModules.getModules(classLoader))
                .addModule(new OAuth2AuthorizationServerJacksonModule())
                .build();

    }
}
