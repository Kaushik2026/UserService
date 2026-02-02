package com.backendlld.userservice.dtos;

import com.nimbusds.jwt.JWT;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String tokenValue;
    private Long userId;

}
