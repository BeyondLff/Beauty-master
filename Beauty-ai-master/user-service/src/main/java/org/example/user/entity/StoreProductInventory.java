package org.example.user.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("store_product_inventory")
public class StoreProductInventory {
    private Integer id;
    private Integer product_id;
    private Integer store_id;
    private BigDecimal discountPrice;
    @TableLogic
    private Integer deleted;
    private Integer status;
    private String spec;
}
