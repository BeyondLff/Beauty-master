package org.example.user.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.user.entity.Menu;
import org.example.user.response.MenuItemResponse;
import org.example.user.response.MenuResponse;
import org.springframework.security.access.ConfigAttribute;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommonMapper {
    @MapKey("id")
    Map<Integer,MenuResponse> getMenusByUserId(String role);
    @MapKey("parent")
    List<MenuResponse> getChildrenMenus();
    //获取路径对应的访问权限
    @Select("select role.name from role,permission,role_permission where role.id=role_permission.role_id " +
            "and permission.id=role_permission.permission_id and permission.path=#{url}")
    List<String> getRoles(String url);
}
