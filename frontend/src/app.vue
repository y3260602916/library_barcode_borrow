<template>
  <div class="app-container">
    <!-- 动态背景效果 -->
    <div class="bg-gradient"></div>
    <div class="bg-blob blob-1"></div>
    <div class="bg-blob blob-2"></div>
    <div class="bg-blob blob-3"></div>

    <!-- 顶部导航栏 - 师生角色 -->
    <div v-if="isLogin && userInfo.userType === 0 && $route.path !== '/login'" class="top-bar glass-nav">
      <div class="logo-section">
        <div class="logo-icon">
          <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
            <path d="M6 4h20v20H6z" fill="#667eea" opacity="0.8"/>
            <path d="M10 8h12v12H10z" fill="white"/>
            <circle cx="16" cy="16" r="4" fill="#764ba2"/>
          </svg>
        </div>
        <span class="logo-text">智慧图书馆</span>
      </div>
      <div class="user-info">
        <el-avatar :size="36" :icon="UserFilled" class="avatar" />
        <span class="user-name">{{ userInfo.userName }}</span>
        <span class="user-badge student">师生</span>
      </div>
      <div class="nav-menu">
        <router-link to="/ocr" class="nav-item" :class="{ active: $route.path === '/ocr' }">
          <el-icon><Camera /></el-icon>
          <span>自助借还</span>
        </router-link>
        <router-link to="/bookList" class="nav-item" :class="{ active: $route.path === '/bookList' }">
          <el-icon><Notebook /></el-icon>
          <span>图书列表</span>
        </router-link>
        <router-link to="/borrowRecord" class="nav-item" :class="{ active: $route.path === '/borrowRecord' }">
          <el-icon><List /></el-icon>
          <span>我的借阅</span>
        </router-link>
        <router-link to="/chart" class="nav-item" :class="{ active: $route.path === '/chart' }">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据统计</span>
        </router-link>
      </div>
      <el-button class="logout-btn" @click="logout">
        <el-icon><SwitchButton /></el-icon>
        <span>退出</span>
      </el-button>
    </div>

    <!-- 顶部导航栏 - 管理员角色 -->
    <div v-if="isLogin && userInfo.userType === 1 && $route.path !== '/login'" class="top-bar glass-nav admin-nav">
      <div class="logo-section">
        <div class="logo-icon admin-icon">
          <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
            <path d="M6 4h20v20H6z" fill="#f56c6c" opacity="0.8"/>
            <path d="M10 8h12v12H10z" fill="white"/>
            <circle cx="16" cy="16" r="4" fill="#c0392b"/>
          </svg>
        </div>
        <span class="logo-text">智慧图书馆 · 管理后台</span>
      </div>
      <div class="user-info">
        <el-avatar :size="36" :icon="UserFilled" class="avatar admin-avatar" />
        <span class="user-name">{{ userInfo.userName }}</span>
        <span class="user-badge admin">管理员</span>
      </div>
      <div class="nav-menu">
        <router-link to="/admin/users" class="nav-item" :class="{ active: $route.path === '/admin/users' }">
          <el-icon><User /></el-icon>
          <span>账号管理</span>
        </router-link>
        <router-link to="/admin/inventory" class="nav-item" :class="{ active: $route.path === '/admin/inventory' }">
          <el-icon><Box /></el-icon>
          <span>出入库</span>
        </router-link>
        <router-link to="/admin/stock" class="nav-item" :class="{ active: $route.path === '/admin/stock' }">
          <el-icon><Shop /></el-icon>
          <span>库存管理</span>
        </router-link>
        <router-link to="/admin/overdue" class="nav-item" :class="{ active: $route.path === '/admin/overdue' }">
          <el-icon><Reading /></el-icon>
          <span>借阅管理</span>
        </router-link>
        <router-link to="/admin/stats" class="nav-item" :class="{ active: $route.path === '/admin/stats' }">
          <el-icon><TrendCharts /></el-icon>
          <span>统计分析</span>
        </router-link>
      </div>
      <el-button class="logout-btn admin-logout" @click="logout">
        <el-icon><SwitchButton /></el-icon>
        <span>退出</span>
      </el-button>
    </div>

    <!-- 路由出口 -->
    <div class="content-wrapper">
      <router-view v-slot="{ Component }">
        <transition name="page-fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
import {
  UserFilled, Camera, Notebook, List, DataAnalysis, SwitchButton,
  User, Box, Shop, Warning, TrendCharts, Reading
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const isLogin = ref(false)
const userInfo = ref({})

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

updateLoginState()

watch(() => route.path, () => {
  updateLoginState()
})

const logout = () => {
  localStorage.removeItem('userInfo')
  updateLoginState()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style>
@import './styles/global.css';

.page-fade-enter-active,
.page-fade-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.page-fade-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.page-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* 背景效果 */
.bg-gradient {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  z-index: -2;
}

.bg-blob {
  position: fixed;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.4;
  z-index: -1;
}

.blob-1 {
  width: 400px;
  height: 400px;
  background: #f093fb;
  top: -100px;
  right: -100px;
  animation: float 8s ease-in-out infinite;
}

.blob-2 {
  width: 500px;
  height: 500px;
  background: #4facfe;
  bottom: -150px;
  left: -150px;
  animation: float 10s ease-in-out infinite reverse;
}

.blob-3 {
  width: 300px;
  height: 300px;
  background: #43e97b;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: float 12s ease-in-out infinite;
}

/* 导航栏样式 */
.top-bar {
  position: fixed;
  top: 20px;
  left: 20px;
  right: 20px;
  z-index: 100;
  border-radius: 20px;
  padding: 12px 24px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  box-shadow: 0 10px 40px -12px rgba(0, 0, 0, 0.2);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.glass-nav {
  background: rgba(255, 255, 255, 0.98);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-text {
  font-size: 18px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.nav-menu {
  display: flex;
  gap: 8px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 20px;
  border-radius: 40px;
  color: #4a5568;
  text-decoration: none;
  transition: all 0.3s ease;
  font-weight: 500;
}

.nav-item:hover {
  background: rgba(102, 126, 234, 0.1);
  color: #667eea;
}

.nav-item.active {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.user-name {
  font-weight: 600;
  color: #2d3748;
}

.user-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.user-badge.student {
  background: #e6f7e6;
  color: #2ecc71;
}

.user-badge.admin {
  background: #fee;
  color: #f56c6c;
}

.logout-btn {
  background: transparent !important;
  border: 1px solid #e2e8f0 !important;
  border-radius: 40px !important;
  color: #4a5568 !important;
}

.logout-btn:hover {
  background: #fee !important;
  border-color: #f56c6c !important;
  color: #f56c6c !important;
}

/* 内容区域 */
.content-wrapper {
  padding-top: 100px;
  padding-left: 24px;
  padding-right: 24px;
  padding-bottom: 24px;
  min-height: 100vh;
}

/* 管理员导航特殊样式 */
.admin-nav .logo-icon {
  background: linear-gradient(135deg, #f56c6c, #c0392b);
}

.admin-nav .logo-text {
  background: linear-gradient(135deg, #f56c6c, #c0392b);
  -webkit-background-clip: text;
  background-clip: text;
}

.admin-nav .nav-item.active {
  background: linear-gradient(135deg, #f56c6c, #c0392b);
}

.admin-logout:hover {
  background: #f56c6c20 !important;
  border-color: #f56c6c !important;
  color: #f56c6c !important;
}
</style>