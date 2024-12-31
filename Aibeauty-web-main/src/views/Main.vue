<template>
  <div class="common-layout">
    <common-header />
    <el-container>
      <common-aside />

      <div class="breadcrumb-container">
        <el-breadcrumb separator="/" style="padding-left: 20px; padding-top: 8px;">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="currentMenu.path" v-if="currentMenu">{{ currentMenu.label }}</el-breadcrumb-item>
        </el-breadcrumb>
        <router-view />
      </div>
    </el-container>
  </div>
</template>

<script>
import { defineComponent, reactive, toRefs } from 'vue'
import CommonHeader from '../components/CommonHeader.vue'
import CommonAside from '../components/CommonAside.vue'
import { useMenuStore } from '../assets/stores'
export default defineComponent({
  components: {
    CommonHeader,
    CommonAside
  },
  setup() {
    const menuStore = useMenuStore();
    const { currentMenu } = toRefs(menuStore);

    // 可以使用currentMenu
    console.log("页面的是", currentMenu);
    return {
      currentMenu,
    }
  },
})
</script>

<style lang="less" scoped>
.common-layout {
  width: 100vw;
  height: 100%;
  background-color: #eee;
}

.el-container {
  height: 100%;

  .el-aside {
    height: 100%;
    background-color: #fff;
    margin-top: 2px;
  }
}

/* 确保面包屑在router-view上方 */
.breadcrumb-container {
  // position: relative;
  // z-index: 1;
  min-width: 80%;
  height: 100%;
  flex: 1;
}

.el-breadcrumb /deep/ span {
  cursor: pointer !important;
}
</style>