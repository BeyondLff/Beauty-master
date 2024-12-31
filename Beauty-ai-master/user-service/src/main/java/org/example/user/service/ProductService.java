package org.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.user.entity.Product;
import org.example.user.response.ListResponse;

import java.util.List;

public interface ProductService extends IService<Product> {

    List<Product> listProducts();
    Product selectById(Integer id);
}
