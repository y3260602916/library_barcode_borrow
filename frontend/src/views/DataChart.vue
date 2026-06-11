<template>
  <el-card shadow="hover" title="📊 图书馆数据统计">
    <div ref="chartDom" style="width: 100%; height: 360px;"></div>
  </el-card>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'

const chartDom = ref(null)
let myChart = null

const initChart = () => {
  myChart = echarts.init(chartDom.value)
  const option = {
    title: { text: '月度借阅量统计' },
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月']
    },
    yAxis: { type: 'value' },
    series: [
      {
        name: '借阅次数',
        type: 'bar',
        data: [120, 200, 150, 80, 230, 180]
      }
    ]
  }
  myChart.setOption(option)
}

onMounted(() => {
  initChart()
  window.addEventListener('resize', () => myChart?.resize())
})

onUnmounted(() => {
  window.removeEventListener('resize', () => myChart?.resize())
  myChart?.dispose()
})
</script>