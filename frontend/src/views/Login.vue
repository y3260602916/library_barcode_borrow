<template>
  <div class="login-page">
    <div class="login-bg">
      <div class="bg-gradient"></div>
      <div class="bg-blob blob-1"></div>
      <div class="bg-blob blob-2"></div>
      <div class="bg-blob blob-3"></div>
    </div>

    <div class="login-container fade-in-up">
      <div class="login-card glass-card">
        <div class="card-header">
          <div class="logo-icon">
            <svg width="48" height="48" viewBox="0 0 32 32" fill="none">
              <path d="M6 4h20v20H6z" fill="#667eea" opacity="0.8"/>
              <path d="M10 8h12v12H10z" fill="white"/>
              <circle cx="16" cy="16" r="4" fill="#764ba2"/>
            </svg>
          </div>
          <h1>欢迎回来</h1>
          <p>登录您的智慧图书馆账号</p>
        </div>

        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="rules"
          class="login-form"
        >
          <el-form-item prop="userAccount">
            <el-input
              v-model="loginForm.userAccount"
              placeholder="请输入账号"
              :prefix-icon="User"
              size="large"
              class="custom-input"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              size="large"
              class="custom-input"
              show-password
            />
          </el-form-item>

          <div v-if="errorMsg" class="error-msg">
            <el-icon><Warning /></el-icon>
            <span>{{ errorMsg }}</span>
          </div>

          <el-button
            type="primary"
            size="large"
            class="login-btn gradient-btn"
            :loading="loading"
            @click="login"
          >
            {{ loading ? '登录中...' : '登录系统' }}
          </el-button>

          <div class="demo-tips">
            <p>演示账号</p>
            <div class="demo-cards">
              <div class="demo-card" @click="fillDemo('student')">
                <span>📚 师生</span>
                <span class="demo-account">2026001 / 123456</span>
              </div>
              <div class="demo-card" @click="fillDemo('admin')">
                <span>👨‍💼 管理员</span>
                <span class="demo-account">admin / 123456</span>
              </div>
            </div>
          </div>
        </el-form>
      </div>

      <div class="deco-text">
        <span>智慧图书馆 · 让阅读更简单</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { User, Lock, Warning } from '@element-plus/icons-vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const loginFormRef = ref(null)
const loginForm = ref({
  userAccount: '',
  password: ''
})
const errorMsg = ref('')
const loading = ref(false)

const rules = {
  userAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const fillDemo = (type) => {
  if (type === 'student') {
    loginForm.value.userAccount = '2026001'
    loginForm.value.password = '123456'
  } else {
    loginForm.value.userAccount = 'admin'
    loginForm.value.password = '123456'
  }
}

const login = async () => {
  if (!loginFormRef.value) return
  await loginFormRef.value.validate(async (valid) => {
    if (!valid) return

    errorMsg.value = ''
    loading.value = true

    try {
      const res = await axios.post('/user/login', loginForm.value)
      if (res.data.code === 200) {
        localStorage.setItem('userInfo', JSON.stringify(res.data.data))
        ElMessage.success('登录成功')
        if (res.data.data.userType === 1) {
          router.push('/admin/users')
        } else {
          router.push('/ocr')
        }
      } else {
        errorMsg.value = res.data.msg
      }
    } catch (err) {
      errorMsg.value = '网络请求失败'
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-bg {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 0;
}

.bg-gradient {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.bg-blob {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.3;
}

.blob-1 {
  width: 500px;
  height: 500px;
  background: #f093fb;
  top: -150px;
  right: -150px;
  animation: float 8s ease-in-out infinite;
}

.blob-2 {
  width: 600px;
  height: 600px;
  background: #4facfe;
  bottom: -200px;
  left: -200px;
  animation: float 10s ease-in-out infinite reverse;
}

.blob-3 {
  width: 350px;
  height: 350px;
  background: #43e97b;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: float 12s ease-in-out infinite;
}

.login-container {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 480px;
  margin: 20px;
}

.login-card {
  padding: 48px 40px;
  border-radius: 32px;
}

.card-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
}

.card-header h1 {
  font-size: 28px;
  font-weight: 700;
  color: #1a202c;
  margin-bottom: 8px;
}

.card-header p {
  color: #718096;
  font-size: 14px;
}

.login-form {
  margin-top: 8px;
}

.custom-input :deep(.el-input__wrapper) {
  border-radius: 14px;
  padding: 4px 12px;
  box-shadow: 0 0 0 1px #e2e8f0 inset;
}

.custom-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #667eea inset;
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.3), 0 0 0 1px #667eea inset;
}

.login-btn {
  width: 100%;
  height: 50px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 14px !important;
  margin-top: 24px;
}

.error-msg {
  background: #fee;
  color: #f56c6c;
  padding: 12px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  margin: 16px 0 0;
}

.demo-tips {
  margin-top: 32px;
  text-align: center;
}

.demo-tips > p {
  font-size: 12px;
  color: #a0aec0;
  margin-bottom: 12px;
}

.demo-cards {
  display: flex;
  gap: 12px;
}

.demo-card {
  flex: 1;
  background: #f7fafc;
  border-radius: 12px;
  padding: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
  border: 1px solid #e2e8f0;
}

.demo-card:hover {
  background: linear-gradient(135deg, #667eea15, #764ba215);
  border-color: #667eea;
  transform: translateY(-2px);
}

.demo-card span:first-child {
  display: block;
  font-weight: 600;
  margin-bottom: 4px;
  font-size: 14px;
}

.demo-account {
  font-size: 11px;
  color: #718096;
}

.deco-text {
  text-align: center;
  margin-top: 32px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
  letter-spacing: 2px;
}

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
}

.fade-in-up {
  animation: fadeInUp 0.8s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>