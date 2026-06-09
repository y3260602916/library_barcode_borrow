<template>
  <el-card shadow="hover" title="📖 我的借阅记录" style="margin-top: 20px">
    <!-- 刷新按钮 -->
    <div style="margin-bottom: 16px;">
      <el-button type="primary" icon="Refresh" @click="getBorrowList">刷新记录</el-button>
    </div>

    <!-- 借阅记录表格 -->
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
          <!-- 仅未归还时显示归还按钮 -->
          <el-button
            type="warning"
            size="small"
            :disabled="row.returnTime !== null"
            @click="handleReturn(row.recordId)"
          >
            归还图书
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 暂无数据提示 -->
    <el-empty description="暂无借阅记录" v-if="borrowList.length === 0 && !loading" />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getCurrentInstance } from 'vue'
const { proxy } = getCurrentInstance()

// 借阅记录列表、加载状态、用户ID
const borrowList = ref([])
const loading = ref(false)
const userId = ref(null)

// 页面加载：读取用户ID + 查询借阅记录
onMounted(() => {
  const userInfo = localStorage.getItem('userInfo')
  if (userInfo) {
    userId.value = JSON.parse(userInfo).userId
    getBorrowList()
  } else {
    ElMessage.warning('请先登录')
  }
})

// 查询当前用户借阅记录（复用后端 /borrow/list 接口）
const getBorrowList = async () => {
  if (!userId.value) return
  loading.value = true
  try {
    const res = await proxy.$axios.get('/borrow/list', {
      params: {
        userId: userId.value
      }
    })
    if (res.data.code === 200) {
      borrowList.value = res.data.data
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (err) {
    ElMessage.error('查询借阅记录失败')
  } finally {
    loading.value = false
  }
}

// 归还图书（调用后端 /borrow/return 接口）
const handleReturn = async (recordId) => {
  try {
    const res = await proxy.$axios.put('/borrow/return', null, {
      params: {
        recordId: recordId
      }
    })
    if (res.data.code === 200) {
      ElMessage.success(res.data.msg)
      // 归还成功后刷新表格
      getBorrowList()
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (err) {
    ElMessage.error('归还图书失败')
  }
}
</script>