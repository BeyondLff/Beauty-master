package org.example.user.request;

import lombok.Data;

import java.math.BigInteger;

@Data
public class QueryUserRequest extends PageArgs {
    private BigInteger account;
    private String username;
    private String phoneNumber;
    private Integer roleId;
    private String storeName;
    private Integer status;
}
