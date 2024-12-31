<template>
  <div class="container">
    <!-- <span class="management">/客户管理</span> -->
    <div class="form-container">
      <!-- 查询表格 -->
      <div class="search-form">
        <h6 class="bold-label">查询表格</h6>
        <el-form :inline="true" :model="searchForm" class="search-form-inline">
          <el-form-item label="客户编号" class="form-label">
            <el-input v-model="searchForm.id" placeholder="请输入客户编号" clearable />
          </el-form-item>
          <el-form-item label="客户姓名" class="form-label">
            <el-input v-model="searchForm.name" placeholder="请输入客户姓名" clearable />
          </el-form-item>
          <el-form-item label="联系电话" class="form-label">
            <el-input v-model="searchForm.phoneNumber" placeholder="请输入联系方式" clearable />
          </el-form-item>
          <el-form-item>
            <el-button @click="onReset" :icon="RefreshRight">重置</el-button>
          </el-form-item>
          <el-form-item label="邮箱号码" class="form-label">
            <el-input v-model="searchForm.email" placeholder="请输入邮箱号码" clearable />
          </el-form-item>
          <el-form-item label="所在地址" class="form-label">
            <el-select v-model="searchForm.address" placeholder="请选择所在地址" clearable>
              <el-option label="地区1" value="shanghai" />
              <el-option label="地区2" value="beijing" />
            </el-select>
          </el-form-item>
          <el-form-item label="员工状态" class="form-label">
            <el-select v-model="searchForm.status" placeholder="请选择员工状态" clearable>
              <el-option label="正常" value="1" />
              <el-option label="异常" value="0" />
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
        <el-button type="primary" :icon="Plus" @click="router.push('/addGuest')">新建</el-button>
        <el-button :icon="Upload">导入</el-button>
      </div>
      <!-- 记录展示表格 -->
      <div class="display-table" style="margin-top: 12px;">
        <el-table :data="tableData" style="width: 100%; margin: 0 auto;">
          <el-table-column fixed prop="id" label="序号" width="100px">
            <template #default="scope">
              {{ (pageInfo.pageNum - 1) * pageInfo.pageSize + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column prop="name" label="姓名" width="150px"/>
          <el-table-column prop="phoneNumber" label="联系电话" width="150px"/>
          <el-table-column prop="email" label="邮箱" />
          <el-table-column prop="address" label="地址" width="200px"/>
          <el-table-column prop="status" label="状态" >
            <template #default="{ row }">{{ row.status == 1 ? "" : "异常" }}</template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="150px" align="center">
            <template #default="scope">
              <el-button link type="primary" size="small" @click="handleEdit(scope.row)">编辑
              </el-button>
              <el-button link type="danger" size="small" @click="handleDelete(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页查询 -->
        <el-pagination layout="prev, pager, next" :total="totalItems" @change="changePage" />
      </div>
      <el-drawer v-model="drawer" :before-close="handleClose" direction="ltr">
        <template #header>
          <h2>客户信息编辑</h2>
        </template>
        <template #default>
          <div>待完成的表单</div>
        </template>
        <template #footer>
          <div style="flex: auto">
            <el-button @click="cancelClick()">取消</el-button>
            <el-button type="primary" @click="confirmClick()">确认</el-button>
          </div>
        </template>
        <!-- <div>
                    <el-form :model="editForm">
                        <el-form-item label="门店名称" prop="name">
                            <el-input v-model="editForm.name" />
                        </el-form-item>
                        <el-form-item label="门店联系方式" prop="landline">
                            <el-input v-model="editForm.landline" />
                        </el-form-item>
                        <el-form-item label="省份/城市" prop="addressCity">
                            <el-input v-model="editForm.address_province" style="width: 150px;" />
                            <el-input v-model="editForm.addressCity" style="width: 150px;" />
                        </el-form-item>
                        <el-form-item label="详细地址" prop="addressDistrict">
                            <el-input v-model="editForm.addressDistrict" type="textarea" style="width: 300px;" />
                        </el-form-item>
                        <el-form-item label="店长编号" prop="account">
                            <el-input v-model="editForm.account" />
                        </el-form-item>
                    </el-form>
                    <div class="demo-drawer__footer">
                        <el-button @click="cancelForm">取消</el-button>
                        <el-button type="primary" :loading="loading" @click="onClick">
                            {{ loading ? '提交中...' : '提交' }}
                        </el-button>
                    </div>
                </div> -->
      </el-drawer>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { Search, RefreshRight, Plus, Upload } from '@element-plus/icons-vue'
import { useIdStore } from '../../assets/stores/index.js'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import api from '../../api/api.js'

// const tokenStore = useTokenStore()
const idStore = useIdStore()
const router = useRouter()

const searchForm = reactive({
  id: '',
  name: '',
  phoneNumber: '',
  email: '',
  address: '',
  status: ''
})

const editForm = reactive({
  name: '',
  phoneNumber: '',
  email: '',
  address: '',
  status: ''
})

let tableData = ref();

const totalItems = ref(0);  // 总条数
const pageInfo = reactive({
  id: idStore.$state.id,
  pageNum: 1,
  pageSize: 10,
})

const getAllInfo = async () => {
  var res = await api.getCustomersList(pageInfo);
  totalItems.value = res.total; // 将总数赋值给 totalItems
  console.log(res);
  tableData.value = res.rows;
}

const onClick = () => {

  // if (drawerRef.value !== null) {
  //     drawerRef.value.close();
  // }
}

// 列表右侧编辑按钮的点击事件
const drawer = ref(false)
// 点击编辑按钮弹出侧栏
const handleEdit = (row) => {
  console.log(row)
  drawer.value = true
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
      done()
    })
    .catch(() => {
      // catch error
    })
}
// 点击取消直接退出
function cancelClick() {
  drawer.value = false
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
      drawer.value = false
    })
    .catch(() => {
      // catch error
    })
}

// 列表右侧删除按钮的点击事件
const handleDelete = (index) => {
  ElMessageBox.confirm(
    '确定删除该客户信息?',
    '警告',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      // 删除这条记录
      // console.log(row)
      tableData.value.splice(index, 1);
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
  getAllInfo();
})
const changePage = (page) => {
  console.log(page)
  pageInfo.pageNum = page
  getAllInfo()
}
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
