package org.example.user.service.Impl;

import org.example.user.response.MenuResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonService {

    public List<MenuResponse> getMenuItemsByUserId() {
        List<MenuResponse> menuItems = new ArrayList<>();

        menuItems.add(createMenuItem("/home", "home", "首页", "House", "home/Home.vue", null, null));
        menuItems.add(createMenuItem("/store", "store", "门店管理", "Shop", "DefaultView.vue", createMeta("门店管理"), createStoreChildren()));
        menuItems.add(createMenuItem("/employee", "employee", "员工管理", "User", "DefaultView.vue", createMeta("员工管理"), createEmployeeChildren()));
        menuItems.add(createMenuItem("/goods", "goods", "商品管理", "Box", "DefaultView.vue", createMeta("商品管理"), createGoodsChildren()));
        menuItems.add(createMenuItem("/guest", "guest", "客户管理", "Service", "DefaultView.vue", createMeta("客户管理"), createGuestChildren()));
        menuItems.add(createMenuItem("/setting", "setting", "个人中心", "Setting", "setting/Setting.vue", createMeta("个人中心"), null));

        return menuItems;
    }

    private MenuResponse createMenuItem(String path, String name, String label, String icon, String url, Map<String, String> meta, List<MenuResponse> children) {
        MenuResponse item = new MenuResponse();
        item.setPath(path);
        item.setName(name);
        item.setLabel(label);
        item.setIcon(icon);
        item.setUrl(url);
        item.setMeta(meta);
        item.setChildren(children);
        return item;
    }

    private Map<String, String> createMeta(String title) {
        Map<String, String> meta = new HashMap<>();
        meta.put("title", title);
        return meta;
    }

    private List<MenuResponse> createStoreChildren() {
        List<MenuResponse> children = new ArrayList<>();
        children.add(createMenuItem("/addStore", "addStore", "添加门店", "CirclePlus", "store/AddStore.vue", createMeta("添加门店"), null));
        children.add(createMenuItem("/storeList", "storeList", "门店列表", "Tickets", "store/Store.vue", createMeta("门店列表"), null));
        return children;
    }

    private List<MenuResponse> createEmployeeChildren() {
        List<MenuResponse> children = new ArrayList<>();
        children.add(createMenuItem("/addEmployee", "addEmployee", "添加员工", "CirclePlus", "employee/AddEmployee.vue", createMeta("添加员工"), null));
        children.add(createMenuItem("/employeeList", "employeeList", "员工列表", "Tickets", "employee/Employee.vue", createMeta("员工列表"), null));
        return children;
    }

    private List<MenuResponse> createGoodsChildren() {
        List<MenuResponse> children = new ArrayList<>();
        children.add(createMenuItem("/goods/addGoods", "addGoods", "添加商品", "CirclePlus", "goods/AddGoods.vue", createMeta("添加商品"), null));
        children.add(createMenuItem("/goods/goodsList", "goodsList", "商品列表", "Tickets", "goods/Goods.vue", createMeta("商品列表"), null));
        return children;
    }

    private List<MenuResponse> createGuestChildren() {
        List<MenuResponse> children = new ArrayList<>();
        children.add(createMenuItem("/guest/guestList", "guestList", "客户列表", "Tickets", "guest/Guest.vue", createMeta("客户列表"), null));
        return children;
    }
}
