import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import BarcodeOcr from '@/views/BarcodeOcr.vue'
import BookList from '@/views/BookList.vue'
import BorrowRecord from '@/views/BorrowRecord.vue'
import DataChart from '@/views/DataChart.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/ocr', component: BarcodeOcr },
  { path: '/bookList', component: BookList },
  { path: '/borrowRecord', component: BorrowRecord },
  { path: '/chart', component: DataChart }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router