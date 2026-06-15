<template>
  <el-card shadow="hover" title="📚 全馆借阅管理" style="margin-top: 20px">
    <!-- 操作栏 -->
    <div style="display: flex; gap: 10px; margin-bottom: 16px; align-items: center; flex-wrap: wrap;">
      <el-button type="primary" icon="Refresh" @click="getBorrowList">刷新数据</el-button>
      
      <!-- 状态筛选 -->
      <el-select v-model="statusFilter" placeholder="请选择状态" style="width: 150px; margin-left: auto;">
        <el-option label="全部" value="all" />
        <el-option label="即将到期" value="warning" />
        <el-option label="已逾期" value="overdue" />
      </el-select>
      
      <span style="color: #f56c6c; margin-left: 10px;">逾期总数：{{ overdueCount }} 条</span>
      <span style="color: #e6a23c; margin-left: 10px;">未归还总数：{{ borrowList.length }} 条</span>
    </div>

    <!-- 借阅记录表格 -->
    <el-table
      :data="filteredList"
      border
      stripe
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column label="记录ID" prop="recordId" width="80" align="center" />
      <el-table-column label="图书名称" prop="bookName" />
      <el-table-column label="作者" prop="author" width="120" />
      <el-table-column label="借阅人" prop="userName" width="100" />
      <el-table-column label="借阅账号" prop="userAccount" width="120" />
      <el-table-column label="借阅时间" width="180" align="center">
        <template #default="{ row }">
          {{ formatDateTime(row.borrowTime) }}
        </template>
      </el-table-column>
      <el-table-column label="借阅天数" prop="daysSinceBorrow" width="100" align="center" />
      <el-table-column label="状态" width="120" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.isOverdue" type="danger">逾期 {{ row.overdueDays }} 天</el-tag>
          <el-tag v-else-if="row.daysRemaining <= 3 && row.daysRemaining > 0" type="warning">即将到期</el-tag>
          <el-tag v-else type="success">正常借阅</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="剩余天数" prop="daysRemaining" width="100" align="center">
        <template #default="{ row }">
          <span :style="{ color: row.isOverdue ? '#f56c6c' : row.daysRemaining <= 3 ? '#e6a23c' : '#67c23a' }">
            {{ row.isOverdue ? '-' + row.overdueDays : row.daysRemaining }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="逾期罚款" prop="fineMoney" width="120" align="center">
        <template #default="{ row }">
          <span v-if="row.isOverdue" style="color: #f56c6c;">¥{{ row.fineMoney }}</span>
          <span v-else style="color: #999;">¥0.00</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" align="center">
        <template #default="{ row }">
          <el-button 
            type="success" 
            size="small" 
            @click="returnBook(row.recordId, row.bookName)"
          >代还书</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty description="暂无借阅记录" v-if="filteredList.length === 0 && !loading" />
  </el-card>
</template>

<script setup>
import { ref, computed, onMounted, getCurrentInstance } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const { proxy } = getCurrentInstance()

const borrowList = ref([])
const loading = ref(false)
const statusFilter = ref('all')

const overdueCount = computed(() => {
  return borrowList.value.filter(item => item.isOverdue).length
})

const filteredList = computed(() => {
  if (statusFilter.value === 'all') {
    return borrowList.value
  } else if (statusFilter.value === 'overdue') {
    return borrowList.value.filter(item => item.isOverdue)
  } else if (statusFilter.value === 'warning') {
    return borrowList.value.filter(item => !item.isOverdue && item.daysRemaining <= 3 && item.daysRemaining > 0)
  }
  return borrowList.value
})

const formatDateTime = (dateStr) => {
  if (!dateStr) return '-'
  try {
    const date = new Date(dateStr)
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch (e) {
    return dateStr
  }
}

const getBorrowList = async () => {
  loading.value = true
  try {
    const res = await proxy.$axios.get('/borrow/not-returned/all')
    if (res.data.code === 200) {
      borrowList.value = res.data.data || []
    } else {
      ElMessage.error(res.data.msg || '获取借阅记录失败')
    }
  } catch (err) {
    ElMessage.error('网络异常，获取借阅记录失败')
  } finally {
    loading.value = false
  }
}

const returnBook = async (recordId, bookName) => {
  try {
    await ElMessageBox.confirm(`确定要为用户办理图书「${bookName}」的还书手续吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })
    const res = await proxy.$axios.put('/borrow/return', null, {
      params: { recordId }
    })
    if (res.data.code === 200) {
      ElMessage.success(res.data.msg || '还书成功')
      getBorrowList()
    } else {
      ElMessage.error(res.data.msg || '还书失败')
    }
  } catch (err) {
    if (err !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

onMounted(() => {
  getBorrowList()
})
</script>
