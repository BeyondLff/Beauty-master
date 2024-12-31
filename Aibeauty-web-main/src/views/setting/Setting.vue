<template>
  <div class="container">
    <div class="form-container">
      <div class="user-info">
        <!-- on-success：上传成功后的回调函数
        before-upload：上传前的钩子函数 -->
        <el-upload class="avatar-uploader" 
          action="http://127.0.0.1:4523/m1/4177433-0-default/users/updateUser" 
          :show-file-list="false" 
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
          <el-tooltip content="点击更换头像" placement="top">
            <el-avatar :size="200" :src="userInfo.avatar" />
          </el-tooltip>
          
        </el-upload>
        <div style="margin-top: 20px;">用户名:{{ userInfo.userName }}</div>
        <div style="margin-top: 20px;">当前权限：
          <el-button v-if="userInfo.roleId == 1" type="success" plain>超级管理员</el-button>
                <el-button v-else-if="userInfo.roleId == 2" type="success" plain>管理员</el-button>
                <el-button v-else-if="userInfo.roleId == 3" type="success" plain>店长/经理</el-button>
                <el-button v-else-if="userInfo.roleId == 4" type="success" plain>普通员工</el-button>
        </div>
      </div>
      <el-form :model="updateForm" class="update-container">
        <el-form-item label="原始密码：">
          <el-input type="input" placeholder="请输入原始密码" v-model="updateForm.oldPassword">
          </el-input>
        </el-form-item>
        <el-form-item label="新的密码：">
          <el-input type="password" placeholder="请输入新密码" v-model="updateForm.newPassword">
          </el-input>
        </el-form-item>
        <el-form-item label="确认密码：">
          <el-input type="password" placeholder="重新输入新密码" v-model="updateForm.checkPassword">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="">
            提交修改
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, getCurrentInstance } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { useIdStore } from '../../assets/stores/index.js';
import api from '../../api/api.js';

const idStore = useIdStore();
const { proxy } = getCurrentInstance();

const userInfo = reactive({
  userName: '',
  account: '',
  roleId: null,
  avatar: idStore.$state.avatar,
});

const updateForm = reactive({
  oldPassword: '',
  newPassword: '',
  checkPassword: '',
});


const getAvatar = async () => {
  if (userInfo.avatar == null || userInfo.avatar == "") {
    const res = await proxy.$api.getAvatar();
    console.log(res);
    userInfo.avatar = res.avatar;
    idStore.setAvatar(res.avatar);
  }
};
const getInfo = async () => {
  const res = await proxy.$api.getInfo();
  console.log(userInfo);
  userInfo.userName = res.userName;
  userInfo.roleId = res.roleId;
}

const handleAvatarSuccess = (response, uploadFile) => {
  if (uploadFile && uploadFile.raw) {
    const avatar = URL.createObjectURL(uploadFile.raw);
    userInfo.avatar = avatar;
    idStore.setAvatar(avatar);
  }
};

const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg') {
    ElMessage.error('上传图片必须为jpg格式');
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('图片大小不得超过2MB!');
    return false;
  }
  return true;
};

onMounted(() => {
  getAvatar();
  getInfo();
});
</script>


<style lang="less" scoped>
.container {
  width: 100%;
  height: 100%;
  padding: 20px 20px;
}

.form-container {
  margin-top: 10px;
  height: 92%;
  padding: 20px 20px;
  background-color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-info {
  border-right: 1px solid #ccc;
  padding-right: 40px;
}

.update-container {
  margin-left: 40px;
}

:deep(.el-form-item__content) {
  justify-content: center;
}
</style>