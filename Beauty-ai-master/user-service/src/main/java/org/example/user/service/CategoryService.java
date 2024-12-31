package org.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.user.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {
    List<Category> getCategory();

}
