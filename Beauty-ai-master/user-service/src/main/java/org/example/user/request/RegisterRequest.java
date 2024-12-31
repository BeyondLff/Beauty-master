package org.example.user.request;

import lombok.Data;

import java.math.BigInteger;

@Data
public class RegisterRequest {
    private String username;  //姓名
    private String phoneNumber;  //电话号
    private String gender;  //性别
    private String idcard;  //身份证
    private String homeAddress;  //地址
    private String storesName; //店名
}

/*
* 注册请求参数*/
