<template>
  <div class="container">
    <!-- <span class="management">/门店管理</span> -->
    <div class="form-container">
      <!-- 查询表格 -->
      <div class="search-form">
        <h6 class="bold-label">查询表格</h6>
        <el-form :inline="true" :model="formInline" class="search-form-inline">
          <el-form-item label="店铺编号" class="form-label">
            <el-input v-model="formInline.id" placeholder="请输出店铺编号" clearable />
          </el-form-item>
          <el-form-item label="店铺名称" class="form-label">
            <el-input v-model="formInline.storeName" placeholder="请输出店铺名称" clearable />
          </el-form-item>
          <el-form-item label="店长姓名" class="form-label">
            <el-input v-model="formInline.ownerName" placeholder="请输出店长姓名" clearable />
          </el-form-item>
          <el-form-item>
            <el-button @click="onReset" :icon="RefreshRight">重置</el-button>
          </el-form-item>
          <el-form-item label="联系电话" class="form-label">
            <el-input v-model="formInline.contact" placeholder="请输出店铺联系电话" clearable />
          </el-form-item>
          <el-form-item label="店铺区域" class="form-label">
            <el-select v-model="formInline.addressCity" placeholder="请选则店铺所在城市" clearable>
              <el-option label="北京" value="beijing" />
              <el-option label="深圳" value="shenzhen" />
            </el-select>
          </el-form-item>
          <el-form-item label="店铺状态" class="form-label">
            <el-select v-model="formInline.status" placeholder="请选择店铺营业状态" clearable>
              <el-option label="营业中" value="1" />
              <el-option label="停业" value="0" />
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
        <el-button type="primary" :icon="Plus" @click="go('/addStore')">新建</el-button>
        <el-button :icon="Upload" @click="exportXlsx">导出</el-button>
      </div>

      <div class="display-table" style="margin-top: 12px; text-align: left;">
        <el-table :data="tableData" style="width: 100%; margin: 0 auto;">
          <el-table-column fixed prop="id" label="序号" width="200">
            <template #default="scope">
              {{ (pageInfo.pageNum - 1) * pageInfo.pageSize + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column prop="storeName" label="门店名称" width="350" />
          <el-table-column prop="ownerName" label="店长" width="300" />
          <el-table-column prop="addressCity" label="城市" width="230" />
          <el-table-column prop="contact" label="联系电话" width="250" />
          <el-table-column label="状态" width="180" align="center">
            <template #default="scope">
              <!-- 使用条件语句根据 status 的值生成不同类型的按钮 -->
              <el-button v-if="scope.row.status === 0" type="success" size="small">营业中</el-button>
              <el-button v-else-if="scope.row.status === 1" type="info" size="small">休息中</el-button>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="150" align="center">
            <template #default="scope">
              <el-button link type="primary" size="small" @click="checkAll(scope.row)">编辑</el-button>
              <!-- 这里的删除为了展示效果，也为了避免频繁地调取实现接口（mock），在删除请求发送后
              前端将之前获取的数组数据进行删除，并没有再次从后端接口获取 -->
              <el-button link type="danger" size="small" @click="handleDelete(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!-- 用于更改表格信息的弹窗 -->
      <el-drawer v-model="drawer" :before-close="handleClose" direction="ltr">
        <template #header>
          <h2>门店信息编辑</h2>
        </template>
        <el-form :model="editInfo" label-width="auto" style="display: flex; max-width: 400px; flex-wrap: wrap;">
          <el-form-item label="门店编号">
            <span>{{ editInfo.id }}</span>
          </el-form-item>
          <el-form-item label="门店名称">
            <el-input v-model="editInfo.storeName" style="width: 300px;">
            </el-input>
          </el-form-item>
          <el-form-item label="店长">
            <el-input v-model="editInfo.ownerName" style="width: 300px;">
            </el-input>
          </el-form-item>
          <el-form-item label="城市">
            <el-input v-model="editInfo.addressCity" style="width: 300px;">
            </el-input>
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="editInfo.contact" style="width: 300px;">
            </el-input>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="editInfo.status" style="width: 300px;">
              <el-option label="营业中" :value="0" />
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

<script setup>
import { defineComponent, onMounted, reactive, getCurrentInstance, ref } from 'vue'
import { Search, RefreshRight, Plus, Upload } from '@element-plus/icons-vue'
import axios from 'axios'
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus'
import { useIdStore } from '../../assets/stores/index.js'
import * as XLSX from 'xlsx'

const router = useRouter();
const idStore = useIdStore();
const totalItems = ref(0); // 声明 totalItems 为响应式变量
let tableData = ref([]);
const { proxy } = getCurrentInstance();
const drawer = ref(false);
//查询接口的变量名和后端还是对不上的，之后要约定
const formInline = reactive({
  id: '',
  storeName: '',
  ownerName: '',
  contact: '',
  addressProvince: '',
  addressCity: '',
  addressDetail:'',
  status: '',
  ownerId: '',
  deleted: ''
})
const pageInfo = reactive({
  pageNum: 1,
  pageSize: 10,
})
const editInfo = reactive({
  id: '',
  storeName: '',
  ownerName: '',
  contact: '',
  addressProvince: '',
  addressCity: '',
  addressDetail:'',
  status: '',
  ownerId: '',
  deleted: ''
})
const getStoreList = async () => {
  let res = await proxy.$api.getStoreList(pageInfo);
  totalItems.value = res.total;
  console.log(res);
  tableData.value = res.rows;
}
const go = (path) => {
  router.push(path);
}

//编辑信息把表格信息拿到编辑框
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
      let res = proxy.$api.updateStore(editInfo);
      drawer.value = false
      ElMessage.success("修改成功！")
    })
    .catch(() => {
      // catch error
    })
}
//删除前二次确认
const handleDelete = (index) => {
  ElMessageBox.confirm(
    '确定删除该门店信息?',
    '警告',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
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
//切换分页
const changePage = (page) => {
  // console.log(page);
  pageInfo.pageNum = page;
  getStoreList(pageInfo);
}
//导出excel表格
const exportXlsx = () => {
  console.log("开始导出")
  // 创建工作表
  const data = XLSX.utils.json_to_sheet(tableData.value)
  // 创建工作簿
  const wb = XLSX.utils.book_new()
  // 将工作表放入工作簿中
  XLSX.utils.book_append_sheet(wb, data, 'data')
  // 生成文件并下载
  XLSX.writeFile(wb, '门店列表.xlsx')
}
onMounted(() => {
  getStoreList();
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
}

.display-table {
  margin-top: 12px;
  text-align: left;
}
</style>
