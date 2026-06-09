<template>
  <el-card shadow="hover" title="📷 自助借还 - 摄像头拍照识码">
    <div style="color:#666; margin-bottom:16px;">
      操作指引：将图书条码对准蓝色取景框，调整清晰后点击【拍照识别】
    </div>

    <!-- 1. 摄像头预览 + 取景框 -->
    <div class="camera-wrapper" style="position: relative; width: 480px; height: 360px; border: 1px solid #ccc; border-radius: 4px; overflow: hidden; margin-bottom: 16px;">
      <video 
        ref="videoRef" 
        autoplay 
        playsinline 
        muted
        style="width: 100%; height: 100%; object-fit: cover;"
      ></video>
      <!-- 取景框标线 -->
      <div class="scan-frame"></div>
      <canvas ref="canvasRef" style="display: none;"></canvas>
    </div>

    <!-- 2. 拍摄结果预览（新增） -->
    <div style="margin-bottom: 16px;" v-if="capturedImage">
      <p style="font-size:14px; color:#666;">拍摄预览：</p>
      <img :src="capturedImage" alt="拍摄的条码" style="width: 240px; border:1px solid #ccc; border-radius:4px;">
    </div>

    <!-- 3. 控制按钮 -->
    <div style="margin-bottom: 16px; display: flex; gap: 12px;">
      <el-button type="primary" @click="openCamera" :disabled="cameraStatus">开启摄像头</el-button>
      <el-button type="danger" @click="closeCamera" :disabled="!cameraStatus">关闭摄像头</el-button>
      <el-button type="success" @click="takePhoto" :disabled="!cameraStatus">拍照识别</el-button>
      <el-button type="info" @click="resetPreview">重新拍摄</el-button>
    </div>

    <!-- 4. 条码结果 + 图书信息（不变） -->
    <el-input
      v-model="barcode"
      placeholder="识别后的图书编码，也可手动输入"
      clearable
      style="width: 320px; margin-bottom: 16px"
    >
      <template #append>
        <el-button @click="queryBookInfo">查询图书</el-button>
      </template>
    </el-input>

    <el-card v-if="bookInfo" shadow="hover" title="📚 图书信息" style="margin-bottom: 16px">
      <p><strong>书名：</strong>{{ bookInfo.bookName }}</p>
      <p><strong>作者：</strong>{{ bookInfo.author }}</p>
      <p><strong>分类：</strong>{{ bookInfo.category }}</p>
      <p><strong>总库存：</strong>{{ bookInfo.totalStock }}</p>
      <p><strong>剩余可借：</strong>{{ bookInfo.remainStock }}</p>
      <el-tag :type="bookInfo.remainStock > 0 ? 'success' : 'danger'">
        {{ bookInfo.remainStock > 0 ? '可借阅' : '库存不足' }}
      </el-tag>
    </el-card>

    <div>
      <el-button
        type="success"
        :disabled="!canBorrow"
        @click="handleBorrow"
      >
        办理图书借阅
      </el-button>
      <el-text style="margin-left: 20px; color: #999;">
        归还图书请前往【我的借阅记录】页面操作
      </el-text>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { getCurrentInstance } from 'vue'
const { proxy } = getCurrentInstance()

// ========== 摄像头/拍照变量 ==========
const videoRef = ref(null)
const canvasRef = ref(null)
const cameraStatus = ref(false)
let mediaStream = null
const capturedImage = ref('') // 存储拍摄的图片预览（base64）

// ========== 业务变量 ==========
const barcode = ref('')
const bookInfo = ref(null)
const userId = ref(null)

onMounted(() => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    userId.value = JSON.parse(user).userId
  }
})

onUnmounted(() => {
  closeCamera()
})

// 开启摄像头
const openCamera = async () => {
  try {
    mediaStream = await navigator.mediaDevices.getUserMedia({
      video: {
        width: { ideal: 1280, min: 640 },
        height: { ideal: 720, min: 480 },
        facingMode: 'environment'
      },
      audio: false
    })
    videoRef.value.srcObject = mediaStream
    cameraStatus.value = true
    ElMessage.success('摄像头开启成功，请对准图书条码')
  } catch (err) {
    ElMessage.error('摄像头调用失败，请检查设备权限或浏览器设置')
  }
}

// 关闭摄像头
const closeCamera = () => {
  if (mediaStream) {
    mediaStream.getTracks().forEach(track => track.stop())
    mediaStream = null
    videoRef.value.srcObject = null
    cameraStatus.value = false
  }
}

// 拍照 + 定格画面 + 显示预览
const takePhoto = () => {
  const video = videoRef.value
  const canvas = canvasRef.value
  const ctx = canvas.getContext('2d')

  // 1. 定格画面：先暂停视频
  video.pause()

  // 2. 绘制到Canvas
  canvas.width = video.videoWidth
  canvas.height = video.videoHeight
  ctx.drawImage(video, 0, 0, canvas.width, canvas.height)

  // 3. 转为Base64，用于预览
  capturedImage.value = canvas.toDataURL('image/png')

  // 4. 转为File上传
  canvas.toBlob((blob) => {
    if (!blob) return
    const file = new File([blob], "barcode_photo.png", { type: "image/png" })
    uploadPhoto(file)
  }, 'image/png')
}

// 重置预览，恢复视频流
const resetPreview = () => {
  capturedImage.value = ''
  barcode.value = ''
  bookInfo.value = null
  videoRef.value.play() // 恢复视频播放
}

// 上传图片到后端（不变）
const uploadPhoto = async (file) => {
  const formData = new FormData()
  formData.append('file', file)
  try {
    const res = await proxy.$axios.post('/barcode/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    console.log("后端完整返回：", res.data)

    if (res.data.code === 200) {
      barcode.value = res.data.data.bookBarcode
      proxy.$message.success('AI 识别成功')
      await queryBookInfo()
    } else {
      proxy.$message.error(res.data.msg || '识别失败，请重新拍摄')
      bookInfo.value = null
    }
  } catch (err) {
    proxy.$message.error('图片上传失败，请重试')
    bookInfo.value = null
  }
}

// 以下业务方法不变
const queryBookInfo = async () => {
  if (!barcode.value) {
    proxy.$message.warning('请先识别条码')
    return
  }
  try {
    const res = await proxy.$axios.get('/barcode/book?barcode=' + barcode.value)
    if (res.data.code === 200) {
      bookInfo.value = res.data.data
      proxy.$message.success('图书信息加载成功')
    } else {
      proxy.$message.error(res.data.msg || '未找到该图书')
      bookInfo.value = null
    }
  } catch (err) {
    proxy.$message.error('查询图书失败')
  }
}

const handleBorrow = async () => {
  if (!userId.value) {
    proxy.$message.warning('请先登录')
    return
  }
  if (!bookInfo.value || bookInfo.value.remainStock <= 0) {
    proxy.$message.warning('图书库存不足，无法借阅')
    return
  }
  try {
    const res = await proxy.$axios.post('/borrow/add', null, {
      params: {
        userId: userId.value,
        bookBarcode: barcode.value
      }
    })
    if (res.data.code === 200) {
      proxy.$message.success('借阅登记成功')
      await queryBookInfo()
    } else {
      proxy.$message.error(res.data.msg)
    }
  } catch (err) {
    proxy.$message.error('借阅办理失败，请重试')
  }
}

const canBorrow = computed(() => {
  return userId.value && bookInfo.value && bookInfo.remainStock > 0
})
</script>

<style scoped>
.camera-wrapper {
  background: #f5f5f5;
}
/* 取景框样式 */
.scan-frame {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 300px;
  height: 80px;
  border: 2px solid #409eff;
  box-shadow: 0 0 0 9999px rgba(0,0,0,0.5);
  pointer-events: none;
}
</style>