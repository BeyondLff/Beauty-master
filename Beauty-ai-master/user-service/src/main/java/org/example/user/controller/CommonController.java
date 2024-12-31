package org.example.user.controller;

import org.example.user.entity.Result;
import org.example.user.response.MenuResponse;
import org.example.user.service.Impl.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/common")
public class CommonController {
    @Autowired
    CommonService commonService;

    @GetMapping("/getMenu")
    public Result<List<MenuResponse>> getMenu() {
        List<MenuResponse> menuItems = commonService.getMenuItemsByUserId();
        return Result.success(menuItems);
    }
}
