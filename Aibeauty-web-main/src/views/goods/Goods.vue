<template>
  <div class="container">
    <!-- <span class="management">/商品管理</span> -->
    <div class="form-container">
      <!-- 查询表格 -->
      <div class="search-form">
        <h6 class="bold-label">查询表格</h6>
        <el-form :inline="true" :model="formInline" class="search-form-inline">
          <el-form-item label="商品编号" class="form-label">
            <el-input v-model="formInline.id" placeholder="请输出商品编号" clearable />
          </el-form-item>
          <el-form-item label="商品名称" class="form-label">
            <el-input v-model="formInline.name" placeholder="请输出商品名称" clearable />
          </el-form-item>
          <el-form-item label="品牌名称" class="form-label">
            <el-input v-model="formInline.user" placeholder="请输出商品品牌名称" clearable />
          </el-form-item>
          <el-form-item>
            <el-button @click="onReset" :icon="RefreshRight">重置</el-button>
          </el-form-item>
          <el-form-item label="上架时间" class="form-label">
            <el-date-picker v-model="value2" type="date" placeholder="请选择上架时间" :disabled-date="disabledDate"
              :shortcuts="shortcuts" />
          </el-form-item>

          <el-form-item label="商品品类" class="form-label">
            <el-select v-model="formInline.category" placeholder="请选择商品品类" clearable>
              <el-option label="水乳" value="1" />
              <el-option label="洁面" value="0" />
            </el-select>
          </el-form-item>
          <el-form-item label="商品状态" class="form-label">
            <el-select v-model="formInline.state" placeholder="请选择商品状态" clearable>
              <el-option label="已上架" value="1" />
              <el-option label="下架" value="0" />
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
        <el-button type="primary" :icon="Plus" @click="go('/addGoods')">新建</el-button>
        <el-button :icon="Upload">导入</el-button>
      </div>
      <div class="display-table" style="margin-top: 12px; text-align: left;">
        <el-table :data="tableData" style="width: 100%; margin: 0 auto;">
          <el-table-column fixed prop="id" label="序号">
            <template #default="scope">
              {{ (pageInfo.pageNum - 1) * pageInfo.pageSize + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column prop="image" label="商品图片" align="center">
            <template #default="scope">
              <el-image style="width: 100px; height: 100px" :src="scope.row.image" :zoom-rate="1.2" :max-scale="7"
                :min-scale="0.2" fit="cover" />
            </template>
          </el-table-column>
          <el-table-column prop="account" label="商品编号" />
          <el-table-column prop="name" label="商品名称" />
          <el-table-column prop="brandname" label="品牌" />
          <el-table-column prop="price" label="价格" />
          <el-table-column prop="stock" label="库存" />
          <el-table-column prop="status" label="状态" align="center">
            <template #default="scope">
              <!-- 使用条件语句根据 status 的值生成不同类型的按钮 -->
              <el-button v-if="scope.row.status === 0" type="success" size="small">在售</el-button>
              <el-button v-else-if="scope.row.status === 1" type="info" size="small">下架</el-button>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="150" align="center">
            <!-- 上架、下架、删除商品 -->
            <template #default="scope">
              <el-button v-if="scope.row.status === 1" link type="primary" size="small"
                @click="handleEdit(scope.row)">上架</el-button>
              <el-button v-else-if="scope.row.status === 0" link type="info" size="small"
                @click="handleEdit(scope.row)">下架</el-button>
              <el-button link type="danger" size="small" @click="confirmDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页查询 -->
        <el-pagination layout="prev, pager, next" :total="totalItems" @change="changePage" />
      </div>
      <!-- 记录展示表格结束 -->
    </div>
  </div>
</template>

<script>
import { defineComponent, onMounted, reactive, ref, getCurrentInstance } from 'vue';
import { Search, RefreshRight, Plus, Upload } from '@element-plus/icons-vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useIdStore } from '../../assets/stores/index.js';

export default defineComponent({
  setup() {
    // 路由和全局状态
    // const router = useRouter();
    const idStore = useIdStore();

    // 声明响应式变量
    const totalItems = ref(0);
    const formInline = reactive({
      id: '',
      name: '',
      user: '',
      time: '',
      category: '',
      state: ''
    });
    const pageInfo = reactive({
      id: idStore.$state.id,
      pageNum: 1,
      pageSize: 10,
    });
    const goodInfo = reactive({
      id: -1,
      operate: -1,
    });

    const tableData = ref([]);
    const value2 = ref('');
    const router = useRouter();

    // 日期快捷方式
    const shortcuts = [
      { text: '今天', value: new Date() },
      { text: '昨天', value: () => new Date(Date.now() - 3600 * 1000 * 24) },
      { text: '一周前', value: () => new Date(Date.now() - 3600 * 1000 * 24 * 7) }
    ];

    // 禁用未来日期
    const disabledDate = (time) => {
      return time.getTime() > Date.now();
    };

    const { proxy } = getCurrentInstance();

    // 获取商品列表
    const getGoodsList = async () => {
      try {
        let res = await proxy.$api.getGoodsList(pageInfo);
        totalItems.value = res.total;
        tableData.value = res.rows;
        console.log(res);
      } catch (error) {
        console.error('获取商品列表时出错:', error);
      }
    };

    // 导航功能
    const go = (path) => {
      router.push(path);
    };

    // 在组件挂载时获取商品列表
    onMounted(() => {
      getGoodsList();
    });

    // 处理分页变化
    const changePage = (page) => {
      console.log(page);
      pageInfo.pageNum = page;
      getGoodsList(pageInfo);
    };

    //上架、下架商品
    const handleEdit = async (data) => {
      if (data.status == 0) {
        data.status = 1;
        goodInfo.id = data.id;
        goodInfo.operate = 1;//下架
      } else {
        data.status = 0;
        goodInfo.id = data.id;
        goodInfo.operate = 0;//上架
      }
      let res = await proxy.$api.editGood(goodInfo);
      // console.log(res);
      // console.log("操作成功")
    }

    //删除商品
    const confirmDelete = (data) => {
      ElMessageBox.confirm(
        '删除会同时下架，确定吗？',
        '警告',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
        }
      )
        .then(() => {
          // 从 tableData 中移除已删除的商品
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

    const handleDelete = async (data) => {
      let res = await proxy.$api.deleteGood(data);
    }

    // 将变量和函数暴露给模板
    return {
      tableData,
      formInline,
      value2,
      Search,
      RefreshRight,
      Plus,
      Upload,
      go,
      shortcuts,
      disabledDate,
      totalItems,
      pageInfo,
      goodInfo,
      changePage,
      handleEdit,
      handleDelete,
      confirmDelete
    };
  },
});
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
  overflow-y: auto;
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

.search-form-inline .el-date-picker {
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

.example-pagination-block {}
</style>
