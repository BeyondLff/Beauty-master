package org.example.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.user.constant.CodeConstant;
import org.example.user.entity.Customers;
import org.example.user.entity.FollowUp;
import org.example.user.exception.ArgException;
import org.example.user.mapper.CustomersMapper;
import org.example.user.mapper.FollowUpMapper;
import org.example.user.utils.myThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowUpServiceImpl extends ServiceImpl<FollowUpMapper, FollowUp> implements IService<FollowUp> {
    @Autowired
    FollowUpMapper followUpMapper;
    @Autowired
    CustomersMapper customersMapper;

    public void addFollowUp(FollowUp followUp) {
        int id = myThreadLocal.get();
        System.out.println("id:"+followUp.getCustomerId())    ;
        followUp.setUserId(id);
        int rows = followUpMapper.insert(followUp);
        if (rows == 0) {
            throw new ArgException(CodeConstant.INVALID_INPUT, "添加失败");
        }
    }

    public void editFollowUp(FollowUp followUp) {
        followUp.setUserId(myThreadLocal.get());
        int rows = followUpMapper.updateById(followUp);
        if (rows == 0) {
            throw new ArgException(CodeConstant.INVALID_INPUT, "修改失败");
        }
    }

    public List<FollowUp> searchFollowUps(Customers customers) {
        QueryWrapper<Customers> queryWrapper = new QueryWrapper<>();
        String phoneNumber = customers.getPhoneNumber();
        String name = customers.getName();
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            queryWrapper.eq("phone_number", phoneNumber);
        }
        if (name != null && !name.isEmpty()) {
            queryWrapper.eq("name", name);
        }
        queryWrapper.select("id");
        List<Integer> customer_ids = customersMapper.selectObjs(queryWrapper).stream()
                .map(obj -> (Integer) obj)
                .toList();
        QueryWrapper<FollowUp> followUpQueryWrapper = new QueryWrapper<>();
        followUpQueryWrapper.in("customer_id", customer_ids);
        return followUpMapper.selectList(followUpQueryWrapper);
    }

    //删除跟进
    public void deleteFollowUp(int id) {
        int rows = followUpMapper.deleteById(id);
        if (rows == 0) {
            throw new ArgException(CodeConstant.INVALID_INPUT, "删除失败");
        }
    }
}
