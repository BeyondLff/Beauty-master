package org.example.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.user.entity.Customers;

@Mapper
public interface CustomersMapper extends BaseMapper<Customers> {
}
