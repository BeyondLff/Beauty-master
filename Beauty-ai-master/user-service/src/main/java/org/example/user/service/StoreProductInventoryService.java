package org.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.user.entity.Category;
import org.example.user.entity.PageBean;
import org.example.user.entity.Product;
import org.example.user.entity.StoreProductInventory;
import org.example.user.request.PageArgs;
import org.example.user.response.ListResponse;

import java.util.List;

public interface StoreProductInventoryService extends IService<StoreProductInventory> {
    PageBean listProducts(PageArgs pageargs);

    List<Product> getProductsWithNoPage();

}
