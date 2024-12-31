package org.example.user.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.user.constant.CodeConstant;
import org.example.user.entity.*;
import org.example.user.exception.*;
import org.example.user.mapper.UserMapper;
import org.example.user.request.*;
import org.example.user.response.MenuItemResponse;
import org.example.user.response.RegisterResponse;
import org.example.user.response.UserResponse;
import org.example.user.utils.AliOSSUtils;
import org.example.user.utils.myThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.*;
import java.math.BigInteger;
import java.util.stream.Collectors;

import static org.example.user.constant.CodeConstant.ACCOUNT_EXIST;


/**
 * 用户服务类，用于处理用户相关的业务逻辑
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AliOSSUtils aliOSSUtils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 用户登录方法
     *
     * @param username 登录请求对象，包含用户账号
     * @return 登录成功返回token和id，失败抛出对应异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.Login(username);
        if(user==null){
            throw new LoginException(CodeConstant.USER_NOT_EXIST,"用户不存在");
        }
        user.setRole(userMapper.getRole(user.getId()));
        return new LoginUser(user);
    }

    /**
     * 用户注册
     *
     * @param request 账号和密码
     * @return 注册成功返回对应信息，失败抛出异常
     */
    public RegisterResponse Register(RegisterRequest request) {
        if (userMapper.getUserByPhoneNumber(request.getPhoneNumber()) != null) {
            throw new RegisterException("电话号码已注册", ACCOUNT_EXIST);
        }
        if (userMapper.getUserByIdCard(request.getIdcard()) != null) {
            throw new RegisterException("身份证已注册", ACCOUNT_EXIST);
        }
        BigInteger account = userMapper.selectAccount();
        String password = bCryptPasswordEncoder.encode("123456");
        Integer status = 1;
        String img = "https://ai-beauty.oss-cn-shenzhen.aliyuncs.com/4a5f9f40-d1df-4c1c-b47e-dc557999ef12.png";
        userMapper.Register(request, account, password, status, img);
        Integer id = userMapper.getId(account);
        userMapper.addUserRole(id, 4);
        userMapper.UpdateAccount();
        return new RegisterResponse(img, account, "123456");
    }

    /**
     * 获取用户信息方法
     *
     * @param request 查询信息集合
     * @return 获取到的用户信息，若未找到则返回null
     */
    public List<UserResponse> searchUserInfo(QueryUserRequest request) {
        return userMapper.searchUserInfo(request);
    }

    /**
     * 获取所有用户信息
     *
     * @return 获取到的用户信息，若未找到则返回null
     */

    public PageBean getAllInfo(PageArgs pageArgs) throws Exception {
        Integer beauty = userMapper.getBeautyId(myThreadLocal.get());
        PageHelper.startPage(pageArgs.pageNum, pageArgs.pageSize);
        Page<UserResponse> page = (Page<UserResponse>) userMapper.getAllInfo(beauty);
        return new PageBean(page.getTotal(), page.getResult());
    }

    /**
     * 删除某用户信息
     */
    public void deleteUser(Integer id) {
        if(userMapper.selectUserById(id)==null){
            throw new ActionException(CodeConstant.USER_NOT_EXIST,"用户不存在");
        }else{
            userMapper.deleteUser(id);
        }
    }

    public void updateUser(UpdateRequest request) {
        if (userMapper.selectUserById(request.getId()) == null) {
            throw new ActionException(CodeConstant.USER_NOT_EXIST, "用户不存在");
        } else {
            userMapper.updateUser(request);
        }
    }


    /**
     * 执行更改id用户密码的操作
     */
    public void UpdateNewPassword(int id, String newPassword) {
        userMapper.updatePassword(id, newPassword);
    }

    /**
     * 判断旧密码是否正确
     *
     * @return 返回判断值
     */
    public boolean validateOldPassword(int userId, String oldPassword) {
        User user = userMapper.getUserById(userId);
        String storePassword = user.getPassword();
        return storePassword.equals(oldPassword);
    }

    /**
     * 更改密码，结果分为三种，更改成功，旧密码错误，两次输入新密码失败
     *
     * @return 返回这三种中哪种结果
     */
    public Result<User> updatePassword1(UpdatePasswordRequest request) {
        boolean isOldPasswordValid = validateOldPassword(request.getId(), request.getOldPassword());
        if (isOldPasswordValid) {
            if (request.getNewPassword().equals(request.getConfirmNewPassword())) {
                UpdateNewPassword(request.getId(), request.getNewPassword());
                User updatedUser = getUserInfoById(request.getId());
                return Result.success(HttpStatus.OK.value(), "User updated successfully", updatedUser);
            } else {
                return Result.error(HttpStatus.BAD_REQUEST.value(), "New passwords do not match");
            }
        } else {
            return Result.error(HttpStatus.BAD_REQUEST.value(), "Old password is wrong");
        }
    }

    public User getUserInfoById(Integer id) {
        return new User();
    }

    public String uploadImage(MultipartFile image,Integer id) throws IOException {
        // 处理图片上传逻辑，例如使用AliOSSUtils上传到阿里云对象存储
        if(image==null){
            throw new UploadException(CodeConstant.ARGS_ERROR,"头像不能为空");
        }
        String url=aliOSSUtils.imageUpLoad(image);
        userMapper.saveUserImage(url,id);
        return url;
    }

    public String getImage(){
        String result;
        if((result=userMapper.getImage(myThreadLocal.get()))==null){
            throw new ArgException(CodeConstant.DATA_NOT_EXIST,"用户信息不存在");
        }
        return result;
    }

    public void setManager(Integer id) {
        if(userMapper.getUserById(id)==null)
            throw new ActionException(CodeConstant.USER_NOT_EXIST,"用户不存在");
        userMapper.setManager(id);
    }

}
