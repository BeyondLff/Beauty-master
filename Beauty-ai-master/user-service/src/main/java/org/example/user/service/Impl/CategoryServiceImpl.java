package org.example.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.user.entity.Category;
import org.example.user.mapper.CategoryMapper;
import org.example.user.service.CategoryService;
import org.example.user.utils.myThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{
    @Autowired
    CategoryMapper categoryMapper;
    public List<Category> getCategory(){
        return this.list();
    }
}
