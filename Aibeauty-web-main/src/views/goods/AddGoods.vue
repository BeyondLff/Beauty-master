<template>
  <div class="container">
    <!-- 商品管理导航 -->
    <!-- <span class="management">/商品管理/新增商品</span> -->
    <div class="form-container">
      <!-- 表单 -->
      <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="120px" class="demo-ruleForm" status-icon>
        <!-- 商品基本信息 -->
        <div class="group-container">
          <div class="group-header">商品基本信息</div>
          <div class="group-content">
            <div class="form-row">
              <div class="form-item">
                <el-form-item label="商品编号" prop="id">
                  <el-input v-model="ruleForm.id" />
                </el-form-item>
              </div>
              <div class="form-item">
                <el-form-item label="商品名称" prop="name">
                  <el-input v-model="ruleForm.name" />
                </el-form-item>
              </div>
            </div>
            <div class="form-row">
              <div class="form-item">
                <el-form-item label="商品分类" prop="category">
                  <el-select v-model="ruleForm.category" placeholder="请选择">
                    <!-- 选项 -->
                    <el-option v-for="(item, index) in categories" :key="index" :label="item.name"
                               :value="item.name"></el-option>
                  </el-select>
                </el-form-item>
              </div>
              <div class="form-item">
                <!-- 品牌 -->
                <el-form-item label="品牌" prop="brand">
                  <el-select v-model="ruleForm.brand" placeholder="请选择">
                    <!-- 选项 -->
                    <el-option v-for="(item, index) in brands" :key="index" :label="item.brandName"
                               :value="item.brandName"></el-option>
                  </el-select>
                </el-form-item>
              </div>
            </div>
          </div>
        </div>
        <!-- 商品库存信息 -->
        <div class="group-container">
          <div class="group-header">商品库存信息</div>
          <div class="group-content">
            <div class="form-row">
              <div class="form-item">
                <el-form-item label="商品状态" prop="status">
                  <el-radio-group v-model="ruleForm.status">
                    <el-radio value="OnSale">在售</el-radio>
                    <el-radio value="OutOfStock">缺货</el-radio>
                    <el-radio value="Discontinued">停产</el-radio>
                  </el-radio-group>
                </el-form-item>
              </div>

              <div class="form-item">
                <!-- 商品库存量 -->
                <el-form-item label="商品库存量" prop="stock">
                  <el-input v-model.number="ruleForm.stock" />
                </el-form-item>
              </div>
            </div>
            <div class="form-row">
              <div class="form-item">
                <el-form-item label="储存条件" prop="storageCondition">
                  <el-select v-model="ruleForm.storageCondition" placeholder="请选择">
                    <!-- 选项 -->
                    <el-option v-for="(item, index) in storageConditions" :key="index" :label="item" :value="item"></el-option>
                  </el-select>
                </el-form-item>
              </div>
              <div class="form-item">
                <el-form-item label="保质期" prop="shelfLife">
                  <el-input v-model="ruleForm.shelfLife" />
                </el-form-item>
              </div>
            </div>
          </div>
        </div>
        <!-- 商品价格信息 -->
        <div class="group-container">
          <div class="group-header">商品价格信息</div>
          <div class="group-content">
            <div class="form-row">
              <div class="form-item">
                <el-form-item label="税收类型" prop="taxType">
                  <el-select v-model="ruleForm.taxType" placeholder="请选择">
                    <!-- 选项 -->
                    <el-option v-for="(item, index) in taxTypes" :key="index" :label="item" :value="item"></el-option>
                  </el-select>
                </el-form-item>
              </div>
              <div class="form-item">
                <el-form-item label="销项税率" prop="taxRate">
                  <el-input v-model="ruleForm.taxRate" />
                </el-form-item>
              </div>
            </div>
            <div class="form-row spec" v-for="(item, index) in ruleForm.spec" :key="index">
              <div class="form-item">
                <el-form-item label="规格" :prop="'spec[' + index + '].weight'" label-width="120px"
                              :rules="[{ required: true, message: '请输入规格', trigger: 'blur' }]">
                  <el-input v-model="item.weight" />
                </el-form-item>
              </div>
              <div class="form-item">
                <el-form-item label="成本价" :prop="'spec[' + index + '].costPrice'" label-width="100px"
                              :rules="[{ required: true, message: '请输入规格', trigger: 'blur' }, { validator: validatePrice, trigger: 'blur' }]">
                  <el-input v-model="item.costPrice" />
                </el-form-item>
              </div>
              <div class="form-item">
                <el-form-item label="目录价" :prop="'spec[' + index + '].listPrice'" label-width="100px"
                              :rules="[{ required: true, message: '请输入规格', trigger: 'blur' }, { validator: validatePrice, trigger: 'blur' }]">
                  <el-input v-model="item.listPrice" />
                </el-form-item>
              </div>
              <div class="form-item" style="text-align: end; max-width: 15%;">
                <el-button type="primary" v-show="index === 0" @click="addSpec">添加</el-button>
                <el-button type="default" v-show="index !== 0" @click="removeSpec(index)">删除</el-button>
              </div>
            </div>
          </div>
        </div>
        <!-- 商品描述信息 -->
        <div class="group-container">
          <div class="group-header">商品描述信息</div>
          <div class="group-content">
            <el-form-item label="商品介绍" prop="introduction">
              <el-input v-model="ruleForm.introduction" type="textarea" />
            </el-form-item>
            <el-form-item label="商品特点" prop="features">
              <el-input v-model="ruleForm.features" type="textarea" />
            </el-form-item>
          </div>
        </div>

      </el-form>
    </div>
    <!-- 提交按钮 -->
    <div class="form-footer">
      <el-form-item>
        <el-button type="primary" @click="submitForm(ruleFormRef)">
          创建
        </el-button>
        <!-- 重置按钮 -->
        <el-button @click="resetForm(ruleFormRef)">重置</el-button>
      </el-form-item>
    </div>
  </div>

</template>

<script setup lang="ts">
import { onMounted, proxyRefs, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { useRouter } from 'vue-router'
import api from '../../api/api'

const categories = ref([
  { name: '电子产品' },
  { name: '家具' },
  { name: '服装' },
  // 其他分类...
])

const brands = ref([
  { brandName: '品牌A' },
  { brandName: '品牌B' },
  { brandName: '品牌C' },
  // 其他品牌...
])

const storageConditions = ref(['常温', '冷藏', '冷冻'])

const taxTypes = ref(['增值税', '消费税', '营业税'])

const ruleFormRef = ref<FormInstance>()
// 表单数据接口
const ruleForm = reactive<{
  id: string,
  name: string,
  category: string,
  brand: string,
  status: string,
  stock: string,
  storageCondition: string,
  shelfLife: string,
  taxType: string,
  taxRate: string,
  spec: Spec[],
  introduction: string,
  features: string
}>({
  id: '',
  name: '',
  category: '',
  brand: '',
  status: 'OnSale',
  stock: '',
  storageCondition: '',
  shelfLife: '',
  taxType: '',
  taxRate: '',
  spec: [{
    weight: '',
    costPrice: '',
    listPrice: '',
  },
  ],
  introduction: '',
  features: ''
})

interface Spec {
  weight: string,
  costPrice: string,
  listPrice: string
}

const validateInt = (rule: any, value: any, callback: any) => {
  if (value < 0)
    return callback(new Error("不能为负数"))
  else
    return callback()
}

// 校验金额
const validatePrice = (rule: any, value: any, callback: any) => {
  let tmp = Number(value)
  if (tmp <= 0) {
    return callback(new Error("金额必须为正数"))
  } else if (isNaN(tmp)) {
    return callback(new Error("请输入数字"))
  } else {
    return callback()
  }
}

// 表单规则
const rules = reactive<FormRules>({
  id: [
    { required: true, message: '请输入商品编号', trigger: 'blur' },
  ],
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
  ],
  category: [
    { required: true, message: '请选择商品分类', trigger: 'change' },
  ],
  brand: [
    { required: true, message: '请选择品牌', trigger: 'change' },
  ],
  status: [
    { required: true, message: '请选择商品状态', trigger: 'change' },
  ],
  stock: [
    { required: true, message: "请输入库存量", trigger: 'blur' },
    { type: 'number', message: "库存量必须是整数" },
    { validator: validateInt, trigger: 'blur' },
  ],
  storageCondition: [
    { required: false, message: '请选择存储条件', trigger: 'change' },
  ],
  shelfLife: [
    { required: true, message: '请输入保质期', trigger: 'blur' },
  ],
  taxType: [
    { required: false, message: '请选择税收类型', trigger: 'change' },
  ],
  taxRate: [
    { required: false, message: '请选择销项税率', trigger: 'change' },
  ],
  introduction: [
    { required: true, message: '请输入商品介绍', trigger: 'blur' },
  ],
  features: [
    { required: true, message: '请输入商品特点', trigger: 'blur' },
  ],
})

// 提交表单
const submitForm = async (formE1: FormInstance | undefined) => {
  if (!formE1)
    return
  await formE1.validate((valid, fields) => {
    if (valid) {
      createGood()
      console.log("submit")
    } else {
    }
  })
}

// 重置表单
const resetForm = (formE1: FormInstance | undefined) => {
  if (!formE1)
    return
  formE1.resetFields()
}

// 添加规格表单项
const addSpec = () => {
  ruleForm.spec.push({
    weight: '',
    listPrice: '',
    costPrice: '',
  })
}

// 移除表单项
const removeSpec = (index: number) => {
  if (index != -1) {
    ruleForm.spec.splice(index, 1)
  }
}

const getCategories = async () => {
  let res: any = await api.getCategories()
  categories.value = res.categories
}

const getAllBrand = async () => {
  let res = await api.getAllBrand()
  brands.value = res
}

const createGood = async () => {
  await api.createGood(ruleForm)
}

onMounted(() => {
  getCategories()
  getAllBrand()
})
</script>

<style scoped>
.container {
  width: 100%;
  height: 100%;
  padding: 20px 0px;
}

.management {
  font-size: 12px;
  margin-left: 20px;
  margin-bottom: 10px;
  display: flex;
}

.form-container {
  padding: 10px 20px;
  overflow-y: auto;
  max-height: calc(100vh - 150px);
  /* 计算表单容器的最大高度 */
}

.group-container {
  margin-bottom: 20px;

  background-color: #ffffff;
  /* 设置每个分组的背景颜色为白色 */
  border: 1px solid #ebeef5;
  /* 添加边框样式 */
  border-radius: 5px;
  /* 添加圆角样式 */
}

.group-header {
  padding: 10px;
  font-size: 16px;
  font-weight: bold;
  border-bottom: 1px solid #ebeef5;
  /* 分组标题下边框 */
}

.group-content {
  padding: 20px 100px 0px 100px;
}

.form-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.spec {
  align-items: start;
}

.spec .form-item {
  max-width: 28%;
}

.form-item {
  flex: 0 0 calc(50% - 60px);
  /* 每行两列，设置每列宽度为50%并留出10px间距 */
}

.form-item:last-child {
  margin-left: 10px;
  /* 最后一个form-item右边距10px */
}

.form-footer {
  position: absolute;
  padding: 15px 25px;
  height: 60px;
  bottom: 0px;
  background-color: #ffffff;
  /* 设置每个分组的背景颜色为白色 */
  width: 88%;
  display: flex;
  justify-content: flex-end;
}

.button-group .el-form-item {
  margin-bottom: 0;
  /* 去除按钮组的底部 margin */
}
</style>
