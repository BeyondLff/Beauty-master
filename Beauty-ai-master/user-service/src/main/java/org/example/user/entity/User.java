package org.example.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("users")
@Data
public class User {
    private Integer id;
    private String account;
    private String username;
    private String password;
    private String phoneNumber;
    private Integer storesId;
    private String image;
    private Integer status;
    @TableField(exist = false)
    private String role;
    private String idcard;
    private String homeAddress;
    private String gender;
}
