<template>
    <el-header>
        <img src="../assets/images/logo.png"
            style="position: absolute; left: 16px; top: 10px; width: 36px; height: 32px;">
        <span style="position: absolute; left: 58px; top: 16px; font-size: 18px; font-weight: bold;">AI Beauty</span>

        <div class="user">
            <span>用户名:{{ userInfo.account }}</span>
            <span style="margin: 20px 20px;">当前权限：
                <el-button v-if="userInfo.roleId == 1" type="success" plain>超级管理员</el-button>
                <el-button v-else-if="userInfo.roleId == 2" type="success" plain>管理员</el-button>
                <el-button v-else-if="userInfo.roleId == 3" type="success" plain>店长/经理</el-button>
                <el-button v-else-if="userInfo.roleId == 4" type="success" plain>普通员工</el-button>
            </span>
            <el-dropdown>
                <el-avatar :src="userInfo.avatar" />
                <template #dropdown>
                    <el-dropdown-menu>
                        <el-dropdown-item @click="go('/setting')">个人中心</el-dropdown-item>
                        <el-dropdown-item @click="dialogVisible = true">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </template>
            </el-dropdown>
        </div>

        <!-- <el-button type="danger" @click="dialogVisible = true"
            style="position: absolute; right: 20px; top: 10px; width: 80px; height: 32px;">退出登录</el-button> -->
        <el-dialog v-model="dialogVisible" width="300px" lock-scroll=false>
            <span>您确定要取消登录吗？</span>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="dialogVisible = false">保持登录</el-button>
                    <el-button type="primary" @click="signOut">确定退出</el-button>
                </div>
            </template>
        </el-dialog>
    </el-header>
</template>

<script>
import { defineComponent, ref, reactive, getCurrentInstance, onMounted } from 'vue'
import { useAsideStore, useTokenStore, useMenuStore, useIdStore } from '../assets/stores/index.js'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router';

export default defineComponent({
    setup() {
        const router = useRouter();
        const asideStore = useAsideStore();
        const tokenStore = useTokenStore();
        const idStore = useIdStore();
        const menuStore = useMenuStore();
        const dialogVisible = ref(false);
        const { proxy } = getCurrentInstance();
        // console.log(asideStore);

        const go = (path) => {
            router.push(path)
        }
        const userInfo = reactive({
            account: '',
            roleId: '',
            avatar: '',
        });
        const userId = {
            id: idStore.$state.id,
        }
        const getAvatar = async () => {
            if (userInfo.avatar == null || userInfo.avatar == "") {
                const res = await proxy.$api.getAvatar();
                console.log(res);
                userInfo.avatar = res.avatar;
                idStore.setAvatar(res.avatar);
            }

        }
        const getInfo = async () => {
            const res = await proxy.$api.getInfo();
            console.log(res);
            userInfo.account = res.account;
            userInfo.roleId = res.roleId;
            idStore.setAccount(res.account);
            idStore.setRole(res.roleId);
        }

        //确定退出要清楚菜单、token、id
        const signOut = () => {
            dialogVisible.value = false;
            tokenStore.cleanToken();
            idStore.cleanId();
            menuStore.cleanMenu();
            router.push({
                name: 'login',
            });
        }
        onMounted(() => {
            getAvatar();
            getInfo();
        })
        return {
            asideStore,
            signOut,
            dialogVisible,
            userInfo,
            go,
        }
    },
})
</script>

<style lang="less" scoped>

.el-header {
    width: 100%;
    background-color: #fff;
    height: 50px;
    position: relative;
    /* 添加 position: relative; 以便子元素使用绝对定位 */

    display: flex;
    align-items: center;
    /* 将子元素垂直居中 */
}

.dialog-footer {
    display: flex;
    justify-content: center;
    /* 水平居中 */
    align-items: center;
    /* 垂直居中 */
}

.user {
    margin-left: auto;
    /* 使用自动外边距将 .user 容器推到父元素的右侧 */
    margin-right: 20px;
    display: flex;
    align-items: center;
    /* 将子元素垂直居中 */
}
</style>
