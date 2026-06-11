<template>
  <el-card shadow="hover" title="📚 图书列表" style="margin-top: 20px">
    <!-- 搜索栏 -->
    <div style="display: flex; gap: 10px; margin-bottom: 16px; align-items: center">
      <el-input
        v-model="keyword"
        placeholder="请输入书名/作者搜索"
        style="width: 300px"
        clearable
      />
      <el-button type="primary" icon="Search" @click="getBookList">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <!-- 图书表格 -->
    <el-table
      :data="bookList"
      border
      stripe
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column label="图书ID" prop="bookId" width="80" align="center" />
      <el-table-column label="图书条码" prop="bookBarcode" width="120" align="center" />
      <el-table-column label="图书名称" prop="bookName" align="center" />
      <el-table-column label="作者" prop="author" width="150" align="center" />
      <el-table-column label="图书分类" prop="category" width="120" align="center" />
      <el-table-column label="剩余库存" prop="remainStock" width="100" align="center" />
      <el-table-column label="操作" width="120" align="center">
        <template #default="{ row }">
          <el-button
            type="success"
            size="small"
            :disabled="row.remainStock <= 0 || btnLoading"
            @click="handleBorrow(row.bookBarcode)"
          >
            {{ row.remainStock <= 0 ? '暂无库存' : '立即借阅' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty description="暂无相关图书" v-if="bookList.length === 0 && !loading" />
  </el-card>
</template>

<script setup>
import { ref, onMounted, onActivated, getCurrentInstance } from 'vue'
import { ElMessage } from 'element-plus'

const { proxy } = getCurrentInstance()

const bookList = ref([])
const loading = ref(false)
const btnLoading = ref(false)
const keyword = ref('')

const getUserId = () => {
  const userInfoStr = localStorage.getItem('userInfo')
  if (!userInfoStr) {
    ElMessage.warning('请先登录账号')
    return null
  }
  try {
    const userInfo = JSON.parse(userInfoStr)
    return userInfo.userId
  } catch (e) {
    ElMessage.warning('用户信息异常，请重新登录')
    localStorage.removeItem('userInfo')
    return null
  }
}

const getBookList = async () => {
  loading.value = true
  try {
    const res = await proxy.$axios.get('/book/list', {
      params: { keyword: keyword.value }
    })
    if (res.data.code === 200) {
      bookList.value = res.data.data || []
    } else {
      ElMessage.error(res.data.msg || '获取图书列表失败')
    }
  } catch (err) {
    ElMessage.error('网络异常，请求图书数据失败')
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  keyword.value = ''
  getBookList()
}

const handleBorrow = async (bookBarcode) => {
  const uid = getUserId()
  if (!uid) return

  btnLoading.value = true
  try {
    const res = await proxy.$axios.post('/borrow/add', null, {
      params: { userId: uid, bookBarcode }
    })
    if (res.data.code === 200) {
      ElMessage.success('借阅成功！可前往我的借阅记录查看')
      getBookList()
    } else {
      ElMessage.error(res.data.msg || '借阅失败')
    }
  } catch (err) {
    ElMessage.error('网络异常，借阅请求失败')
  } finally {
    btnLoading.value = false
  }
}

onMounted(() => {
  getBookList()
})

onActivated(() => {
  getBookList()
})
</script>