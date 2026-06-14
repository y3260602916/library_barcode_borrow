<template>
  <el-card shadow="hover" title="📚 全馆图书库存管理" style="margin-top: 20px">
    <!-- 搜索栏 -->
    <div style="display: flex; gap: 10px; margin-bottom: 16px; align-items: center">
      <el-input
        v-model="keyword"
        placeholder="请输入书名/作者搜索"
        style="width: 300px"
        clearable
      />
      <el-button type="primary" icon="Search" @click="getStockList">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <!-- 库存表格 -->
    <el-table
      :data="stockList"
      border
      stripe
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column label="图书ID" prop="bookId" width="80" align="center" />
      <el-table-column label="图书条码" prop="bookBarcode" width="120" align="center" />
      <el-table-column label="图书名称" prop="bookName" />
      <el-table-column label="作者" prop="author" width="150" />
      <el-table-column label="分类" prop="category" width="100" />
      <el-table-column label="总库存" prop="totalStock" width="100" align="center" />
      <el-table-column label="已借出" prop="borrowedCount" width="100" align="center" />
      <el-table-column label="剩余库存" prop="remainStock" width="100" align="center" />
      <el-table-column label="操作" width="150" align="center">
        <template #default="{ row }">
          <el-button type="warning" size="small" @click="openAdjustModal(row)">调整库存</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty description="暂无库存数据" v-if="stockList.length === 0 && !loading" />

    <!-- 调整库存弹窗 -->
    <el-dialog title="调整库存" v-model="showAdjustModal" width="400px">
      <el-form ref="adjustFormRef" :model="adjustForm" label-width="80px">
        <el-form-item label="图书名称">
          <el-input :value="adjustForm.bookName" disabled />
        </el-form-item>
        <el-form-item label="当前库存">
          <el-input :value="adjustForm.currentStock" disabled />
        </el-form-item>
        <el-form-item label="新库存数量" :required="true">
          <el-input v-model="adjustForm.newStock" type="number" placeholder="请输入新库存数量" />
        </el-form-item>
        <el-form-item label="调整原因">
          <el-input v-model="adjustForm.remark" placeholder="请输入调整原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAdjustModal = false">取消</el-button>
        <el-button type="primary" @click="adjustStock">确定调整</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue'
import { ElMessage } from 'element-plus'

const { proxy } = getCurrentInstance()

const stockList = ref([])
const loading = ref(false)
const keyword = ref('')
const showAdjustModal = ref(false)
const adjustForm = ref({
  bookId: null,
  bookName: '',
  currentStock: 0,
  newStock: 0,
  remark: ''
})

const getStockList = async () => {
  loading.value = true
  try {
    const params = keyword.value ? { keyword: keyword.value } : {}
    const res = await proxy.$axios.get('/book/stock', { params })
    if (res.data.code === 200) {
      stockList.value = res.data.data || []
    } else {
      ElMessage.error(res.data.msg || '获取库存失败')
    }
  } catch (err) {
    ElMessage.error('网络异常，获取库存失败')
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  keyword.value = ''
  getStockList()
}

const openAdjustModal = (row) => {
  adjustForm.value = {
    bookId: row.bookId,
    bookName: row.bookName,
    currentStock: row.totalStock,
    newStock: row.totalStock,
    remark: ''
  }
  showAdjustModal.value = true
}

const adjustStock = async () => {
  if (!adjustForm.value.newStock || adjustForm.value.newStock < 0) {
    ElMessage.warning('请输入有效的库存数量')
    return
  }
  try {
    const res = await proxy.$axios.put('/book/adjust-stock', {
      bookId: adjustForm.value.bookId,
      newStock: adjustForm.value.newStock,
      remark: adjustForm.value.remark
    })
    if (res.data.code === 200) {
      ElMessage.success('库存调整成功')
      showAdjustModal.value = false
      getStockList()
    } else {
      ElMessage.error(res.data.msg || '调整失败')
    }
  } catch (err) {
    ElMessage.error('网络异常，调整失败')
  }
}

onMounted(() => {
  getStockList()
})
</script>
