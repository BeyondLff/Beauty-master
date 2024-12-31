package org.example.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.user.entity.User;
import org.example.user.entity.Menu;
import org.example.user.request.ImageRequest;
import org.example.user.request.QueryUserRequest;
import org.example.user.request.RegisterRequest;
import org.example.user.request.UpdateRequest;
import org.example.user.response.UserResponse;

import java.math.BigInteger;
import java.util.List;

/**
 * 用户映射器接口，用于定义数据库操作方法
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT stores_id FROM users WHERE id = #{userId}")
    Integer getStoreIdByUserId(@Param("userId") Integer userId);
    /**
     * 根据账号和密码查询用户信息
     *
     * @param account 登录请求对象，包含用户账号和密码
     * @return 查询到的用户信息，若未找到则返回null
     */
    @Select("select id,password from users where account=#{account}")
    public User Login(String account);

    @Select("select *from users")
    public List<User> getAllUsers();

    /**
     * 查询所有用户信息
     *
     * @return 查询到的用户信息，若未找到则返回null
     */
//  @Select("select id,account,username,phone_number,password,status,stores_id,image,status from beauty_schema.users " +
//          "where users.stores_id=#{id} and users.id=#{id}")
//  public List<AllUserResponse> getAllInfo(Integer id);

    //删除用户
    @Delete("DELETE FROM users WHERE id = #{id}")
    void deleteUser(Integer id);

    @Select("select * from users where id=#{id}")
    public User getUserById(Integer id);


    @Select("select stores_id from users where id=#{id}")
    Integer getBeautyId(Integer id);

    @Select("select users.phone_number from users where phone_number=#{phoneNumber}")
    String getPhone(String phoneNumber);

    //用户注册
    @Insert("insert into users(username,password,phone_number,account,status,image,idcard,home_address,gender) " +
            "values (#{request.username},#{password},#{request.phoneNumber},#{account},#{status}," +
            "#{img},#{request.idcard},#{request.homeAddress},#{request.gender})")
    void Register(RegisterRequest request, BigInteger account, String password, Integer status, String img);


    @Select("select id from account")
    BigInteger selectAccount();

    //用户账号表加加
    @Update("update account set id=id+1")
    void UpdateAccount();


    //保存用户头像
    @Update("update users set image=#{image} where id=#{id}")
    void saveUserImage(String image, Integer id);

    //获取某店的所有店员信息
    @Select("select users.id,users.account,username,users.phone_number,stores.store_name,role.name role,users.status " +
            "from users,stores,role " +
            "where users.stores_id=#{beautyId} and users.stores_id=stores.id and role.id=" +
            "(select role_id from user_role where user_id=users.id)")
    List<UserResponse> getAllInfo(Integer beautyId);


    @Update("update users set password=#{password} where id=#{id}")
    void updatePassword(int id, String newPassword);

    //用户查询
    List<UserResponse> searchUserInfo(QueryUserRequest request);

    @Insert("")
    void addUser(User adminUser);  //还没实现

    //根据account获取id
    @Select("select id from users where account=#{account}")
    Integer getId(BigInteger account);


    //新增用户后同时修改user_role表添加对应职位
    @Insert("insert into user_role(user_id, role_id) " +
            "values (#{id},#{role})")
    void addUserRole(Integer id, Integer role);

    //根据id搜用户
    @Select("select * from users where id=#{id}")
    User selectUserById(Integer id);

    //动态更新用户数据
    void updateUser(UpdateRequest request);

    @Select("select name from role where id=(" +
            "select role_id from user_role where user_id=#{id})")
    String getRole(Integer id);

    @Select("select * from users where account=#{account}")
    User getUserByAccount(String account);

    @Select("select * from users where idcard=#{idCard} LIMIT 1")
    User getUserByIdCard(String idCard);

    @Select("select * from users where phone_number=#{phoneNumber} LIMIT 1")
    User getUserByPhoneNumber(String phoneNumber);

    @Select("select users.image from users where id=#{id}")
    String getImage(Integer id);

    @Update("update user_role set role_id=3 where user_id=#{id}")
    void setManager(Integer id);

}
