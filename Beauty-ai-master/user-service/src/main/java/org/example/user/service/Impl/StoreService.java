package org.example.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.user.constant.CodeConstant;
import org.example.user.entity.PageBean;
import org.example.user.entity.Store;
import org.example.user.entity.User;
import org.example.user.exception.AddException;
import org.example.user.exception.ArgException;
import org.example.user.exception.UpdateException;
import org.example.user.mapper.StoreMapper;
import org.example.user.mapper.UserMapper;
import org.example.user.request.PageArgs;
import org.example.user.request.SearchStoreRequest;
import org.example.user.service.StoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService extends ServiceImpl<StoreMapper, Store> implements StoreServiceImpl {

    @Autowired
    StoreMapper storeMapper;
    @Autowired
    UserMapper userMapper;

    //新增商店
    public Store addStore(Store store) {
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("store_name", store.getStoreName());
        if (storeMapper.exists(queryWrapper)) {
            throw new AddException(CodeConstant.ACCOUNT_EXIST, "门店名已存在");
        } else {
            storeMapper.insert(store);
            return store;
        }
    }

    //更新商店信息
    public Store updateStore(Store store) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", store.getOwnerId());
        if (store.getStatus() != 0 && store.getStatus() != 1) {
            throw new UpdateException(CodeConstant.INVALID_INPUT, "状态字段为非法输入");
        }
        if (!userMapper.exists(userQueryWrapper) && store.getOwnerId() != null) {
            throw new UpdateException(CodeConstant.DATA_NOT_EXIST, "该用户不存在");
        }
//        QueryWrapper<Store> storeQueryWrapper = new QueryWrapper<>();
//        storeQueryWrapper.eq("store_name", store.getStoreName());
//        if (storeMapper.selectOne(storeQueryWrapper) != null) {
//            throw new UpdateException(CodeConstant.ACCOUNT_EXIST, "门店名已存在");
//        }
        //更新商店
        if (storeMapper.updateById(store) == 0) {
            throw new UpdateException(CodeConstant.USER_NOT_EXIST, "门店不存在");
        } else {
            if (store.getOwnerId() != null) {
                store.setOwnerName(userMapper.getUserById(store.getOwnerId()).getUsername());
            }
            return store;
        }
    }

    // 删除门店信息
    public void deleteStoreById(Integer id) {
        int res = storeMapper.deleteById(id);
        if (res == 0) {
            throw new AddException(CodeConstant.DATA_NOT_EXIST, "该门店不存在");
        }
    }


    //获取所有门店信息
    public PageBean getAllStore(PageArgs pageArgs) {
        if (pageArgs.pageNum == null || pageArgs.pageSize == null) {
            throw new ArgException(CodeConstant.ARGS_ERROR, "参数错误");
        } else {
            PageHelper.startPage(pageArgs.pageNum, pageArgs.pageSize);
            Page<Store> page = (Page<Store>) storeMapper.selectAllStores();
            return new PageBean(page.getTotal(), page.getResult());
        }
    }

    //条件查询门店信息
    public PageBean searchStores(SearchStoreRequest request) {
        if (request.pageNum == null || request.pageSize == null) {
            throw new ArgException(CodeConstant.ARGS_ERROR, "参数错误，分页数据不能为null");
        } else {
            PageHelper.startPage(request.getPageNum(), request.getPageSize());
            Page<Store> page = (Page<Store>) storeMapper.searchStores(request);
            return new PageBean(page.getTotal(), page.getResult());
        }
    }

    //获取全部门店信息用于Excel导出
    public List<Store> getAllStoreInfo() {
        List<Store> stores = storeMapper.selectList(null);
        for (Store store : stores) {
            if (store.getOwnerId() != null) {
                store.setOwnerName(userMapper.getUserById(store.getOwnerId()).getUsername());
            }
        }
        return stores;
    }
}
