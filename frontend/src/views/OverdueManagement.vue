<template>
  <el-card shadow="hover" title="⏰ 全馆借阅逾期管理" style="margin-top: 20px">
    <!-- 操作栏 -->
    <div style="display: flex; gap: 10px; margin-bottom: 16px; align-items: center">
      <el-button type="primary" icon="Refresh" @click="getOverdueList">刷新数据</el-button>
      <el-button type="danger" icon="Bell" :disabled="selectedRecords.length === 0" @click="batchRemind">批量催还</el-button>
      <span style="color: #f56c6c; margin-left: 10px;">逾期总数：{{ overdueList.length }} 条</span>
    </div>

    <!-- 逾期记录表格 -->
    <el-table
      :data="overdueList"
      border
      stripe
      style="width: 100%"
      v-loading="loading"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="记录ID" prop="recordId" width="80" align="center" />
      <el-table-column label="图书名称" prop="bookName" />
      <el-table-column label="作者" prop="author" width="120" />
      <el-table-column label="借阅人" prop="userName" width="100" />
      <el-table-column label="借阅账号" prop="userAccount" width="120" />
      <el-table-column label="借阅时间" prop="borrowTime" width="180" />
      <el-table-column label="逾期天数" prop="overdueDays" width="100" align="center">
        <template #default="{ row }">
          <el-tag type="danger">{{ row.overdueDays }} 天</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="逾期罚款" prop="fineMoney" width="120" align="center">
        <template #default="{ row }">
          <span style="color: #f56c6c;">¥{{ row.fineMoney }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" align="center">
        <template #default="{ row }">
          <el-button type="warning" size="small" @click="remindSingle(row.recordId)">催还</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty description="暂无逾期记录" v-if="overdueList.length === 0 && !loading" />
  </el-card>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const { proxy } = getCurrentInstance()

const overdueList = ref([])
const loading = ref(false)
const selectedRecords = ref([])

const getOverdueList = async () => {
  loading.value = true
  try {
    const res = await proxy.$axios.get('/borrow/overdue/all')
    if (res.data.code === 200) {
      overdueList.value = res.data.data || []
    } else {
      ElMessage.error(res.data.msg || '获取逾期记录失败')
    }
  } catch (err) {
    ElMessage.error('网络异常，获取逾期记录失败')
  } finally {
    loading.value = false
  }
}

const handleSelectionChange = (val) => {
  selectedRecords.value = val.map(item => item.recordId)
}

const remindSingle = async (recordId) => {
  try {
    await ElMessageBox.confirm('确定要发送催还提醒吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })
    const res = await proxy.$axios.post('/borrow/overdue/remind', [recordId])
    if (res.data.code === 200) {
      ElMessage.success('催还提醒已发送')
    } else {
      ElMessage.error(res.data.msg || '发送失败')
    }
  } catch (err) {
    if (err !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const batchRemind = async () => {
  try {
    await ElMessageBox.confirm(`确定要向选中的 ${selectedRecords.value.length} 位用户发送催还提醒吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })
    const res = await proxy.$axios.post('/borrow/overdue/remind', selectedRecords.value)
    if (res.data.code === 200) {
      ElMessage.success('批量催还提醒已发送')
      selectedRecords.value = []
    } else {
      ElMessage.error(res.data.msg || '发送失败')
    }
  } catch (err) {
    if (err !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

onMounted(() => {
  getOverdueList()
})
</script>
