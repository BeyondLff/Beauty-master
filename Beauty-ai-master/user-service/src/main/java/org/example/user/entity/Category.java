package org.example.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("category")
@Data
public class Category {
    private int id;
    private String categoryName;
}
