package org.example.user.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.user.entity.Category;
import org.example.user.entity.PageBean;
import org.example.user.entity.Product;
import org.example.user.entity.StoreProductInventory;
import org.example.user.mapper.StoreProductInventoryMapper;
import org.example.user.mapper.UserMapper;
import org.example.user.request.PageArgs;
import org.example.user.response.ListResponse;
import org.example.user.service.StoreProductInventoryService;
import org.example.user.utils.myThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class StoreProductInventoryServiceImpl extends ServiceImpl<StoreProductInventoryMapper, StoreProductInventory>
        implements StoreProductInventoryService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StoreProductInventoryMapper storeProductInventoryMapper;

    @Override
    public PageBean listProducts(PageArgs pageArgs) {
        // 获取当前用户的store_id
        Integer userId = myThreadLocal.get(); // 假设你有一个工具类获取当前用户ID
        Integer storeId = userMapper.getStoreIdByUserId(userId);
        PageHelper.startPage(pageArgs.pageNum, pageArgs.pageSize);
        Page<ListResponse> page = (Page<ListResponse>) storeProductInventoryMapper.listAllByStoreId(storeId);
        return new PageBean(page.getTotal(),page.getResult());
    }
    public List<Product> getProductsWithNoPage() {
        Integer storeIdByUserId = userMapper.getStoreIdByUserId(myThreadLocal.get());
        return storeProductInventoryMapper.listAllByStoreIdWithNoPage(storeIdByUserId);
    }
}

