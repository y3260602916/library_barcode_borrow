<template>
  <el-card shadow="hover" title="📊 借阅数据统计分析" style="margin: 20px;">
    <!-- 时间筛选 -->
    <div style="display: flex; gap: 10px; margin-bottom: 24px; align-items: center">
      <el-date-picker v-model="startDate" type="date" placeholder="开始日期" />
      <span>至</span>
      <el-date-picker v-model="endDate" type="date" placeholder="结束日期" />
      <el-button type="primary" icon="Search" @click="loadStats">查询统计</el-button>
    </div>

    <!-- 统计卡片 -->
    <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 24px;">
      <el-statistic title="借阅总量" :value="stats.totalBorrowCount" suffix="次" />
      <el-statistic title="已归还" :value="stats.returnedCount" suffix="次" />
      <el-statistic title="未归还" :value="stats.notReturnedCount" suffix="次" />
      <el-statistic title="逾期数量" :value="stats.overdueCount" suffix="条" />
    </div>

    <!-- 借阅趋势图 -->
    <div style="height: 300px; margin-bottom: 24px;">
      <h4>📈 借阅趋势（按日期）</h4>
      <div ref="trendChartRef" style="width: 100%; height: 250px;"></div>
    </div>

    <!-- 分类借阅统计 -->
    <div style="height: 300px; margin-bottom: 24px;">
      <h4>📊 分类借阅统计</h4>
      <div ref="categoryChartRef" style="width: 100%; height: 250px;"></div>
    </div>

    <!-- 热门图书排行 -->
    <div style="margin: 30px 0;">
      <h4>🔥 热门图书排行（进货参考）</h4>
      <el-table
        :data="hotBooks"
        border
        stripe
        v-loading="hotBooksLoading"
        style="width: 100%"
      >
        <el-table-column label="排名" width="80" align="center">
          <template #default="{ $index }">
            <el-tag v-if="$index < 3" :type="['danger', 'warning', 'success'][$index]">
              {{ $index + 1 }}
            </el-tag>
            <span v-else>{{ $index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="bookName" label="图书名称" />
        <el-table-column prop="author" label="作者" />
        <el-table-column prop="category" label="分类" />
        <el-table-column prop="borrowCount" label="借阅次数" align="center" />
        <el-table-column prop="totalStock" label="总库存" align="center" />
        <el-table-column prop="remainStock" label="剩余库存" align="center" />
        <el-table-column label="进货建议" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="row.remainStock < 2 ? 'danger' : 'success'">
              {{ row.remainStock < 2 ? '建议补货' : '库存充足' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted, onUnmounted, getCurrentInstance } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const { proxy } = getCurrentInstance()
const axios = proxy.$axios

const trendChartRef = ref(null)
const categoryChartRef = ref(null)
let trendChart = null
let categoryChart = null

const startDate = ref('')
const endDate = ref('')
const stats = ref({
  totalBorrowCount: 0,
  returnedCount: 0,
  notReturnedCount: 0,
  overdueCount: 0
})
const hotBooks = ref([])
const hotBooksLoading = ref(false)

const initTrendChart = (data) => {
  if (trendChart) trendChart.dispose()
  trendChart = echarts.init(trendChartRef.value)
  
  const dates = data.map(item => item.date)
  const counts = data.map(item => item.count)
  
  trendChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: dates },
    yAxis: { type: 'value' },
    series: [{ name: '借阅量', type: 'line', smooth: true, data: counts }]
  })
}

const initCategoryChart = (data) => {
  if (categoryChart) categoryChart.dispose()
  categoryChart = echarts.init(categoryChartRef.value)
  
  const categoryList = []
  Object.keys(data).forEach(key => {
    categoryList.push({ name: key, value: data[key] })
  })
  
  categoryChart.setOption({
    tooltip: { trigger: 'item' },
    series: [{ type: 'pie', radius: ['40%', '70%'], data: categoryList }]
  })
}

const loadStats = async () => {
  try {
    // 加载借阅统计
    const statsRes = await axios.get('/stats/borrow-stat', {
      params: { startDate: startDate.value, endDate: endDate.value }
    })
    if (statsRes.data.code === 200) {
      stats.value = statsRes.data.data || {
        totalBorrowCount: 0,
        returnedCount: 0,
        notReturnedCount: 0,
        overdueCount: 0
      }
      
      // 加载分类统计图表
      if (stats.value.categoryBorrowCount) {
        initCategoryChart(stats.value.categoryBorrowCount)
      }
    }

    // 加载借阅趋势
    const trendRes = await axios.get('/stats/borrow-trend', {
      params: { startDate: startDate.value, endDate: endDate.value }
    })
    if (trendRes.data.code === 200) {
      const trendData = trendRes.data.data || []
      initTrendChart(trendData)
    }

    // 加载热门图书
    hotBooksLoading.value = true
    const hotBooksRes = await axios.get('/stats/hot-books', { params: { limit: 10 } })
    if (hotBooksRes.data.code === 200) {
      hotBooks.value = hotBooksRes.data.data || []
    }
    hotBooksLoading.value = false
  } catch (err) {
    ElMessage.error('统计数据加载失败')
    hotBooksLoading.value = false
  }
}

const resizeChart = () => {
  trendChart?.resize()
  categoryChart?.resize()
}

onMounted(() => {
  loadStats()
  window.addEventListener('resize', resizeChart)
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeChart)
  if (trendChart) trendChart.dispose()
  if (categoryChart) categoryChart.dispose()
})
</script>
