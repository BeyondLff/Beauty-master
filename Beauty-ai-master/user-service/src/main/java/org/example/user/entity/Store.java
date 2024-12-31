package org.example.user.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;

import java.time.LocalDateTime;

@TableName("stores")
@Data
public class Store {
    private Integer id;
    private String storeName;
    private String addressProvince;
    private String addressCity;
    private String addressDetail;
    private Integer status;
    private String contact;
    private Integer ownerId;
    @TableField(exist = false)
    private String ownerName;
    @TableLogic
    private Integer deleted;
}
