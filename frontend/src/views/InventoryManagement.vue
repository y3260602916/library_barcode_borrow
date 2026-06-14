<template>
  <el-card shadow="hover" title="📦 图书出入库管理" style="margin-top: 20px">
    <!-- 选项卡 -->
    <el-tabs v-model="activeTab" style="margin-bottom: 16px">
      <el-tab-pane label="入库管理" name="stockIn">
        <div style="display: flex; gap: 10px; margin-bottom: 16px; flex-wrap: wrap;">
          <el-input v-model="stockInForm.bookId" placeholder="图书ID（已有图书）" style="width: 150px" />
          <el-input v-model="stockInForm.bookBarcode" placeholder="图书条码" style="width: 150px" />
          <el-input v-model="stockInForm.bookName" placeholder="图书名称（新增图书必填）" style="width: 180px" />
          <el-input v-model="stockInForm.author" placeholder="作者" style="width: 120px" />
          <el-input v-model="stockInForm.category" placeholder="分类" style="width: 100px" />
          <el-input v-model="stockInForm.price" placeholder="价格" style="width: 100px" />
          <el-input v-model="stockInForm.quantity" type="number" placeholder="入库数量" style="width: 120px" />
          <el-input v-model="stockInForm.remark" placeholder="备注" style="width: 200px" />
          <el-button type="success" icon="Plus" @click="handleStockIn">入库</el-button>
        </div>
        <div style="color: #999; font-size: 12px; margin-bottom: 16px;">
          <strong>提示：</strong>
          如果图书已存在，填写图书ID或条码即可；如果是新书入库，请填写条码、图书名称（必填），作者、分类、价格（选填）
        </div>
        <el-table :data="stockInRecords" border stripe style="width: 100%">
          <el-table-column label="记录ID" prop="recordId" width="80" />
          <el-table-column label="图书ID" prop="bookId" width="80" />
          <el-table-column label="图书名称" prop="bookName" />
          <el-table-column label="数量" prop="quantity" width="80" align="center" />
          <el-table-column label="操作人" prop="operator" width="100" />
          <el-table-column label="操作时间" prop="createTime" width="180" />
          <el-table-column label="备注" prop="remark" />
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="出库管理" name="stockOut">
        <div style="display: flex; gap: 10px; margin-bottom: 16px;">
          <el-input v-model="stockOutForm.bookId" placeholder="图书ID" style="width: 150px" />
          <el-input v-model="stockOutForm.bookBarcode" placeholder="图书条码" style="width: 150px" />
          <el-input v-model="stockOutForm.quantity" type="number" placeholder="出库数量" style="width: 120px" />
          <el-input v-model="stockOutForm.remark" placeholder="备注" style="width: 200px" />
          <el-button type="danger" icon="Minus" @click="handleStockOut">出库</el-button>
        </div>
        <el-table :data="stockOutRecords" border stripe style="width: 100%">
          <el-table-column label="记录ID" prop="recordId" width="80" />
          <el-table-column label="图书ID" prop="bookId" width="80" />
          <el-table-column label="图书名称" prop="bookName" />
          <el-table-column label="数量" prop="quantity" width="80" align="center" />
          <el-table-column label="操作人" prop="operator" width="100" />
          <el-table-column label="操作时间" prop="createTime" width="180" />
          <el-table-column label="备注" prop="remark" />
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="出入库流水" name="records">
        <el-table :data="allRecords" border stripe style="width: 100%">
          <el-table-column label="记录ID" prop="recordId" width="80" />
          <el-table-column label="类型" prop="type" width="80">
            <template #default="{ row }">
              <el-tag :type="row.type === 'IN' ? 'success' : 'danger'">
                {{ row.type === 'IN' ? '入库' : '出库' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="图书ID" prop="bookId" width="80" />
          <el-table-column label="图书名称" prop="bookName" />
          <el-table-column label="数量" prop="quantity" width="80" align="center" />
          <el-table-column label="操作人" prop="operator" width="100" />
          <el-table-column label="操作时间" prop="createTime" width="180" />
          <el-table-column label="备注" prop="remark" />
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue'
import { ElMessage } from 'element-plus'

const { proxy } = getCurrentInstance()

const activeTab = ref('stockIn')
const stockInForm = ref({ bookId: '', bookBarcode: '', bookName: '', author: '', category: '', price: '', quantity: 1, remark: '' })
const stockOutForm = ref({ bookId: '', bookBarcode: '', quantity: 1, remark: '' })
const stockInRecords = ref([])
const stockOutRecords = ref([])
const allRecords = ref([])

const getOperator = () => {
  const userInfo = localStorage.getItem('userInfo')
  if (userInfo) {
    return JSON.parse(userInfo).userName
  }
  return '管理员'
}

const handleStockIn = async () => {
  // 如果没有图书ID，检查是否有图书条码（新增图书必须有）
  if (!stockInForm.value.bookId && !stockInForm.value.bookBarcode) {
    ElMessage.warning('请输入图书ID或条码')
    return
  }
  // 如果是新增图书（没有bookId），必须填写图书名称
  if (!stockInForm.value.bookId && !stockInForm.value.bookName) {
    ElMessage.warning('新增图书入库时，请填写图书名称')
    return
  }
  if (!stockInForm.value.quantity || stockInForm.value.quantity <= 0) {
    ElMessage.warning('请输入有效数量')
    return
  }
  try {
    const params = {
      bookId: stockInForm.value.bookId || null,
      bookBarcode: stockInForm.value.bookBarcode || null,
      bookName: stockInForm.value.bookName || null,
      author: stockInForm.value.author || null,
      category: stockInForm.value.category || null,
      price: stockInForm.value.price || null,
      quantity: stockInForm.value.quantity,
      operator: getOperator(),
      remark: stockInForm.value.remark
    }
    const res = await proxy.$axios.post('/inventory/stock-in', params)
    if (res.data.code === 200) {
      ElMessage.success(res.data.msg || '入库成功')
      loadRecords()
      stockInForm.value = { bookId: '', bookBarcode: '', bookName: '', author: '', category: '', price: '', quantity: 1, remark: '' }
    } else {
      ElMessage.error(res.data.msg || '入库失败')
    }
  } catch (err) {
    ElMessage.error('网络异常，入库失败')
  }
}

const handleStockOut = async () => {
  if (!stockOutForm.value.bookId && !stockOutForm.value.bookBarcode) {
    ElMessage.warning('请输入图书ID或条码')
    return
  }
  if (!stockOutForm.value.quantity || stockOutForm.value.quantity <= 0) {
    ElMessage.warning('请输入有效数量')
    return
  }
  try {
    const params = {
      bookId: stockOutForm.value.bookId || null,
      bookBarcode: stockOutForm.value.bookBarcode || null,
      quantity: stockOutForm.value.quantity,
      operator: getOperator(),
      remark: stockOutForm.value.remark
    }
    const res = await proxy.$axios.post('/inventory/stock-out', params)
    if (res.data.code === 200) {
      ElMessage.success('出库成功')
      loadRecords()
      stockOutForm.value = { bookId: '', bookBarcode: '', quantity: 1, remark: '' }
    } else {
      ElMessage.error(res.data.msg || '出库失败')
    }
  } catch (err) {
    ElMessage.error('网络异常，出库失败')
  }
}

const loadRecords = async () => {
  try {
    const res = await proxy.$axios.get('/inventory/records')
    if (res.data.code === 200) {
      const records = res.data.data || []
      stockInRecords.value = records.filter(r => r.type === 'IN')
      stockOutRecords.value = records.filter(r => r.type === 'OUT')
      allRecords.value = records
    }
  } catch (err) {
    ElMessage.error('获取出入库记录失败')
  }
}

onMounted(() => {
  loadRecords()
})
</script>
