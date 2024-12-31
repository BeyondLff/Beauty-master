<template>
  <el-aside>
    <el-menu :default-active="$route.path" router class="el-menu-vertical-demo">
      <!-- 根据接口返回的数据动态渲染左侧导航栏 -->
      <!-- 没有子菜单的菜单项 -->
      <el-menu-item :index="item.path" v-for="item in noChildren()" :key="item.path" @click="clickMenu(item)">
        <component class="icons" :is="item.icon"></component>
        <span>{{ item.label }}</span>
      </el-menu-item>
      <!-- 子菜单的渲染 -->
      <el-sub-menu :index="item.label" v-for="item in hasChildren()" :key="item.path">
        <template #title>
          <component class="icons" :is="item.icon"></component>
          <span>{{ item.label }}</span>
        </template>
        <el-menu-item-group>
          <el-menu-item :index="subItem.path" v-for="(subItem, subIndex) in item.children" :key="subIndex"
            @click="clickMenu(subItem)">
            <component class="icons" :is="subItem.icon"></component>
            <span>{{ subItem.label }}</span>
          </el-menu-item>
        </el-menu-item-group>
      </el-sub-menu>
    </el-menu>
  </el-aside>
</template>

<script>
import { defineComponent, ref, toRefs, onMounted, getCurrentInstance } from 'vue'
import { useAsideStore, useIdStore, useMenuStore } from '../assets/stores/index.js'
import { useRouter } from 'vue-router';
import router from '../router/index.js';

export default defineComponent({
  setup() {
    const asideStore = useAsideStore();
    const idStore = useIdStore();
    const menuStore = useMenuStore();
    const { proxy } = getCurrentInstance();
    const router = useRouter();

    //从仓库取得菜单数组进行判断渲染
    const menuList = menuStore.$state.menuList;
    // console.log(menuStore.$state.menuList);
    const noChildren = () => {
      return menuList.filter((item) => !item.children);
    };

    const hasChildren = () => {
      return menuList.filter((item) => item.children);
    };
    //菜单跳转
    const clickMenu = (item) => {
      menuStore.selectMenu(item);
      router.push({
        name: item.name,
      });
    };
    return {
      asideStore,
      noChildren,
      hasChildren,
      clickMenu,
    }
  },
})
</script>

<style lang="less" scoped>
.el-aside {
  width: 180px;
  height: 100%;
}

.el-menu {
  border-right: none;
}

.icons {
  height: 18px;
  width: 18px;
}
</style>