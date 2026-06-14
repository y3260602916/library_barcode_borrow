import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },
  server: {
    port: 5173,
    proxy: {
      '/user': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/book': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/borrow': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/inventory': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/stats': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})