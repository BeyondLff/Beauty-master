<template>
  <div class="container">
    <!-- <span class="management">/员工管理</span> -->
    <div class="form-container">
      <!-- 查询表格 -->
      <div class="search-form">
        <h6 class="bold-label">查询表格</h6>
        <el-form :inline="true" :model="formInline" class="search-form-inline">
          <el-form-item label="员工编号" class="form-label">
            <el-input v-model="formInline.account" placeholder="请输入员工编号" clearable />
          </el-form-item>
          <el-form-item label="员工姓名" class="form-label">
            <el-input v-model="formInline.username" placeholder="请输入员工姓名" clearable />
          </el-form-item>
          <el-form-item label="联系电话" class="form-label">
            <el-input v-model="formInline.phoneNumber" placeholder="请输入联系方式" clearable />
          </el-form-item>
          <el-form-item>
            <el-button @click="onReset" :icon="RefreshRight">重置</el-button>
          </el-form-item>
          <el-form-item label="店铺名称" class="form-label">
            <el-input v-model="formInline.storename" placeholder="请输入店铺名称" clearable />
          </el-form-item>
          <el-form-item label="员工岗位" class="form-label">
            <el-select v-model="formInline.role" placeholder="请选择所在岗位" clearable>
              <el-option label="店长" value="3" />
              <el-option label="普通员工" value="4" />
            </el-select>
          </el-form-item>
          <el-form-item label="员工状态" class="form-label">
            <el-select v-model="formInline.status" placeholder="请选择员工状态" clearable>
              <el-option label="工作中" value="0" />
              <el-option label="休息中" value="1" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit" :icon="Search">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!-- 查询表格结束 -->
      <el-divider style="width: 100%; margin: 0 auto;" />
      <div class="button">
        <el-button type="primary" :icon="Plus" @click="go('/addEmployee')">新建</el-button>
        <el-button :icon="Upload">导入</el-button>
      </div>
      <!-- 记录展示表格 -->
      <div class="display-table" style="margin-top: 12px; text-align: left;">
        <el-table :data="tableData" style="width: 100%; margin: 0 auto;">
          <el-table-column fixed prop="id" label="序号">
            <template #default="scope">
              {{ (pageInfo.pageNum - 1) * pageInfo.pageSize + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column prop="account" label="员工帐号" />
          <el-table-column prop="username" label="姓名" />
          <el-table-column prop="phoneNumber" label="联系电话" />
          <el-table-column prop="name" label="店铺名称" width="250" />
          <el-table-column prop="role" label="岗位" width="180" />
          <el-table-column label="状态" width="100" align="center">
            <template #default="scope">
              <!-- 使用条件语句根据 status 的值生成不同类型的按钮 -->
              <el-button v-if="scope.row.status === 0" type="success" size="small">工作中</el-button>
              <el-button v-else-if="scope.row.status === 1" type="info" size="small">休息中</el-button>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="150" align="center">
            <template #default="scope">
              <el-button link type="primary" size="small" @click="checkAll(scope.row)">编辑</el-button>
              <el-button link type="danger" size="small" @click="confirmDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!-- 用于更改表格信息的弹窗 -->
      <el-drawer v-model="drawer" :before-close="handleClose" direction="ltr">
        <template #header>
          <h2>员工信息编辑</h2>
        </template>
        <el-form :model="editInfo" label-width="auto" style="display: flex; max-width: 400px; flex-wrap: wrap;">
          <el-form-item label="员工帐号">
            <span>{{ editInfo.account }}</span>
          </el-form-item>
          <el-form-item label="员工姓名">
            <el-input v-model="editInfo.username" style="width: 300px;">
            </el-input>
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="editInfo.phoneNumber" style="width: 300px;">
            </el-input>
          </el-form-item>
          <el-form-item label="所在门店">
            <el-input v-model="editInfo.name" style="width: 300px;">
            </el-input>
          </el-form-item>
          <el-form-item label="岗位">
            <el-input v-model="editInfo.role" style="width: 300px;">
            </el-input>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="editInfo.status" style="width: 300px;">
              <el-option label="工作中" :value="0" />
              <el-option label="休息中" :value="1" />
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
          <div style="flex: auto">
            <el-button @click="cancelClick()">取消</el-button>
            <el-button type="primary" @click="confirmClick()">确认</el-button>
          </div>
        </template>
      </el-drawer>
      <br>
      <div class="example-pagination-block">
        <el-pagination layout="prev, pager, next" :total="totalItems" @change="changePage" />
      </div>
    </div>

  </div>
</template>

<script>
import { defineComponent, onMounted, reactive, getCurrentInstance, ref } from 'vue'
import { Search, RefreshRight, Plus, Upload } from '@element-plus/icons-vue'
import { useTokenStore, useIdStore } from '../../assets/stores/index.js'
import axios from 'axios'
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus'

export default defineComponent({
  setup() {
    const formInline = reactive({
      account: '',
      ownerName: '',
      phoneNumber: '',
      role: '',
      storename: '',
      status: ''
    })
    let tableData = ref([]);
    const router = useRouter();
    const tokenStore = useTokenStore();
    const idStore = useIdStore();
    const { proxy } = getCurrentInstance();
    const totalItems = ref(0); // 声明 totalItems 为响应式变量
    const drawer = ref(false);
    //分页查询表单
    const pageInfo = reactive({
      id: idStore.$state.id,
      pageNum: 1,
      pageSize: 10,
    })
    //编辑信息的表单
    const editInfo = reactive({
      id: 0,
      account: '',
      username: '',
      ownerName: '',
      role: '',
      phoneNumber: '',
      status: '',
    })
    // 条件查询员工信息
    const onSubmit = async () => {
      let res = await proxy.$api.getUserInfo(formInline)
      totalItems.value = res.total; // 将总数赋值给 totalItems
      console.log(res);
      tableData.value = res.rows;
    }
    // 重置查询条件
    const onReset = () => {
      formInline.account = ''
      formInline.username = ''
      formInline.phoneNumber = ''
      formInline.role = ''
      formInline.storename = ''
      formInline.status = ''
    }


    //获取所有员工信息
    const getAllInfo = async () => {
      //console.log(pageInfo.id);
      let res = await proxy.$api.getAllInfo(pageInfo);
      totalItems.value = res.total; // 将总数赋值给 totalItems
      console.log(res);
      tableData.value = res.rows;
    }
    //路由跳转
    const go = (path) => {
      router.push(path)
    }

    //将表格中某行的数据写入要修改的表单
    const checkAll = (data) => {
      drawer.value = true;
      Object.assign(editInfo, data);
      console.log(editInfo);
    }
    // 点击空白区域弹出警告
    const handleClose = (done) => {
      ElMessageBox.confirm(
        '不保存就离开吗？',
        '警告', {
        confirmButtonText: '离开',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          ElMessage("取消修改");
          done()
        })
        .catch(() => {
          // catch error
        })
    }
    // 点击取消直接退出
    function cancelClick() {
      drawer.value = false,
        ElMessage("取消修改");
    }
    // 点击确认弹出二次确认
    function confirmClick() {
      ElMessageBox.confirm(
        '确认提交修改吗？',
        '确认', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'success'
      })
        .then(() => {
          // 提交
          let res = proxy.$api.updateUser(editInfo);
          drawer.value = false
          ElMessage.success("修改成功！")
        })
        .catch(() => {
          // catch error
        })
    }

    const handleDelete = async (data) => {
      let res = await proxy.$api.deleteEmployee(data);
    }

    const confirmDelete = (data) => {
      ElMessageBox.confirm(
        '确定删除该员工信息?',
        '警告',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
        }
      )
        .then(() => {
          // 从 tableData 中移除已删除的员工
          tableData.value = tableData.value.filter(item => item.id !== data.id);
          handleDelete(data.id)
          ElMessage({
            type: 'success',
            message: '删除成功',
          })
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: '删除取消',
          })
        })
    }
    onMounted(() => {
      // getEmployeeList();
      getAllInfo();
    })
    const changePage = (page) => {
      console.log(page);
      pageInfo.pageNum = page;
      getAllInfo(pageInfo);
    }
    return {
      tableData,
      totalItems,
      formInline,
      Search,
      RefreshRight,
      Plus,
      Upload,
      go,
      handleDelete,
      confirmDelete,
      handleClose,
      pageInfo,
      changePage,
      drawer,
      checkAll,
      editInfo,
      cancelClick,
      confirmClick,
      onSubmit,
      onReset,
    }
  },
})
</script>

<style scoped>
.container {
  width: 100%;
  height: 100%;
  padding: 20px 20px;
}

.management {
  font-size: 12px;
  display: flex;
}

.form-container {
  margin-top: 10px;
  height: 92%;
  padding: 20px 20px;
  background-color: #ffffff;
}

.bold-label {
  font-size: 15px;
  font-weight: bold;
  text-align: left;
  display: flex;
  margin-bottom: 10px;
}

.form-label {
  font-size: 14px;
  font-weight: bold;
  width: 27%;
}

.search-form-inline .el-input {
  --el-input-width: 230px;
  --el-input-height: 32px;
}

.search-form-inline .el-select {
  --el-select-width: 230px;
  --el-select-height: 32px;

}

.button {
  margin-top: 12px;
  display: flex;
  width: 80px;
  height: 30px;
}

.display-table {
  margin-top: 12px;
  text-align: left;
}
</style>
