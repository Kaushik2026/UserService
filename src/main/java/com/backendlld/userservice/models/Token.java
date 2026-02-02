package com.backendlld.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tokens")
public class Token extends BaseModel {
    private String tokenValue;
    private Date expiryDate;
    private String userEmail;
    private boolean isValid = true;
    private boolean isRevoked = false;
    private LocalDateTime expiresAt;

    @ManyToOne
    private User user;
}
