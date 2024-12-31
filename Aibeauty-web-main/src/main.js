import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from './router'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import './assets/less/index.less'
import { createPinia } from 'pinia'
import { useTokenStore, useMenuStore } from '../src/assets/stores/index.js'
import api from './api/api.js'

const pinia = createPinia()
const app = createApp(App)

app.use(ElementPlus)
app.use(pinia)

// 在初始化pinia仓库后才能使用
const tokenStore = useTokenStore();
const menuStore = useMenuStore();
app.config.globalProperties.$api = api
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}


menuStore.setMenuRouter(router);

//全局的路由守卫
router.beforeEach((to, from, next) => {
    const token = tokenStore.$state.token;
    if (!token && to.name != 'login') {
        //没有登录token且去的不是login，重定向到login登录
        next({ name: 'login' })
    }else
    {
        next()
    }
})

app.use(router)
app.mount('#app')
