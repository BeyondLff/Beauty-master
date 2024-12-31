package org.example.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListResponse {
    private Integer id;
    private String account;
    private String name;
    private String brandname;
    private Double price;
    private Integer stock;  //库存
    private Integer status; //状态
    private Double discountPrice;
    private String image;
}
