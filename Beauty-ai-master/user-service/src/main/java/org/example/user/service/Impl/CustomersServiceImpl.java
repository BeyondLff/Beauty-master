package org.example.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.example.user.constant.CodeConstant;
import org.example.user.entity.Customers;
import org.example.user.entity.PageBean;
import org.example.user.exception.ArgException;
import org.example.user.mapper.CustomersMapper;
import org.example.user.request.PageArgs;
import org.example.user.response.ListCustomersResponse;
import org.example.user.response.Page;
import org.example.user.service.CustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.user.utils.myThreadLocal;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomersServiceImpl extends ServiceImpl<CustomersMapper, Customers> implements CustomersService {
    @Autowired
    CustomersMapper customersMapper;

    public Map<Character, List<Customers>> getCustomersList() {
//        int id = myThreadLocal.get();
//        if (myThreadLocal.get() == null) {
//            throw new ArgException(CodeConstant.USER_NOT_EXIST, "用户不存在");
//        }
//        QueryWrapper<Customers> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_id", id);
        List<Customers> customers = customersMapper.selectList(null);
        Map<Character, List<Customers>> groupedByLetter = customers.stream()
                .collect(Collectors.groupingBy(Customers::getLetter));
        return groupedByLetter;
    }

    public Customers getCustomerById(int id) {
        if (customersMapper.selectById(id) == null) {
            throw new ArgException(CodeConstant.USER_NOT_EXIST, "用户不存在");
        }
        return customersMapper.selectById(id);
    }

    //新增客户
    public void addCustomer(Customers customers) {
        int id = myThreadLocal.get();
        String firstLetter = getFirstPinYin(customers.getName());
        customers.setLetter(firstLetter.charAt(0));
        customers.setUserId(id);
        int row = customersMapper.insert(customers);
        if (row == 0) {
            throw new ArgException(CodeConstant.ACCOUNT_EXIST, "新增客户失败");
        }
    }

    //编辑客户
    public void editCustomer(Customers customers) {
        int id = myThreadLocal.get();
        String firstLetter = getFirstPinYin(customers.getName());
        customers.setLetter(firstLetter.charAt(0));
        customers.setUserId(id);
        int row = customersMapper.updateById(customers);
        if (row == 0) {
            throw new ArgException(CodeConstant.ACCOUNT_EXIST, "编辑客户失败");
        }
    }

    //汉语字符串转拼音首字母
    public static String getFirstPinYin(String hanyu) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        StringBuilder firstPinyin = new StringBuilder();
        char[] hanyuArr = hanyu.trim().toCharArray();
        try {
            for (int i = 0, len = hanyuArr.length; i < len; i++) {
                if (Character.toString(hanyuArr[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] pys = PinyinHelper.toHanyuPinyinStringArray(hanyuArr[i], format);
                    firstPinyin.append(pys[0].charAt(0));
                } else {
                    firstPinyin.append(hanyuArr[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
        return firstPinyin.toString();
    }

    //删除客户
    public void deleteCustomer(int id) {
        int row = customersMapper.deleteById(id);
        if (row == 0) {
            throw new ArgException(CodeConstant.ACCOUNT_EXIST, "删除客户失败");
        }
    }


}
