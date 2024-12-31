package org.example.user.constant;

public class CodeConstant {
    public static Integer success = 200; //操作成功
    public static Integer PASSWORD_ERROR = 4000;   //密码错误
    public static Integer USER_NOT_EXIST = 4001;  //用户不存在
    public static Integer DATA_NOT_EXIST = 4002;  //数据不存在
    public static Integer ACCOUNT_EXIST = 4003;  //账号已存在-注册返回
    public static Integer AUTHORITY_ERROR = 4004;    //权限错误
    public static Integer POSITION_NOT_EXIST = 4005; //职位不存在

    public static Integer ARGS_ERROR = 4006; //传入参数有误

    public static Integer IDENTIFY_ERROR = 4007; //身份认证错误
    public static Integer ALREADY_LOGIN = 4008; //用户已经登录

    public static Integer INVALID_INPUT = 4009;//非法输入

}
