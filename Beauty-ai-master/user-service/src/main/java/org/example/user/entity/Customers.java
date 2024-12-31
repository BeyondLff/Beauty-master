package org.example.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.lang.reflect.Type;

@Data
@TableName("customers")
public class Customers {
    @TableId(type = IdType.AUTO)
    private int id;
    private String imgUrl;
    private String phoneNumber;
    private String name;
    private int sex;
    private int age;
    private String status;
    private char letter;
    private int userId;
    private String address;
    private String email;
    @TableLogic
    private int deleted;

}
