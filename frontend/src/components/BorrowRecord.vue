<template>
  <el-card shadow="hover" title="📖 我的借阅记录" style="margin-top: 20px">
    <div style="margin-bottom: 16px;">
      <el-button type="primary" icon="Refresh" @click="getBorrowList">刷新记录</el-button>
    </div>

    <el-table
      :data="borrowList"
      border
      stripe
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column label="记录ID" prop="recordId" width="80" align="center" />
      <el-table-column label="图书ID" prop="bookId" width="80" align="center" />
      <el-table-column label="借阅时间" prop="borrowTime" align="center" />
      <el-table-column label="归还时间" prop="returnTime" align="center">
        <template #default="{ row }">
          {{ row.returnTime ? row.returnTime : '未归还' }}
        </template>
      </el-table-column>
      <el-table-column label="是否逾期" prop="isOverdue" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.isOverdue === 1 ? 'danger' : 'success'">
            {{ row.isOverdue === 1 ? '已逾期' : '正常' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="逾期罚款(元)" prop="fineMoney" width="120" align="center" />
      <el-table-column label="操作" width="120" align="center">
        <template #default="{ row }">
          <el-button
            type="warning"
            size="small"
            :disabled="row.returnTime !== null || btnLoading"
            @click="handleReturn(row.recordId)"
          >
            归还图书
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty description="暂无借阅记录" v-if="borrowList.length === 0 && !loading" />
  </el-card>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue'
import { ElMessage } from 'element-plus'

// 全局实例，兼容你项目的 $router/$axios
const { proxy } = getCurrentInstance()
const globalRouter = proxy?.$router

const borrowList = ref([])
const loading = ref(false)
const btnLoading = ref(false)
const userId = ref(null)

// 🔴 只有点击归还的时候，才校验扫码！查看记录不拦截！
const checkScanFlag = () => {
  const flag = sessionStorage.getItem('scanFlag')
  if (!flag) {
    ElMessage.warning('请先前往自助借还页拍照识别图书条码！')
    // 兼容 router 不存在的情况，不报错
    if (globalRouter && typeof globalRouter.push === 'function') {
      globalRouter.push('/barcode-ocr')
    }
    return false
  }
  return true
}

// 解析用户信息
const getUserId = () => {
  const userInfoStr = localStorage.getItem('userInfo')
  if (!userInfoStr) {
    ElMessage.warning('请先登录')
    return null
  }
  try {
    const userInfo = JSON.parse(userInfoStr)
    return userInfo.userId
  } catch (error) {
    ElMessage.warning('用户信息异常，请重新登录')
    localStorage.removeItem('userInfo')
    return null
  }
}

// 获取借阅列表
const getBorrowList = async () => {
  const uid = getUserId()
  if (!uid) return
  userId.value = uid

  loading.value = true
  try {
    const res = await proxy.$axios.get('/borrow/list', {
      params: { userId: uid }
    })
    if (res.data.code === 200) {
      borrowList.value = Array.isArray(res.data.data) ? res.data.data : []
    } else {
      ElMessage.error(res.data.msg || '获取借阅记录失败')
    }
  } catch (err) {
    ElMessage.error('网络异常，查询借阅记录失败')
  } finally {
    loading.value = false
  }
}

// 归还图书
const handleReturn = async (recordId) => {
  // 🔴 只有归还的时候，才校验扫码！
  if (!checkScanFlag()) return
  btnLoading.value = true

  try {
    const res = await proxy.$axios.put('/borrow/return', null, {
      params: { recordId }
    })
    if (res.data.code === 200) {
      ElMessage.success(res.data.msg || '归还成功')
      sessionStorage.removeItem('scanFlag')
      getBorrowList()
    } else {
      ElMessage.error(res.data.msg || '归还图书失败')
    }
  } catch (err) {
    ElMessage.error('网络异常，归还图书失败')
  } finally {
    btnLoading.value = false
  }
}

// 页面加载：直接加载数据，不拦截！
onMounted(() => {
  getBorrowList()
})
</script>