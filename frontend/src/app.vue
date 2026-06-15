<template>
  <div class="app-container">
    <!-- 顶部导航栏 - 师生角色 -->
    <div class="top-bar" v-if="isLogin && userInfo.userType === 0">
      <span>
        当前登录：{{ userInfo.userName }}
        ｜
        身份：师生
      </span>
      <div class="nav-btn">
        <el-button @click="$router.push('/ocr')">扫码借书</el-button>
        <el-button @click="$router.push('/bookList')">图书列表</el-button>
        <el-button @click="$router.push('/borrowRecord')">我的借阅记录</el-button>
        <el-button @click="$router.push('/chart')">数据统计</el-button>
        <el-button type="text" @click="logout">退出登录</el-button>
      </div>
    </div>

    <!-- 顶部导航栏 - 管理员角色 -->
    <div class="top-bar admin-bar" v-if="isLogin && userInfo.userType === 1">
      <span>
        当前登录：{{ userInfo.userName }}
        ｜
        身份：<span style="color: #f56c6c; font-weight: bold;">管理员</span>
      </span>
      <div class="nav-btn">
        <el-button type="primary" @click="$router.push('/admin/users')">账号管理</el-button>
        <el-button @click="$router.push('/admin/inventory')">出入库管理</el-button>
        <el-button @click="$router.push('/admin/stock')">库存管理</el-button>
        <el-button @click="$router.push('/admin/overdue')">逾期管理</el-button>
        <el-button @click="$router.push('/admin/stats')">统计分析</el-button>
        <el-button type="text" @click="logout">退出登录</el-button>
      </div>
    </div>

    <!-- 路由出口：当前路由页面在这里渲染 -->
    <div class="content">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

// 用响应式变量存登录状态
const isLogin = ref(false)
const userInfo = ref({})

// 封装一个读取登录状态的方法，每次都从 localStorage 重新读
const updateLoginState = () => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    isLogin.value = true
    userInfo.value = JSON.parse(user)
  } else {
    isLogin.value = false
    userInfo.value = {}
  }
}

// 页面加载时先读一次
updateLoginState()

// 关键：监听路由变化，每次跳转都重新读登录状态
watch(
  () => route.path,
  () => {
    updateLoginState()
  }
)

// 退出登录
const logout = () => {
  localStorage.removeItem('userInfo')
  updateLoginState() // 退出后也更新状态
  ElMessage.success('已退出登录')
  router.push('/login')
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
  align-items: center;
  font-size: 16px;
}
.admin-bar {
  background: linear-gradient(135deg, #1f2937 0%, #111827 100%);
  color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}
.admin-bar .el-button {
  color: #fff;
  border-color: rgba(255, 255, 255, 0.3);
  background: transparent;
}
.admin-bar .el-button:hover {
  background: rgba(255, 255, 255, 0.1);
}
.admin-bar .el-button--primary {
  background: #f56c6c;
  border-color: #f56c6c;
}
.admin-bar .el-button--primary:hover {
  background: #f78989;
  border-color: #f78989;
}
.admin-bar .el-button--text {
  color: #9ca3af;
}
.nav-btn {
  display: flex;
  gap: 10px;
}
.content {
  padding: 20px;
}
</style>