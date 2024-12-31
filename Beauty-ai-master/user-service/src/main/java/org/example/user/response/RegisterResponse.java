package org.example.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
@AllArgsConstructor
@Data
public class RegisterResponse {
    private String img;
    private BigInteger account;
    private String password;
}
