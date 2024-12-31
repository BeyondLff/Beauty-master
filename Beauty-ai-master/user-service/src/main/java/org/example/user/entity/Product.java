package org.example.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("products")
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer product_id;
    private String account;
    private String name;
    private String brandName;

    private String stock;
    private Integer status;
    @TableField(exist = false)
    private String tag;
    private String image;
    private String price;
    @TableLogic
    private Integer deleted;
}
