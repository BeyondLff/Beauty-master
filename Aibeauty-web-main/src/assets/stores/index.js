import { defineStore } from 'pinia'
import cookie from 'js-cookie'
import router from '../../router/index.js';


// 折叠左侧菜单
export const useAsideStore = defineStore(
    'aside', {
        state: () => ({ isCollapse: true }),
        actions: {
            updateIsCollapse () {
                this.isCollapse = !this.isCollapse;
            }
        },
    }
)

export const useTokenStore = defineStore(
    'token', {
        state: () => ({ token: '' }),
        actions: {
            setToken (newToken) {
                this.token = newToken;
                cookie.set('token',newToken);
            },
            cleanToken () {
                this.token = '';
                cookie.remove('token');
            },
            getToken () {
                this.token = this.token || cookie.get('token');
            }
        },
    }
)

export const useIdStore = defineStore(
    'id', {
        state: () => ({
            id: 0,
            avatar: '',
            role: '',
            account: 0,
        }),
        actions: {
            setId (newId) {
                this.id = newId;
            },
            cleanId () {
                this.id = 0;
            },
            getId () {
                return this.id;
            },
            setAvatar (avatar) {
                this.avatar = avatar;
            },
            setAccount (account) {
                this.account = account;
            },
            setRole (role) {
                this.role = role;
            },
        },
    }
)

//存路由信息
export const useMenuStore = defineStore(
    'menu', {
    state: () => ({
        menuList: [],
        menuRouter: [],
        currentMenu: null,
    }),
    actions: {
        setMenuList(data) {
            this.menuList = data;
            // console.log(this.menuList);
        },
        //对应组件添加到路由中
        setMenuRouter() {
            this.menuList.forEach(item => {
                if (item.children) {
                    item.children.forEach(child => {
                        let routerInfo = {
                            path: child.path,
                            name: child.name,
                            label: child.label,
                            component: () => import(`../../views/${child.url}`),
                        }
                        this.menuRouter.push(routerInfo);
                    })
                } else {
                    let routerInfo = {
                        path: item.path,
                        name: item.name,
                        label: item.label,
                        component: () => import(`../../views/${item.url}`),
                    }
                    this.menuRouter.push(routerInfo);
                } 
            })
            this.menuRouter.forEach(item => {
                router.addRoute('home1', item);
            })
        },
        //为了安全，在退出登录时清空菜单、路由（还有token、id）
        cleanMenu() {
            this.menuList = [];
            this.menuRouter = [];
        },
        selectMenu(val) {
            val.name == 'home' ? (this.currentMenu = null) : (this.currentMenu = val);
            console.log("仓库的菜单是",this.currentMenu);
        },
    }
}
)