<!-- 安装地区选择器 -->
<!-- npm install element-china-area-data -S -->
<template>
    <div class="container">
        <div class="form-container">
            <div class="form">
                <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="auto"
                    style=" margin: 0 auto; max-width: fit-content;" status-icon :inline="true">
                    <div class="formTitle">店铺信息</div>
                    <el-form-item label="省份" prop="provinceCode">
                        <el-select v-model="ruleForm.provinceCode" placeholder="请选择省份" @change="handleProvinceSelect"
                            style="width: 200px;">
                            <el-option v-for="item in regionData" :label="item.label" :value="item.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="城市" prop="cityCode">
                        <el-select v-model="ruleForm.cityCode" placeholder="请选择城市"
                            :disabled="!ruleForm.provinceCode || cityList.length == 0" @change="handleCitySelect"
                            style="width: 200px;">
                            <el-option v-for="item in cityList" :label="item.label" :value="item.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="详细地址" prop="addressDetail">
                        <el-input v-model="ruleForm.addressDetail" type="textarea" style="width: 500px;"
                            :autosize="{ minRows: 1, maxRows: 4 }" />
                    </el-form-item>
                    <br>
                    <el-form-item label="店名" prop="storeName">
                        <el-input v-model="ruleForm.storeName" style="width: 200px;" />
                    </el-form-item>
                    <el-form-item label="联系方式" prop="storeContact">
                        <el-input v-model="ruleForm.storeContact" style="width: 200px;" />
                    </el-form-item>
                    <el-divider />
                    <div class="formTitle">
                        店长信息
                        <el-button link type="primary" @click="openDialog">{{ is_choose ? "重新选择" : "已有账户，选择一个"
                            }}</el-button>
                        <el-button link type="danger" @click="resetChoose" v-show="is_choose">取消选择</el-button>
                    </div>
                    <!-- 姓名 性别 联系电话 身份证号码 家庭住址 -->
                    <el-form-item label="姓名" prop="username">
                        <el-input v-model="ruleForm.username" style="width: 200px;" :disabled="is_choose" />
                    </el-form-item>
                    <el-form-item label="性别" prop="gender">
                        <el-select v-model="ruleForm.gender" placeholder="" style="width: 80px;" :disabled="is_choose">
                            <el-option label="男" value="男" />
                            <el-option label="女" value="女" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="电话" prop="phoneNumber">
                        <el-input v-model="ruleForm.phoneNumber" style="width: 160px;" :disabled="is_choose" />
                    </el-form-item>
                    <br>
                    <el-form-item label="身份证" prop="identity">
                        <el-input v-model="ruleForm.identity" style="width: 200px;" :disabled="is_choose" />
                    </el-form-item>
                    <el-form-item label="住址" prop="address">
                        <el-input v-model="ruleForm.address" type="textarea" style="width: 350px;"
                            :autosize="{ minRows: 1, maxRows: 3 }" :disabled="is_choose" />
                    </el-form-item>
                    <div style="text-align: center; margin: 20px 0;">
                        <el-button type="default" @click="resetForm(ruleFormRef)">重置</el-button>
                        <el-button type="primary" @click="trans(); submitForm(ruleFormRef)">完成</el-button>
                    </div>
                </el-form>
            </div>
            <!-- 弹出选择员工的对话框 -->
            <el-dialog v-model="dialog" title="选择员工">
                <!-- 查询域 -->
                <div class="search">
                    <div style="width: 50%;">
                        <el-input v-model="searchForm.username" placeholder="姓名" :prefix-icon="Search"
                            style="width: 40%;" />
                        <el-input v-model="searchForm.phoneNumber" placeholder="电话" :prefix-icon="Search"
                            style="width: 60%;" />
                    </div>
                    <div>
                        <el-button type="primary" @click="onSearch">查询</el-button>
                        <el-button @click="resetSearch">重置</el-button>
                    </div>
                </div>
                <!-- 员工列表 -->
                <div style="width: 80%; margin: 0 auto;">
                    <el-table :data="tableData">
                        <el-table-column prop="avatar" label="头像" align="center">
                            <template #default="scope">
                                <el-image style="width: 50px; height: 50px" :src="scope.row.avatar" fit="cover" />
                            </template>
                        </el-table-column>
                        <el-table-column prop="username" label="员工昵称" align="center" width="100px" />
                        <el-table-column prop="phoneNumber" label="手机号码" align="center" min-width="" />
                        <el-table-column label="操作" align="center" width="100px">
                            <template #default="scoped">
                                <el-button link type="primary" @click="choose(scoped.row)">选择</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <el-pagination layout="prev, pager, next" :total="totalItems" @change="changePage" />
                </div>
            </el-dialog>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive, computed, ComputedRef, onMounted } from 'vue'
import { type FormInstance, type FormRules, ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import api from '../../api/api'
import { regionData, codeToText } from 'element-china-area-data'
import { useTokenStore, useIdStore } from '../../assets/stores/index.js'

const dialog = ref(false)
const is_choose = ref(false)
const idStore = useIdStore();
const pageInfo = reactive({
    id: idStore.$state.id,
    pageNum: 1,
    pageSize: 10
})
const tableData = ref([])
const totalItems = ref(0)

const searchForm = ref({
    username: '',
    phoneNumber: ''
})

interface RuleForm {
    storeName: string
    storeContact: string
    addressProvince: string
    addressCity: string
    addressDetail: string
    provinceCode: string
    cityCode: string
    username: string
    gender: string
    phoneNumber: string
    identity: string
    address: string
    userId: string
}
const ruleFormRef = ref<FormInstance>()
const ruleForm = reactive<RuleForm>({
    // 店铺的属性
    storeName: '',         // 店名
    storeContact: '',      // 座机
    addressProvince: '',   // 省份
    addressCity: '',       // 城市
    addressDetail: '',     // 详细地址
    // 以下两个变量只是选择器使用，不是提交的表单项
    provinceCode: '',   // 省份编号
    cityCode: '',       // 城市编号
    // 店长的属性
    username: '',       // 姓名
    gender: '',         // 性别
    phoneNumber: '',    // 电话
    identity: '',       // 身份证号
    address: '',        // 家庭住址
    // 如果是选择现有员工作为店长
    userId: ''          // 员工id
})
// 表单项目的验证规则
const rules = reactive<FormRules<RuleForm>>({
    storeName: [
        { required: true, message: "请输入门店名称", trigger: "change" }
    ],
    storeContact: [
        { required: true, message: "请输入联系方式", trigger: "change" }
    ],
    provinceCode: [
        { required: true, message: "请选择省份", trigger: "change" }
    ],
    cityCode: [
        { required: true, message: "请选择城市", trigger: "change" }
    ],
    addressDetail: [
        { required: true, message: "请输入详细地址", trigger: "change" }
    ],
    username: [
        { required: true, message: "请输入姓名", trigger: "change" }
    ],
    gender: [
        { required: true, message: "请选择性别", trigger: "change" }
    ],
    phoneNumber: [
        { required: true, message: "请输入电话号码", trigger: "change" }
    ],
    identity: [
        { required: true, message: "请输入身份证号码", trigger: "change" }
    ],
    address: [
        { required: true, message: "请输入家庭地址", trigger: "change" }
    ],
})
// 提交表单
const submitForm = async (formEl: FormInstance | undefined) => {
    if (!formEl) return
    await formEl.validate((valid, fields) => {
        if (valid) {
            // 将验证合法ruleForm对象发送给后端
            let res: any
            if (is_choose) {
                res = api.addStoreByChooseUser(ruleForm)
            } else {
                res = api.addStoreByCreateUser(ruleForm)
            }
            ElMessage({ message: "成功创建", type: "success" })
            resetForm(formEl)
            console.log(res)
        } else {
            // 否则提示需要录入的项目
            console.log('error submit!', fields)
        }
    })
}
// 重置表单
const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
    is_choose.value = false
}
// 地址转中文
const trans = () => {
    ruleForm.addressProvince = codeToText[ruleForm.provinceCode]
    ruleForm.addressCity = codeToText[ruleForm.cityCode]
}
// 抛出地址
const emit = defineEmits<{
    (e: 'getAddress', data: {
        code: string[] //区域码
        name: string[] //汉字
        isComplete: boolean  //是否选择完整，方便校验
    }): void
}>()
// 切换省份函数
const handleProvinceSelect = () => {
    ruleForm.cityCode = ''
    emit('getAddress', {
        code: [ruleForm.provinceCode], //区域码
        name: [codeToText[ruleForm.provinceCode]], //汉字
        isComplete: false
    })
}
// 切换城市函数
const handleCitySelect = () => {
    emit('getAddress', {
        code: [ruleForm.provinceCode, ruleForm.cityCode], //区域码
        name: [codeToText[ruleForm.provinceCode], codeToText[ruleForm.cityCode]], //汉字
        isComplete: true
    })
}
interface AreaList {
    value: string
    label: string
    children?: AreaList[]
}
// 二级城市列表
const cityList: ComputedRef<AreaList[]> = computed((): AreaList[] => {
    if (!ruleForm.provinceCode) {
        return []
    }
    var temp = regionData.find((item: any) => {
        return item.value == ruleForm.provinceCode
    })
    if (temp && temp.children)
        return temp.children
    return []
})

onMounted(() => {

})
// 打开员工选择界面
const openDialog = () => {
    pageInfo.pageNum = 1
    pageInfo.pageSize = 10
    getList()
    // 如果是重新选择就清空选择条件
    dialog.value = true
}
// 获取员工列表
const getList = async () => {
    let res: any = await api.getAllInfo(pageInfo)
    totalItems.value = res.total
    tableData.value = res.rows
}
// 点击查询按钮
const onSearch = () => {
    pageInfo.pageNum = 1
    pageInfo.pageSize = 10
    search()
}
// 根据条件查询员工
const search = async () => {
    if (searchForm.value.username == '' && searchForm.value.phoneNumber == '')
        return
    // 合并分页和查询对象
    const tmp = Object.assign({}, pageInfo, searchForm.value)
    let res: any = await api.searchUserInfo(tmp)
    totalItems.value = res.total
    tableData.value = res.rows
}
// 重置查询条件
const resetSearch = async () => {
    if (searchForm.value.username != '' || searchForm.value.phoneNumber != '') {
        searchForm.value.username = ''
        searchForm.value.phoneNumber = ''
        pageInfo.pageNum = 1
        pageInfo.pageSize = 10
        let res: any = await api.getAllInfo(pageInfo)
        totalItems.value = res.total
        tableData.value = res.rows
    }
}
// 切换分页
const changePage = (page: number) => {
    pageInfo.pageNum = page
    if (searchForm.value.username != '' || searchForm.value.phoneNumber != '') {
        search()
    } else {
        getList()
    }
}
// 选择这个员工作为店长
const choose = (row: any) => {
    // 接口字段不统一！
    ruleForm.username = row.username
    // ruleForm.gender = row.gender
    ruleForm.gender = "男"
    ruleForm.phoneNumber = row.phoneNumber
    // ruleForm.identity = row.identity
    ruleForm.identity = "123456"
    // ruleForm.address = row.address
    ruleForm.address = "654321"
    ruleForm.userId = row.id
    is_choose.value = true
    dialog.value = false
}
// 重置店长选择
const resetChoose = () => {
    let tmp = Object.assign({}, ruleForm)
    ruleFormRef.value?.resetFields()
    ruleForm.storeName = tmp.storeName
    ruleForm.storeContact = tmp.storeContact
    ruleForm.provinceCode = tmp.provinceCode
    ruleForm.cityCode = tmp.cityCode
    ruleForm.addressDetail = tmp.addressDetail
    is_choose.value = false
}


</script>

<style scoped>
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
}

.form {
    margin: 100px auto;
    height: fit-content;
    text-align: left;
}

.formTitle {
    text-align: left;
    font-size: 18px;
    margin: 20px 0;
    vertical-align: bottom;
}


.search {
    display: flex;
    justify-content: space-around;
    margin: 10px;
}
</style>