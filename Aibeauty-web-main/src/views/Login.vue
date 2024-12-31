<template>
  <div class="box">
    <!-- 登录框 -->
    <el-form :model="loginForm" class="login-container">
      <h3>系统登录</h3>
      <el-form-item>
        <el-input type="input" placeholder="请输入账号" v-model="loginForm.account">
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-input :type="showPassword ? 'text' : 'password'" placeholder="请输入密码" v-model="loginForm.password">
          <template #append>
            <el-button @click="showPassword = !showPassword" :icon="showPassword ? 'View' : 'Hide'"></el-button>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="login">
          登录
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { defineComponent, onMounted, reactive, getCurrentInstance, ref } from 'vue'
import axios from 'axios'
import router from '../router';
import { useRouter } from 'vue-router';
import { useTokenStore, useIdStore, useMenuStore } from '../assets/stores/index.js'
import { ElMessage } from 'element-plus';

export default defineComponent({
  setup() {
    //双向绑定数据，否则无法输入
    const loginForm = reactive({
      account: 'admin',
      password: '',
    });
    const { proxy } = getCurrentInstance();
    const router = useRouter();
    const tokenStore = useTokenStore();
    const idStore = useIdStore();
    const menuStore = useMenuStore();
    const showPassword = ref(false);

    //登录接口，登录时就要获取id、token还有菜单列表
    const login = async () => {
      const res = await proxy.$api.login(loginForm);
      console.log(res);
      tokenStore.setToken(res.token);
      idStore.setId(res.id);
      // tokenStore.setToken('eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MzYsImV4cCI6MTcxODk2NDM1N30.ak76X4pjGaFTi2ja73BQ9boBfWktV0fL6TEojQ5o_pY');
      ElMessage.success("登录成功！");
      //登录成功后等待另一个接口获取菜单数据
      await getMenu();
      router.push({
        name: 'home',
      });
    }

    //获取左侧导航菜单列表的接口，根据id返回不同的导航栏
    const getMenu = async () => {
      const res = await proxy.$api.getMenu();
      menuStore.setMenuList(res);
      menuStore.setMenuRouter(res);
    }
    return {
      loginForm,
      login,
      tokenStore,
      showPassword,
    }
  },
})
</script>

<style lang="less" scoped>
.login-container {
  width: 300px;
  background-color: #fff;
  border: 1px solid #eaeaea;
  border-radius: 15px;
  padding: 35px 35px 15px 35px;
  box-shadow: 0 0 25px #cacaca;
  margin: auto;

  h3 {
    text-align: center;
    margin-bottom: 20px;
    color: #545454;
  }
}

:deep(.el-form-item__content) {
  justify-content: center;
}

.box {
  height: 100%;
  padding-top: 180px;
  background-color: #eee;
}
</style>