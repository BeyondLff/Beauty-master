package org.example.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import org.example.user.entity.PageBean;
import org.example.user.entity.Result;
import org.example.user.request.PageArgs;
import org.example.user.service.Impl.CategoryServiceImpl;
import org.example.user.service.ProductService;
import org.example.user.service.StoreProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
public class GoodController {
    @Autowired
    CategoryServiceImpl categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    StoreProductInventoryService storeProductInventoryService;

    @GetMapping("/categories")
    public Result getCategory() {
        return Result.success(200, "success", categoryService.getCategory());
    }

    @GetMapping("/getGoodsList")
    public Result getAllGoods(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        PageArgs pageArgs = new PageArgs();
        pageArgs.setPageNum(pageNum);
        pageArgs.setPageSize(pageSize);
        return Result.success(200, "success", storeProductInventoryService.listProducts(pageArgs));
    }

    @GetMapping("/detail")
    public Result getGoodDetail(Integer goodsId) {
        return Result.success(200, "success", productService.selectById(goodsId));
    }

    @GetMapping("/list")
    public Result getGoodWithNoPage() {
        return Result.success(200, "success",  storeProductInventoryService.getProductsWithNoPage());
    }
    @GetMapping("/getAllList")
    public Result getAllList() {
        return Result.success(200, "success", productService.listProducts());
    }
}
