package org.example.user.controller;

import org.example.user.entity.Customers;
import org.example.user.entity.FollowUp;
import org.example.user.entity.Result;
import org.example.user.exception.ArgException;
import org.example.user.request.PageArgs;
import org.example.user.response.ListCustomersResponse;
import org.example.user.service.CustomersService;
import org.example.user.service.Impl.CustomersServiceImpl;
import org.example.user.service.Impl.FollowUpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    CustomersServiceImpl customersService;
    @Autowired
    FollowUpServiceImpl followUpService;

    //获取客户列表
    @GetMapping("/getList")
    public Result<List<ListCustomersResponse>> getCustomersListController(){
        Map<Character, List<Customers>> groupedByLetter = customersService.getCustomersList();
        return Result.success(groupedByLetter.entrySet().stream()
                .map(entry -> {
                    ListCustomersResponse response = new ListCustomersResponse();
                    response.setLetter(entry.getKey());
                    response.setCustomers(entry.getValue());
                    return response;
                })
                .collect(Collectors.toList()));
    }

//    web端获取客户列表
//    @GetMapping("/getWebList")
//    public Result<List<Customers>> getWebCustomersListController(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
//        PageArgs pageArgs = new PageArgs();
//        pageArgs.setPageNum(pageNum);
//        pageArgs.setPageSize(pageSize);
////        return Result.success(customersService.listCustomers(pageArgs));
//    }
    //获取客户详细信息
    @GetMapping("/getInfo")
    public Result<Customers> getCustomerById(@RequestParam("id") Integer id) {
        try {
            return Result.success(customersService.getCustomerById(id));
        } catch (ArgException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    //新增客户
    @PostMapping("/addGuest")
    public Result addCustomer(@RequestBody Customers customers) {
        try {
            customersService.addCustomer(customers);
            return Result.success(200, "新增客户成功");
        } catch (ArgException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    //更新客户信息
    @PostMapping("/editGuest")
    public Result editCustomer(@RequestBody Customers customers) {
        try {
            customersService.editCustomer(customers);
            return Result.success(200, "更新客户信息成功");
        } catch (ArgException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    //删除客户
    @DeleteMapping("/deleteGuest")
    public Result deleteCustomer(@RequestParam("id") Integer id) {
        try {
            customersService.deleteCustomer(id);
            return Result.success(200, "删除客户成功");
        } catch (ArgException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    //新增客户跟进
    @PostMapping("/addFollowUp")
    public Result addFollowUp(@RequestBody FollowUp followUp) {
        try {
            followUpService.addFollowUp(followUp);
            return Result.success(200, "新增跟进成功");
        } catch (ArgException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    //修改客户跟进
    @PostMapping("/editFollowUp")
    public Result editFollowUp(@RequestBody FollowUp followUp) {
        try {
            followUpService.editFollowUp(followUp);
            return Result.success(200, "修改跟进成功");
        } catch (ArgException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    //查询客户跟进
    @PostMapping("/followHistory")
    public Result<List<FollowUp>> searchFollowUps(@RequestBody Customers customers) {
        try {
            return Result.success(followUpService.searchFollowUps(customers));
        } catch (ArgException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    //删除跟进
    @DeleteMapping("/deleteFollowUp")
    public Result deleteFollowUp(@RequestParam("id") Integer id) {
        try {
            followUpService.deleteFollowUp(id);
            return Result.success(200, "删除跟进成功");
        } catch (ArgException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }
}
