<template>
  <div class="app-container">
    <!-- 顶部导航栏 -->
    <div class="top-bar" v-if="isLogin">
      <span>
        当前登录：{{ userInfo.userName }}
        ｜
        身份：{{ userInfo.userType === 0 ? '师生' : '管理员' }}
      </span>
      <div class="nav-btn">
        <el-button @click="$router.push('/ocr')">自助借还</el-button>
        <el-button @click="$router.push('/bookList')">图书列表</el-button>
        <el-button @click="$router.push('/borrowRecord')">我的借阅记录</el-button>
        <el-button @click="$router.push('/chart')">数据统计</el-button>
        <el-button type="link" @click="logout">退出登录</el-button>
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
.nav-btn {
  display: flex;
  gap: 10px;
}
.content {
  padding: 20px;
}
</style>