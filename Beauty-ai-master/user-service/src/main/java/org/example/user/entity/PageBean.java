package org.example.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class PageBean {
    private Long total;  //返回总条数
    private List rows;  //存放结果数组
}
