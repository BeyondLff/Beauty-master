package org.example.user.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String password;
    private String account;
    /*
    * 登录的参数需求：
    * password:登录密码
    * account:登录账号
    * */
}
