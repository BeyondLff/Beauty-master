package org.example.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.user.entity.Product;
import org.example.user.mapper.ProductMapper;
import org.example.user.response.ListResponse;
import org.example.user.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    ProductMapper productMapper;

    public List<Product> listProducts() {
//        List<ListResponse> products = new ArrayList<>();
        return productMapper.selectList(null);
//        products.add(new ListResponse(681, "4155193", "宝可梦训练大师", "David", 114.0, 514, 0, 0.0,"https://img1.baidu.com/it/u=1251116221,1871555249&fm=253&fmt=auto&app=138&f=JPEG?w=550&h=308"));
//        products.add(new ListResponse(682, "9263844", "皮卡丘", "Karen", 1919.0, 810, 0, 0.0,"https://pica.zhimg.com/v2-a12520d9074e59078d07c3183d1f9286_b.jpg"));
//        products.add(new ListResponse(683, "5458444", "杰尼龟", "Maria", 685.2, 429, 0, 0.0,"https://img.3dmgame.com/uploads/images/news/20200721/1595316080_479085.jpg"));
//        products.add(new ListResponse(684, "5830461", "可达鸭", "Charles", 236.5, 606, 1, 0.0,"https://s3.ifanr.com/wp-content/uploads/2022/05/1593518601_373107.jpeg!720"));
//        products.add(new ListResponse(685, "4072038", "妙蛙种子", "Elizabeth", 732.0, 481, 1, 0.0,"https://prodigits.co.uk/thumbs/wallpapers/p2ls/anime/45/20941b7312535404.jpg"));
//        products.add(new ListResponse(686, "8207605", "小火龙", "Deborah", 257.1, 490, 1, 0.0,"https://img.3dmgame.com/uploads/images/news/20191119/1574149623_755993.jpg"));
//        products.add(new ListResponse(687, "7911540", "翼神龙", "Angela", 719.9, 165, 1, 0.0,"https://i02piccdn.sogoucdn.com/640fd5dbc2a74f87"));
//        products.add(new ListResponse(688, "7903810", "别群却对", "Brenda", 552.0, 38, 1, 0.0,"http://dummyimage.com/120x240"));
//        products.add(new ListResponse(689, "9526261", "层决一小", "Mark", 243.1, 997, 0, 0.0,"http://dummyimage.com/160x600"));
//        products.add(new ListResponse(690, "4494651", "规治道", "Linda", 743.4, 831, 0, 0.0,"http://dummyimage.com/180x150"));

    }
    public Product selectById(Integer id){
        QueryWrapper<Product>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("product_id",id);
        return productMapper.selectOne(queryWrapper);
    }
}
