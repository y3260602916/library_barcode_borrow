<template>
  <div style="padding: 20px;">
    <div style="margin-bottom: 16px;">
      当前登录：张三 | 身份：师生
    </div>

    <div class="camera-wrapper" style="position: relative; width: 480px; height: 360px; border: 1px solid #ccc; border-radius: 4px; overflow: hidden; margin-bottom: 16px;">
      <video 
        ref="videoRef" 
        autoplay 
        playsinline 
        muted
        style="width: 100%; height: 100%; object-fit: cover;"
      ></video>
      <div class="scan-frame"></div>
      <canvas ref="canvasRef" style="display: none;"></canvas>
    </div>

    <div style="display: flex; gap: 12px; margin-bottom: 16px;">
      <el-button type="primary" @click="openCamera" :disabled="cameraStatus">开启摄像头</el-button>
      <el-button type="danger" @click="closeCamera" :disabled="!cameraStatus">关闭摄像头</el-button>
      <el-button type="success" @click="takePhoto" :disabled="!cameraStatus">拍照识别</el-button>
      <!-- 把 type="text" 改为 link，消除 ElementPlus 弃用警告 -->
      <el-button link @click="resetPreview">重新拍摄</el-button>
    </div>

    <div style="display: flex; gap: 8px; align-items: center; margin-bottom: 16px;">
      <el-input
        v-model="barcode"
        placeholder="识别后的图书编码（仅拍照可录入）"
        clearable
        readonly
        style="width: 320px;"
      ></el-input>
      <el-button type="primary" @click="queryBookInfo">查询图书</el-button>
    </div>

    <div style="display: flex; gap: 12px;">
      <el-button type="success" :disabled="!canBorrow" @click="handleBorrow">办理图书借阅</el-button>
      <span style="color: #999; font-size: 14px;">归还图书请前往【我的借阅记录】页面操作</span>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { getCurrentInstance } from 'vue'
const { proxy } = getCurrentInstance()

const videoRef = ref(null)
const canvasRef = ref(null)
const cameraStatus = ref(false)
let mediaStream = null

const FRAME_WIDTH = 360
const FRAME_HEIGHT = 100

const barcode = ref('')
const bookInfo = ref(null)
const userId = ref(null)
const fromCamera = ref(false)

onMounted(() => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    userId.value = JSON.parse(user).userId
  }
  sessionStorage.removeItem('scanFlag')
})

onUnmounted(() => {
  closeCamera()
})

const openCamera = async () => {
  try {
    mediaStream = await navigator.mediaDevices.getUserMedia({
      video: {
        facingMode: 'environment'
      },
      audio: false
    })
    videoRef.value.srcObject = mediaStream
    cameraStatus.value = true
    ElMessage.success('摄像头开启成功')
  } catch (err) {
    ElMessage.error('摄像头调用失败，请检查权限')
    console.error(err)
  }
}

const closeCamera = () => {
  if (mediaStream) {
    mediaStream.getTracks().forEach(track => track.stop())
    mediaStream = null
    videoRef.value.srcObject = null
    cameraStatus.value = false
  }
}

const takePhoto = () => {
  if (!cameraStatus.value) return
  const video = videoRef.value
  const canvas = canvasRef.value
  const ctx = canvas.getContext('2d')

  const vw = video.videoWidth
  const vh = video.videoHeight
  const cropX = (vw - FRAME_WIDTH) / 2
  const cropY = (vh - FRAME_HEIGHT) / 2

  canvas.width = FRAME_WIDTH
  canvas.height = FRAME_HEIGHT
  ctx.drawImage(video, cropX, cropY, FRAME_WIDTH, FRAME_HEIGHT, 0, 0, FRAME_WIDTH, FRAME_HEIGHT)

  canvas.toBlob((blob) => {
    if (!blob) return
    const file = new File([blob], "barcode.jpg", { type: "image/jpeg" })
    uploadPhoto(file)
  }, 'image/jpeg')
}

const resetPreview = () => {
  barcode.value = ''
  bookInfo.value = null
  fromCamera.value = false
  sessionStorage.removeItem('scanFlag')
  if (cameraStatus.value) {
    videoRef.value.play()
  }
}

const uploadPhoto = async (file) => {
  const formData = new FormData()
  formData.append('file', file)
  try {
    const res = await proxy.$axios.post('/barcode/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    if (res.data.code === 200) {
      barcode.value = res.data.data.bookBarcode
      fromCamera.value = true
      sessionStorage.setItem('scanFlag', 'valid')
      ElMessage.success('识别成功')
      await queryBookInfo()
    } else {
      ElMessage.error(res.data.msg || '识别失败')
      fromCamera.value = false
      sessionStorage.removeItem('scanFlag')
    }
  } catch (err) {
    ElMessage.error('上传失败')
    console.error(err)
    fromCamera.value = false
    sessionStorage.removeItem('scanFlag')
  }
}

const queryBookInfo = async () => {
  if (!barcode.value) return
  try {
    const res = await proxy.$axios.get('/barcode/book?barcode=' + barcode.value)
    if (res.data.code === 200) {
      bookInfo.value = res.data.data
      ElMessage.success('查询成功')
    } else {
      ElMessage.error(res.data.msg || '未找到图书')
      bookInfo.value = null
    }
  } catch (err) {
    ElMessage.error('查询失败')
    console.error(err)
  }
}

const handleBorrow = async () => {
  if (!userId.value) {
    ElMessage.warning('请先登录')
    return
  }
  if (!bookInfo.value || bookInfo.value.remainStock <= 0) {
    ElMessage.warning('库存不足')
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
      ElMessage.success('借阅成功')
      sessionStorage.removeItem('scanFlag')
      await queryBookInfo()
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (err) {
    ElMessage.error('借阅失败')
    console.error(err)
  }
}

const canBorrow = computed(() => {
  return userId.value && bookInfo.value && Number(bookInfo.value?.remainStock) > 0 && fromCamera.value
})
</script>

<style scoped>
.camera-wrapper {
  background: #f5f5f5;
}
.scan-frame {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 360px;
  height: 100px;
  border: 2px solid #409eff;
  box-shadow: 0 0 0 9999px rgba(0,0,0,0.5);
  pointer-events: none;
}
</style>