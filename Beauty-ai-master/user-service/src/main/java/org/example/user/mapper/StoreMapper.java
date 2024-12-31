package org.example.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.user.entity.Store;
import org.example.user.request.SearchStoreRequest;

import java.util.List;

@Mapper
public interface StoreMapper extends BaseMapper<Store> {


    @Select("select * from stores")
    List<Store> selectAllStores();

    //自定义搜索商店信息
    List<Store> searchStores(SearchStoreRequest searchStoreRequest);

}
