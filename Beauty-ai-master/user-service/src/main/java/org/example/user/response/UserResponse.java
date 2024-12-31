package org.example.user.response;

import lombok.Data;

@Data
public class UserResponse {
    private Integer id;
    private String account;
    private String username;
    private String phoneNumber;
    private String role;
    private Integer status;
    private String storeName;
}
