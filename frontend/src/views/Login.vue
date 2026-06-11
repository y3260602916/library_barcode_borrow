<template>
  <div class="login-container">
    <div class="login-box">
      <h2>智能图书借阅系统</h2>
      <el-form ref="loginFormRef" :model="loginForm" label-width="80px">
        <el-form-item label="账号">
          <el-input v-model="loginForm.userAccount" placeholder="请输入登录账号"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <!-- 错误提示 -->
        <div class="error-tip" v-if="errorMsg">{{ errorMsg }}</div>
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="login">登 录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
// 新增：引入 useRouter
import { useRouter } from 'vue-router'

// 表单数据
const loginForm = ref({
  userAccount: '',
  password: ''
})
// 错误提示
const errorMsg = ref('')
// 新增：获取路由实例
const router = useRouter()

// 登录请求
const login = async () => {
  errorMsg.value = ''
  // 简单前端非空校验
  if (!loginForm.value.userAccount) {
    errorMsg.value = '请输入账号'
    return
  }
  if (!loginForm.value.password) {
    errorMsg.value = '请输入密码'
    return
  }

  try {
    const res = await axios.post('/user/login', loginForm.value)
    if (res.data.code === 200) {
      // 登录成功：存储用户信息到本地缓存
      localStorage.setItem('userInfo', JSON.stringify(res.data.data))
      ElMessage.success('登录成功')
      // 替换原来的 window.location.reload()，改用路由跳转
      router.push('/ocr') // 登录成功后直接跳转到自助借还页
    } else {
      errorMsg.value = res.data.msg
    }
  } catch (err) {
    errorMsg.value = '网络请求失败'
  }
}
</script>

<style scoped>
.login-container {
  width: 100vw;
  height: 100vh;
  background-color: #f5f7fa;
  display: flex;
  justify-content: center;
  align-items: center;
}
.login-box {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}
.login-btn {
  width: 100%;
}
.error-tip {
  color: #f56c6c;
  text-align: center;
  margin: 10px 0;
}
</style>