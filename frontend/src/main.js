import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from 'axios'
// 引入路由配置
import router from './router'

const app = createApp(App)

// 配置axios
axios.defaults.baseURL = 'http://localhost:8080'
axios.defaults.timeout = 10000

// 全局挂载axios
app.config.globalProperties.$axios = axios

// 挂载插件：ElementPlus 和 router
app.use(ElementPlus)
app.use(router)

app.mount('#app')