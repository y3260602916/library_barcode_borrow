<template>
  <el-card shadow="hover" title="👥 师生账号管理" style="margin-top: 20px">
    <!-- 搜索栏 -->
    <div style="display: flex; gap: 10px; margin-bottom: 16px; align-items: center">
      <el-input
        v-model="keyword"
        placeholder="请输入账号/姓名搜索"
        style="width: 300px"
        clearable
        @keyup.enter="getUserList"
      />
      <el-select v-model="userType" placeholder="选择身份类型" style="width: 150px">
        <el-option label="全部" :value="''" />
        <el-option label="师生" :value="0" />
        <el-option label="管理员" :value="1" />
      </el-select>
      <el-button type="primary" icon="Search" @click="getUserList">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
      <el-button type="success" icon="Plus" @click="openAddModal">新增用户</el-button>
    </div>

    <!-- 用户表格 -->
    <el-table
      :data="userList"
      border
      stripe
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column label="用户ID" prop="userId" width="80" align="center" />
      <el-table-column label="账号" prop="userAccount" width="120" align="center" />
      <el-table-column label="姓名" prop="userName" />
      <el-table-column label="身份" prop="userType" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.userType === 1 ? 'danger' : 'success'">
            {{ row.userType === 1 ? '管理员' : '师生' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180" align="center">
        <template #default="{ row }">
          {{ formatDateTime(row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template #default="{ row }">
          <el-button type="warning" size="small" @click="resetPassword(row.userId)">重置密码</el-button>
          <el-button type="danger" size="small" @click="deleteUser(row.userId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty description="暂无用户数据" v-if="userList.length === 0 && !loading" />

    <!-- 新增用户弹窗 -->
    <el-dialog title="新增用户" v-model="showModal" width="400px">
      <el-form :model="userForm" label-width="80px">
        <el-form-item label="账号" :required="true">
          <el-input v-model="userForm.userAccount" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item label="姓名" :required="true">
          <el-input v-model="userForm.userName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="密码" :required="true">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="身份">
          <el-select v-model="userForm.userType" placeholder="请选择身份">
            <el-option label="师生" :value="0" />
            <el-option label="管理员" :value="1" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showModal = false">取消</el-button>
        <el-button type="primary" @click="addUser">确定</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const userList = ref([])

// 格式化日期时间
const formatDateTime = (timestamp) => {
  if (!timestamp) return '-'
  const date = new Date(timestamp)
  if (isNaN(date.getTime())) return '-'
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}
const loading = ref(false)
const keyword = ref('')
const userType = ref('')
const showModal = ref(false)
const userForm = ref({
  userAccount: '',
  userName: '',
  password: '',
  userType: 0
})

const getUserList = async () => {
  loading.value = true
  try {
    const params = {}
    if (keyword.value) {
      params.keyword = keyword.value
    }
    if (userType.value !== '') {
      params.userType = userType.value
    }
    const res = await axios.get('/user/list', { params })
    if (res.data.code === 200) {
      userList.value = res.data.data || []
    } else {
      ElMessage.error(res.data.msg || '获取用户列表失败')
    }
  } catch (err) {
    console.error('获取用户列表失败:', err)
    ElMessage.error('网络异常，获取用户列表失败')
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  keyword.value = ''
  userType.value = ''
  getUserList()
}

const openAddModal = () => {
  userForm.value = {
    userAccount: '',
    userName: '',
    password: '',
    userType: 0
  }
  showModal.value = true
}

const addUser = async () => {
  console.log('addUser function called')
  console.log('userForm:', userForm.value)
  
  if (!userForm.value.userAccount) {
    ElMessage.warning('请输入账号')
    return
  }
  if (!userForm.value.userName) {
    ElMessage.warning('请输入姓名')
    return
  }
  if (!userForm.value.password) {
    ElMessage.warning('请输入密码')
    return
  }

  try {
    console.log('准备发送请求，数据:', JSON.stringify(userForm.value))
    const res = await axios.post('/user/add', userForm.value)
    console.log('请求结果:', res)
    if (res.data.code === 200) {
      ElMessage.success('添加成功')
      showModal.value = false
      getUserList()
    } else {
      ElMessage.error(res.data.msg || '添加失败')
    }
  } catch (err) {
    console.error('新增用户失败:', err)
    if (err.response) {
      console.error('响应错误:', err.response.data)
      ElMessage.error('添加失败: ' + (err.response.data?.msg || err.message))
    } else {
      ElMessage.error('网络异常，添加失败: ' + err.message)
    }
  }
}

const resetPassword = async (userId) => {
  try {
    await ElMessageBox.confirm('确定要重置该用户密码吗？重置后密码将变为默认值123456', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })
    const res = await axios.put('/user/reset-password', { userId, newPassword: '123456' })
    if (res.data.code === 200) {
      ElMessage.success('密码重置成功，新密码为123456')
    } else {
      ElMessage.error(res.data.msg || '重置失败')
    }
  } catch (err) {
    if (err !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const deleteUser = async (userId) => {
  console.log('deleteUser function called with userId:', userId)
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    console.log('用户确认删除，准备发送请求')
    const res = await axios.delete(`/user/delete/${userId}`)
    console.log('删除请求结果:', res)
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      getUserList()
    } else {
      ElMessage.error(res.data.msg || '删除失败')
    }
  } catch (err) {
    console.error('删除用户失败:', err)
    if (err.response) {
      console.error('响应错误:', err.response.data)
      ElMessage.error('删除失败: ' + (err.response.data?.msg || err.message))
    } else if (err !== 'cancel') {
      ElMessage.error('删除失败，可能存在关联数据')
    }
  }
}

onMounted(() => {
  getUserList()
})
</script>
