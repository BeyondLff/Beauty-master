import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
    {
        path: '/',
        name: 'home1',
        redirect: '/home',
        meta: {
            title: "首页",
        },
        component: () => import('../views/Main.vue'),
        children: [
            
        ]
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('../views/Login.vue')
    }

]

const router = createRouter({
    history: createWebHashHistory(),
    routes,
})

export default router