<template>
    <div class="common-layout">我是home</div>
</template>

<script>
import { defineComponent,getCurrentInstance,onMounted } from 'vue'
import axios from 'axios'
export default defineComponent({
    setup() {
        // const {proxy} = getCurrentInstance();
        // const getEmployeeList = async () => {
        //     // await axios 
        //     // .get("http://127.0.0.1:4523/m1/4177433-0-default/users/getUserInfo") 
        //     // .then((res) => {
        //     //     console.log(res);
        //     // });
        //     //上面是局部挂载
        //     let res = await proxy.$api.getEmployeeList();
        //     // console.log(res);

        // }
        // onMounted(() => {
        //     getEmployeeList();
        // })
        // return {
            
        // }
    },
})
</script>


<style lang="less" scoped>
.common-layout {
    
}
</style>