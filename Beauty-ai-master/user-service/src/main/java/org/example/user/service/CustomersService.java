package org.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.user.entity.Customers;

import java.util.List;
import java.util.Map;

public interface CustomersService extends IService<Customers> {
    Map<Character, List<Customers>> getCustomersList();


}
