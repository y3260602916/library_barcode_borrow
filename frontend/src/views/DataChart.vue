<template>
  <el-card shadow="hover" title="📊 数据统计 & AI智能推荐" style="margin: 20px;">
    <!-- 1. 顶部统计卡片 -->
    <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 24px;">
      <el-statistic title="馆藏图书种类" :value="stats.bookTypes" suffix="种" />
      <el-statistic title="馆藏图书总量" :value="stats.totalStock" suffix="本" />
      <el-statistic title="未归还图书" :value="stats.unreturnedBooks" suffix="本" />
      <el-statistic title="我的借阅数" :value="stats.userBorrowCount" suffix="本" />
    </div>

    <!-- 2. 双图表区域 -->
    <div style="display: flex; gap: 20px; margin-bottom: 24px;">
      <div style="flex: 1; height: 300px;">
        <h4>📈 近7天借阅趋势</h4>
        <div ref="trendChartRef" style="width: 100%; height: 250px;"></div>
      </div>
      <div style="flex: 1; height: 300px;">
        <h4>📊 馆藏图书分类占比</h4>
        <div ref="pieChartRef" style="width: 100%; height: 250px;"></div>
      </div>
    </div>

    <!-- 3. 常规推荐列表 -->
    <div style="margin: 30px 0;">
      <h4>📖 常规推荐（基于借阅分类统计）</h4>
      <el-table
        :data="normalRecommendList"
        border
        stripe
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column prop="bookName" label="图书名称" />
        <el-table-column prop="author" label="作者" />
        <el-table-column prop="category" label="图书分类" />
        <el-table-column prop="remainStock" label="剩余库存" align="center" />
        <el-table-column label="操作" width="100" align="center">
          <template #default="{ row }">
            <el-button
              type="success"
              size="small"
              :disabled="row.remainStock <= 0"
              @click="handleBorrow(row.bookBarcode)"
            >
              借阅
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty
        v-if="!loading && normalRecommendList.length === 0"
        description="暂无推荐图书"
      />
    </div>

    <!-- 4. AI智能推荐列表 -->
    <div v-if="showAiRecommend" style="margin: 30px 0;">
      <h4>🤖 AI智能推荐（Python端大模型分析）</h4>
      <el-table
        :data="aiRecommendList"
        border
        stripe
        v-loading="aiLoading"
        style="width: 100%"
      >
        <el-table-column prop="bookName" label="图书名称" />
        <el-table-column prop="author" label="作者" />
        <el-table-column prop="category" label="图书分类" />
        <el-table-column prop="remainStock" label="剩余库存" align="center" />
        <el-table-column prop="reason" label="AI推荐理由" width="300" />
        <el-table-column label="操作" width="100" align="center">
          <template #default="{ row }">
            <el-button
              type="success"
              size="small"
              :disabled="row.remainStock <= 0"
              @click="handleAiBorrow(row)"
            >
              借阅
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty
        v-if="!aiLoading && aiRecommendList.length === 0"
        description="AI暂无推荐内容"
      />
    </div>
    <!-- AI推荐模块隐藏时的提示 -->
    <div v-else style="margin: 30px 0; padding: 20px; background: #f9f9f9; border-radius: 8px; text-align: center;">
      <el-empty description="📚 借阅记录不足，暂无法提供AI个性化推荐（建议借阅3本以上图书）" />
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted, onUnmounted, getCurrentInstance } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const { proxy } = getCurrentInstance()
const axios = proxy.$axios

// 图表DOM
const trendChartRef = ref(null)
const pieChartRef = ref(null)
let trendChart = null
let pieChart = null

// 页面数据
const stats = ref({
  bookTypes: 0,
  totalStock: 0,
  unreturnedBooks: 0,
  userBorrowCount: 0
})
const normalRecommendList = ref([])
const aiRecommendList = ref([])
const loading = ref(false)
const aiLoading = ref(false)
const pageLoading = ref(false) // 全局请求锁
const showAiRecommend = ref(false) // 是否显示AI推荐模块
let userId = ''

// 借阅记录阈值，超过该值才显示AI推荐
const AI_RECOMMEND_THRESHOLD = 3

// 从本地存储获取登录用户ID
const getUserId = () => {
  const userInfoStr = localStorage.getItem('userInfo')
  if (!userInfoStr) {
    console.log('[DEBUG] localStorage 中没有 userInfo')
    return null
  }
  try {
    const userInfo = JSON.parse(userInfoStr)
    console.log('[DEBUG] 获取到的用户信息:', userInfo)
    console.log('[DEBUG] 用户ID:', userInfo.userId)
    return userInfo.userId
  } catch (e) {
    console.log('[DEBUG] 解析用户信息失败:', e)
    return null
  }
}

// 初始化双图表（加强空数据容错）
const initEcharts = (trendData, categoryData) => {
  // 兜底空数组，彻底防止报错
  const days = trendData?.days ?? []
  const counts = trendData?.counts ?? []
  const categoryList = categoryData ?? []

  // 折线趋势图
  if (trendChart) {
    trendChart.dispose()
  }
  trendChart = echarts.init(trendChartRef.value)
  trendChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: days
    },
    yAxis: { type: 'value' },
    series: [
      {
        name: '借阅量',
        type: 'line',
        smooth: true,
        data: counts
      }
    ]
  })

  // 饼图分类占比
  if (pieChart) {
    pieChart.dispose()
  }
  pieChart = echarts.init(pieChartRef.value)
  pieChart.setOption({
    tooltip: { trigger: 'item' },
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        data: categoryList
      }
    ]
  })
}

// 常规推荐借阅操作
const handleBorrow = async (bookBarcode) => {
  try {
    const res = await axios.post('/borrow/add', null, {
      params: { userId, bookBarcode }
    })
    if (res.data.code === 200) {
      ElMessage.success('借阅成功')
      loadAllData()
    }
  } catch (err) {
    ElMessage.error('借阅操作失败')
  }
}

// AI推荐借阅操作（需要先通过书名查询图书条码）
const handleAiBorrow = async (row) => {
  try {
    // 优先使用条码
    if (row.bookBarcode) {
      await handleBorrow(row.bookBarcode)
      return
    }
    
    // 如果没有条码，通过书名查询图书信息
    const searchRes = await axios.get('/book/search', {
      params: { keyword: row.bookName }
    })
    
    if (searchRes.data.code === 200 && searchRes.data.data && searchRes.data.data.length > 0) {
      const book = searchRes.data.data[0]
      await handleBorrow(book.bookBarcode)
    } else {
      ElMessage.warning('未找到该图书')
    }
  } catch (err) {
    ElMessage.error('借阅操作失败')
  }
}

// 加载所有接口数据
const loadAllData = async () => {
  if (pageLoading.value) return
  pageLoading.value = true

  userId = getUserId()
  if (!userId) {
    ElMessage.warning('请先登录账号')
    pageLoading.value = false
    return
  }
  userId = Number(userId)

  // 1. 加载基础统计数据
    try {
      const statsRes = await axios.get('/stats/overview', {
        params: { userId }
      })
      if (statsRes.data && statsRes.data.code === 200 && statsRes.data.data) {
        stats.value = {
          bookTypes: statsRes.data.data.bookTypes || 0,
          totalStock: statsRes.data.data.totalStock || 0,
          unreturnedBooks: statsRes.data.data.unreturnedBooks || 0,
          userBorrowCount: statsRes.data.data.userBorrowCount || 0
        }
      }
    } catch (err) {
      ElMessage.error('统计数据加载失败')
    }

  // 2. 加载图表数据（增加多层判断）
  try {
    const chartRes = await axios.get('/stats/charts')
    if (chartRes.data && chartRes.data.code === 200 && chartRes.data.data) {
      const data = chartRes.data.data
      const trend = data.trend || {}
      const category = data.category || []
      // 确保数据格式正确
      const trendData = {
        days: Array.isArray(trend.days) ? trend.days : [],
        counts: Array.isArray(trend.counts) ? trend.counts : []
      }
      initEcharts(trendData, category)
    }
  } catch (err) {
    ElMessage.error('图表数据加载失败')
  }

  // 3. 加载常规推荐
  loading.value = true
  try {
    const normalRes = await axios.get('/stats/recommend', { params: { userId } })
    if (normalRes.data && normalRes.data.code === 200) {
      normalRecommendList.value = Array.isArray(normalRes.data.data) ? normalRes.data.data : []
    }
  } catch (err) {
    ElMessage.error('常规推荐加载失败')
  } finally {
    loading.value = false
  }

  // 4. 加载AI推荐（仅当借阅记录足够时）
  showAiRecommend.value = stats.value.userBorrowCount >= AI_RECOMMEND_THRESHOLD
  
  if (showAiRecommend.value) {
    aiLoading.value = true
    try {
      const aiRes = await axios.get('/stats/ai-recommend', { params: { userId } })
      if (aiRes.data && aiRes.data.code === 200) {
        aiRecommendList.value = Array.isArray(aiRes.data.data) ? aiRes.data.data : []
      }
    } catch (err) {
      ElMessage.error('AI推荐服务调用异常')
    } finally {
      aiLoading.value = false
    }
  }
  
  pageLoading.value = false
}

// 窗口自适应
const resizeChart = () => {
  trendChart?.resize()
  pieChart?.resize()
}

onMounted(() => {
  loadAllData()
  window.addEventListener('resize', resizeChart)
})

// 销毁实例，释放资源
onUnmounted(() => {
  window.removeEventListener('resize', resizeChart)
  if (trendChart) trendChart.dispose()
  if (pieChart) pieChart.dispose()
})
</script>