<template>
    <div class="container">
        <div class="form-container">
            <div class="form">
                <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" :size="formSize" label-width="auto"
                    style="max-width:400px; margin:0 auto" class="demo-rule-form" status-icon>
                    <el-form-item label="员工姓名" prop="username">
                        <el-input v-model="ruleForm.username"  />
                    </el-form-item>
                    <el-form-item label="性别" prop="gender">
                        <el-select v-model="ruleForm.gender" placeholder="" >
                            <el-option label="男" value="男" />
                            <el-option label="女" value="女" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="联系电话" prop="phoneNumber">
                        <el-input v-model="ruleForm.phoneNumber"  />
                    </el-form-item>
                    <el-form-item label="身份证号码" prop="identity">
                        <el-input v-model="ruleForm.identity"  />
                    </el-form-item>
                    <el-form-item label="家庭住址" prop="address">
                        <el-input v-model="ruleForm.address" type="textarea"  :autosize="{ minRows: 1, maxRows: 3 }"/>
                    </el-form-item>
                </el-form>
                <el-button @click="resetForm(ruleFormRef)">重置</el-button>
                <el-button type="primary" @click="submitForm(ruleFormRef)">完成</el-button>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import api from '../../api/api'
interface RuleForm {
    username: string
    gender: string
    phoneNumber: string
    identity: string
    address: string
}
const formSize = ref('default')
const ruleFormRef = ref<FormInstance>()
const ruleForm = reactive<RuleForm>({
    username: '',   // 员工姓名
    gender: '',        // 性别
    phoneNumber: '',// 电话号码
    identity: '',   // 身份证号
    address: '',    // 家庭地址
})
// 表单项目的验证规则
const rules = reactive<FormRules<RuleForm>>({
    username: [
        { required: true, message: "请输入员工姓名", trigger: "change" },
    ],
    gender: [
        { required: true, message: "请选择性别", trigger: "change" }
    ],
    phoneNumber: [
        { required: true, message: "请输入联系电话", trigger: "change" }
    ],
    identity: [
        { required: true, message: "请输入身份证号", trigger: "change" }
    ],
    address: [
        { required: true, message: "请输入家庭住址", trigger: "change" }
    ]
})
// 提交表单
const submitForm = async (formEl: FormInstance | undefined) => {
    if (!formEl) return
    await formEl.validate((valid, fields) => {
        if (valid) {
            // 将验证合法ruleForm对象发送给后端
            // console.log(ruleForm)
            var res = api.addEmployee(ruleForm)
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
}


</style>