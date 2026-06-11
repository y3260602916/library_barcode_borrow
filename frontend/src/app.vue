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

      <div style="padding: 20px;">
        <!-- 标签页切换所有功能模块 -->
        <el-tabs v-model="activeTab" type="card">
          <el-tab-pane label="自助借还" name="ocr">
            <BarcodeOcr />
          </el-tab-pane>
          <el-tab-pane label="图书列表" name="bookList">
            <BookList />
          </el-tab-pane>
          <el-tab-pane label="我的借阅记录" name="borrow">
            <BorrowRecord />
          </el-tab-pane>
          <el-tab-pane label="数据统计" name="chart">
            <DataChart />
          </el-tab-pane>
        </el-tabs>
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
import BorrowRecord from './components/BorrowRecord.vue'
import DataChart from './components/DataChart.vue'
import BookList from './components/BookList.vue'

// 登录状态 & 用户信息
const isLogin = ref(false)
const userInfo = ref({})
// 标签页激活项
const activeTab = ref('ocr')

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