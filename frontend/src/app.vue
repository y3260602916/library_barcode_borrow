<template>
  <div>
    <!-- 未登录：显示登录页 -->
    <Login v-if="!isLogin" />

    <!-- 已登录：主页面 -->
    <div v-else class="main">
      <!-- 顶部导航栏 -->
      <div class="top-bar">
        <span>
          当前登录：{{ userInfo.userName }}
          ｜
          身份：{{ userInfo.userType === 0 ? '师生' : '管理员' }}
        </span>
        <el-button type="text" @click="logout">退出登录</el-button>
      </div>

      <!-- 功能区域：扫码识别 + 个人借阅记录 -->
      <div style="padding: 20px;">
        <!-- 条码识别借阅 -->
        <BarcodeOcr />
        <!-- 个人借阅记录（还书入口） -->
        <BorrowRecord />
        <!-- 数据图表 -->
        <DataChart style="margin-top: 20px;" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
// 引入所有组件
import Login from './Login.vue'
import BarcodeOcr from './components/BarcodeOcr.vue'
import BorrowRecord from './components/BorrowRecord.vue' // 新增借阅记录组件
import DataChart from './components/DataChart.vue'

// 登录状态 & 用户信息
const isLogin = ref(false)
const userInfo = ref({})

// 读取本地登录信息
onMounted(() => {
  let user = localStorage.getItem('userInfo')
  if (user) {
    isLogin.value = true
    userInfo.value = JSON.parse(user)
  }
})

// 退出登录
const logout = () => {
  localStorage.removeItem('userInfo')
  isLogin.value = false
  userInfo.value = {}
  ElMessage.success('已退出登录')
}
</script>

<style scoped>
.top-bar {
  height: 60px;
  line-height: 60px;
  padding: 0 30px;
  background: #fff;
  box-shadow: 0 1px 4px #ccc;
  display: flex;
  justify-content: space-between;
  font-size: 16px;
}
.main {
  width: 100%;
}
</style>