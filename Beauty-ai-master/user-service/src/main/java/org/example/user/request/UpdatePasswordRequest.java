package org.example.user.request;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private Integer id;
    private String oldPassword;
    private String NewPassword;
    private String confirmNewPassword;
    /*
     * 修改账号密码的参数需求：
     * password:登录密码
     * username:用户名
     * */
}
