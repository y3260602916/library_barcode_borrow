import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import BarcodeOcr from '@/views/BarcodeOcr.vue'
import BookList from '@/views/BookList.vue'
import BorrowRecord from '@/views/BorrowRecord.vue'
import DataChart from '@/views/DataChart.vue'
import UserManagement from '@/views/UserManagement.vue'
import InventoryManagement from '@/views/InventoryManagement.vue'
import StockManagement from '@/views/StockManagement.vue'
import OverdueManagement from '@/views/OverdueManagement.vue'
import AdminStats from '@/views/AdminStats.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  // 师生功能
  { path: '/ocr', component: BarcodeOcr, meta: { requiresAuth: true, requireAdmin: false } },
  { path: '/bookList', component: BookList, meta: { requiresAuth: true, requireAdmin: false } },
  { path: '/borrowRecord', component: BorrowRecord, meta: { requiresAuth: true, requireAdmin: false } },
  { path: '/chart', component: DataChart, meta: { requiresAuth: true, requireAdmin: false } },
  // 管理员功能
  { path: '/admin/users', component: UserManagement, meta: { requiresAuth: true, requireAdmin: true } },
  { path: '/admin/inventory', component: InventoryManagement, meta: { requiresAuth: true, requireAdmin: true } },
  { path: '/admin/stock', component: StockManagement, meta: { requiresAuth: true, requireAdmin: true } },
  { path: '/admin/overdue', component: OverdueManagement, meta: { requiresAuth: true, requireAdmin: true } },
  { path: '/admin/stats', component: AdminStats, meta: { requiresAuth: true, requireAdmin: true } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局登录拦截和权限验证
router.beforeEach((to, from, next) => {
  const needLogin = to.matched.some(item => item.meta.requiresAuth)
  const needAdmin = to.matched.some(item => item.meta.requireAdmin)
  const userInfo = localStorage.getItem('userInfo')

  // 需要登录但未登录
  if (needLogin && !userInfo) {
    next('/login')
    return
  }

  // 需要管理员权限验证
  if (needAdmin && userInfo) {
    const user = JSON.parse(userInfo)
    if (user.userType !== 1) {
      // 非管理员尝试访问管理员页面，跳转回首页
      next('/ocr')
      return
    }
  }

  next()
})

export default router