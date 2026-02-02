package com.backendlld.userservice.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogOutRequestDto {
    @NotBlank(message = "Token value is required")
    private String tokenValue;
}
