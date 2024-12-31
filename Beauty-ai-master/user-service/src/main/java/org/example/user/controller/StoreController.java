package org.example.user.controller;

import org.example.user.entity.PageBean;
import org.example.user.entity.Result;
import org.example.user.entity.Store;
import org.example.user.exception.ActionException;
import org.example.user.exception.AddException;
import org.example.user.exception.ArgException;
import org.example.user.exception.UpdateException;
import org.example.user.request.PageArgs;
import org.example.user.request.SearchStoreRequest;
import org.example.user.service.Impl.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    //新增商店
    @PostMapping("/addStore")
    public Result<Store> addStoreController(@RequestBody Store store) {
        try {
            Store newStore = storeService.addStore(store);
            return Result.success(200, "新增门店成功", newStore);
        } catch (AddException ex) {
            return Result.error(ex.getCode(), ex.getMessage());
        }
    }

    //获取全部门店信息
    @PostMapping("/getAllStoresInfo")
    public Result<PageBean> getAllStoreController(@RequestBody PageArgs pageArgs) {
        try {
            PageBean storeList = storeService.getAllStore(pageArgs);
            return Result.success(200, "成功获取全部门店信息", storeList);
        } catch (ArgException ex) {
            return Result.error(ex.getCode(), ex.getMessage());
        }
    }

    //条件查询门店信息
    @GetMapping("/searchStoresInfo")
    public Result<PageBean> searchStoresController(@RequestBody SearchStoreRequest searchStoreRequest) {
        try {
            PageBean storeList = storeService.searchStores(searchStoreRequest);
            return Result.success(200, "查询门店信息成功", storeList);
        } catch (ArgException ex) {
            return Result.error(ex.getCode(), ex.getMessage());
        }
    }

    //更新单个门店信息
    @PutMapping("/updateInfo")
    public Result<Store> updateStoreInfoController(@RequestBody Store store) {
        try {
            Store updatedStore = storeService.updateStore(store);
            return Result.success(200, "更新门店成功", updatedStore);
        } catch (UpdateException ex) {
            return Result.error(ex.getCode(), ex.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public Result deleteStoreController(@RequestParam("id") Integer id) {
        try {
            storeService.deleteStoreById(id);
            return Result.success(200, "删除门店成功");
        } catch (ActionException ex) {
            return Result.error(ex.getCode(), ex.getMessage());
        }
    }

    @GetMapping("/exportAllStore")
    public Result<List<Store>> exportAllStoreController() {
        return Result.success(200, "导出所有门店信息成功", storeService.getAllStoreInfo());
    }

}
