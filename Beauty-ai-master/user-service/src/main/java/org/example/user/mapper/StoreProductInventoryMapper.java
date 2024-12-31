package org.example.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.user.entity.Category;
import org.example.user.entity.Product;
import org.example.user.entity.StoreProductInventory;
import org.example.user.response.ListResponse;

import java.util.List;

@Mapper
public interface StoreProductInventoryMapper extends BaseMapper<StoreProductInventory> {

    List<ListResponse> listAllByStoreId(Integer storeId);

    List<Product> listAllByStoreIdWithNoPage(Integer storeIdByUserId);
}

