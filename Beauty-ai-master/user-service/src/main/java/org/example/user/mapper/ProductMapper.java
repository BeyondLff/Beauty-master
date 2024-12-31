package org.example.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.user.entity.Product;
import org.example.user.response.ListResponse;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    List<ListResponse> listAll(List<Integer>ids);
}
