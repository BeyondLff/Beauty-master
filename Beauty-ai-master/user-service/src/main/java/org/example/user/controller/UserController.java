package org.example.user.controller;

import org.example.user.constant.CodeConstant;
import org.example.user.entity.PageBean;
import org.apache.ibatis.annotations.Param;
import org.example.user.entity.Result;
import org.example.user.entity.User;
import org.example.user.exception.ActionException;
import org.example.user.exception.LoginException;
import org.example.user.exception.RegisterException;
import org.example.user.request.*;
import org.example.user.response.LoginResponse;
import org.example.user.response.RegisterResponse;
import org.example.user.service.Impl.SecurityLoginService;
import org.example.user.service.Impl.UserService;
import org.example.user.utils.AliOSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @Autowired
    SecurityLoginService securityLoginService;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 用户登录接口
     *
     * @param request 包含用户账号和密码的登录请求对象
     * @return 返回登录结果，成功则返回用户信息，失败则返回错误信息
     */
    @PostMapping("/login")
    public Result<LoginResponse> Login(@RequestBody LoginRequest request) {
        try {
            System.out.println("1");
            LoginResponse login = securityLoginService.Login(request);
            return Result.success(login);
        } catch (LoginException ex) {
            return Result.error(ex.getCode(), ex.getMessage());
        }

    }

    //注册接口
    @PostMapping("/register")
    public Result<RegisterResponse> Register(@RequestBody RegisterRequest request) {
        try {
            RegisterResponse register = userService.Register(request);
            return Result.success(200, "register success", register);
        } catch (RegisterException ex) {
            return Result.error(ex.getCode(), ex.getMessage());
        }

    }

    /**
     * 获取某店长所有对应店铺中用户信息
     *
     * @return 返回获取到的信息
     */
    @GetMapping("/getAllInfo")
    public Result getAllInfo(@RequestParam Integer pageNum,@RequestParam Integer pageSize) throws Exception {
        PageArgs pageArgs = new PageArgs();
        pageArgs.setPageNum(pageNum);
        pageArgs.setPageSize(pageSize);
        PageBean pageBean = userService.getAllInfo(pageArgs);
        return Result.success(200, "success get", pageBean);
    }

    /**
     * 获取用户信息接口
     *
     * @param request 要查询的信息封装类
     * @return 返回获取的用户信息
     */
    @GetMapping("/searchUserInfo")
    public Result searchUserInfo(@RequestBody QueryUserRequest request) {
        return Result.success(200, "Query success", userService.searchUserInfo(request));
    }

    /**
     * 删除用户接口
     *
     * @param id 删除用户的id
     * @return 返回删除结果
     */
    @DeleteMapping("/delete")
    public Result deleteUser(@RequestParam("id") Integer id) {
        try {
            userService.deleteUser(id);
            return Result.success(200, "delete success", id);
        } catch (ActionException ex) {
            return Result.error(ex.getCode(), ex.getMessage());
        }

    }

    @PutMapping("/updateUser")
    public Result updateUser(@RequestBody UpdateRequest request) {
        try {
            userService.updateUser(request);
            return Result.success(200, "update success");
        } catch (ActionException ex) {
            return Result.error(ex.getCode(), ex.getMessage());
        }

    }

    //头像上传接口
    @PostMapping("/uploadimage")
    public Result uploadImage(MultipartFile image,Integer id) throws IOException {
        String url= userService.uploadImage(image,id);
        return Result.success(200,"头像上传成功！",url);
    }

    /**
     * 更新用户密码
     *
     * @return 返回获取到的信息
     */
    @PostMapping("/updatePassword")
    public Result<User> updatePassword(@RequestBody UpdatePasswordRequest request, @Param("id") Integer id) {
        return userService.updatePassword1(request);
    }

    @PostMapping("/logout")
    public Result logout(@RequestParam Integer id){
        redisTemplate.delete(id.toString());
        return Result.success(200,"退出登录成功！");
    }

    @GetMapping("/getImage")
    public Result<String> getImage(){
        return Result.success(CodeConstant.success,"请求成功！",userService.getImage());
    }

    @PostMapping("/setManager")
    public Result setManager(@RequestParam Integer id){
        userService.setManager(id);
        return Result.success(200,"店长设置成功!");
    }
}
