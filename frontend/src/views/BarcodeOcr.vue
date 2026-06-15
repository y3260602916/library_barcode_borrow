<template>
  <div style="padding: 20px;">
    <!-- 摄像头和图书信息并排显示 -->
    <div style="display: flex; gap: 24px; margin-bottom: 16px;">
      <!-- 摄像头区域 -->
      <div style="flex-shrink: 0;">
        <div class="camera-wrapper" style="position: relative; width: 480px; height: 360px; border: 1px solid #ccc; border-radius: 4px; overflow: hidden;">
          <video 
            ref="videoRef" 
            autoplay 
            playsinline 
            muted
            style="width: 100%; height: 100%; object-fit: cover;"
          ></video>
          <!-- 扫描框四角 -->
          <div class="scan-corner top-left"></div>
          <div class="scan-corner top-right"></div>
          <div class="scan-corner bottom-left"></div>
          <div class="scan-corner bottom-right"></div>
          <!-- 扫描线动画 -->
          <div class="scan-line"></div>
          <canvas ref="canvasRef" style="display: none;"></canvas>
        </div>

        <div style="display: flex; gap: 12px; margin-top: 16px;">
          <el-button type="success" @click="openCamera" :disabled="cameraStatus">开启摄像头</el-button>
          <el-button type="danger" @click="closeCamera" :disabled="!cameraStatus">关闭摄像头</el-button>
          <el-button type="success" @click="takePhoto" :disabled="!cameraStatus">拍照识别</el-button>
          <el-button type="text" @click="resetPreview">重新拍摄</el-button>
        </div>

        <div style="display: flex; gap: 8px; align-items: center; margin-top: 16px;">
          <el-input
            v-model="barcode"
            placeholder="识别后的图书编码（仅拍照可录入）"
            clearable
            readonly
            style="width: 320px;"
          ></el-input>
          <el-button type="primary" @click="queryBookInfo">查询图书</el-button>
        </div>
      </div>

      <!-- 图书信息展示区域 -->
      <div style="flex: 1; min-width: 400px;">
        <h3 style="margin-bottom: 16px; font-size: 16px; font-weight: bold; color: #333;">📚 图书信息</h3>
        
        <div v-if="bookInfo" style="background: #f9f9f9; border-radius: 8px; padding: 20px;">
          <el-card style="border: none; box-shadow: none;">
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
              <div style="padding: 12px; background: #fff; border-radius: 4px;">
                <span style="color: #999; font-size: 13px;">图书ID</span>
                <div style="font-size: 16px; font-weight: bold; color: #333; margin-top: 4px;">{{ bookInfo.bookId }}</div>
              </div>
              <div style="padding: 12px; background: #fff; border-radius: 4px;">
                <span style="color: #999; font-size: 13px;">图书名称</span>
                <div style="font-size: 16px; font-weight: bold; color: #333; margin-top: 4px;">{{ bookInfo.bookName }}</div>
              </div>
              <div v-if="bookInfo.author" style="padding: 12px; background: #fff; border-radius: 4px;">
                <span style="color: #999; font-size: 13px;">作者</span>
                <div style="font-size: 16px; color: #333; margin-top: 4px;">{{ bookInfo.author }}</div>
              </div>
              <div v-if="bookInfo.publisher" style="padding: 12px; background: #fff; border-radius: 4px;">
                <span style="color: #999; font-size: 13px;">出版社</span>
                <div style="font-size: 16px; color: #333; margin-top: 4px;">{{ bookInfo.publisher }}</div>
              </div>
              <div v-if="bookInfo.category" style="padding: 12px; background: #fff; border-radius: 4px;">
                <span style="color: #999; font-size: 13px;">图书分类</span>
                <div style="font-size: 16px; color: #333; margin-top: 4px;">{{ bookInfo.category }}</div>
              </div>
              <div v-if="bookInfo.publishDate" style="padding: 12px; background: #fff; border-radius: 4px;">
                <span style="color: #999; font-size: 13px;">出版日期</span>
                <div style="font-size: 16px; color: #333; margin-top: 4px;">{{ bookInfo.publishDate }}</div>
              </div>
              <div v-if="bookInfo.isbn" style="padding: 12px; background: #fff; border-radius: 4px;">
                <span style="color: #999; font-size: 13px;">ISBN</span>
                <div style="font-size: 16px; color: #333; margin-top: 4px;">{{ bookInfo.isbn }}</div>
              </div>
              
              <div style="padding: 12px; background: #fff; border-radius: 4px;">
                <span style="color: #999; font-size: 13px;">总库存</span>
                <div style="font-size: 16px; font-weight: bold; color: #333; margin-top: 4px;">{{ bookInfo.totalStock }}</div>
              </div>
              <div style="padding: 12px; background: #fff; border-radius: 4px;">
                <span style="color: #999; font-size: 13px;">剩余库存</span>
                <div style="font-size: 16px; font-weight: bold; color: bookInfo.remainStock > 0 ? '#67c23a' : '#f56c6c'; margin-top: 4px;">
                  {{ bookInfo.remainStock }}
                </div>
              </div>
              <div v-if="bookInfo.description" style="padding: 12px; background: #fff; border-radius: 4px; grid-column: span 2;">
                <span style="color: #999; font-size: 13px;">图书简介</span>
                <div style="font-size: 14px; color: #666; margin-top: 4px; line-height: 1.6;">{{ bookInfo.description }}</div>
              </div>
            </div>
          </el-card>
        </div>

        <!-- 未查询到图书时的提示 -->
        <div v-else style="background: #f9f9f9; border-radius: 8px; padding: 40px; text-align: center;">
          <div style="font-size: 48px; margin-bottom: 16px;">🔍</div>
          <div style="color: #999; font-size: 14px;">请扫描图书条形码或输入图书编码查询</div>
          <div style="color: #ccc; font-size: 12px; margin-top: 8px;">扫描后将显示图书详细信息</div>
        </div>
      </div>
    </div>

    <!-- 借阅按钮 -->
    <div style="display: flex; gap: 12px;">
      <el-button type="success" :disabled="!canBorrow" @click="handleBorrow" size="large">办理图书借阅</el-button>
      <span style="color: #999; font-size: 14px; line-height: 36px;">归还图书请前往【我的借阅记录】页面操作</span>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const videoRef = ref(null)
const canvasRef = ref(null)
const cameraStatus = ref(false)
let mediaStream = null
let scanTimer = null // 扫描定时器
let barcodeDetector = null // BarcodeDetector 实例

const FRAME_WIDTH = 360
const FRAME_HEIGHT = 100

const barcode = ref('')
const bookInfo = ref(null)
const userId = ref(null)
const userName = ref('')
const fromCamera = ref(false)
const scanning = ref(false) // 是否正在扫描

onMounted(() => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    const userData = JSON.parse(user)
    userId.value = userData.userId
    userName.value = userData.userName
  }
  sessionStorage.removeItem('scanFlag')
  
  // 检查浏览器是否支持 BarcodeDetector
  if ('BarcodeDetector' in window) {
    BarcodeDetector.getSupportedFormats().then(formats => {
      console.log('支持的条码格式:', formats)
    })
  }
})

const closeCamera = () => {
  // 停止扫描
  if (scanTimer) {
    cancelAnimationFrame(scanTimer)
    scanTimer = null
  }
  scanning.value = false
  
  if (mediaStream) {
    mediaStream.getTracks().forEach(track => track.stop())
    mediaStream = null
  }
  if (videoRef.value) {
    videoRef.value.srcObject = null
  }
  cameraStatus.value = false
}

onUnmounted(() => {
  closeCamera()
})

const openCamera = async () => {
  try {
    mediaStream = await navigator.mediaDevices.getUserMedia({
      video: {
        facingMode: 'environment',
        width: { ideal: 1280 },
        height: { ideal: 720 }
      },
      audio: false
    })
    videoRef.value.srcObject = mediaStream
    cameraStatus.value = true
    ElMessage.success('摄像头开启成功，请将条形码对准扫描框')
    
    // 等待视频加载完成后开始扫描
    videoRef.value.onloadedmetadata = () => {
      startRealtimeScan()
    }
  } catch (err) {
    if (err.name === 'NotAllowedError' || err.name === 'PermissionDeniedError') {
      ElMessage.error('摄像头权限被拒绝，请在浏览器设置中允许摄像头权限')
    } else if (err.name === 'NotFoundError') {
      ElMessage.error('未检测到摄像头设备')
    } else {
      ElMessage.error('摄像头调用失败：' + err.message)
    }
    console.error('摄像头错误:', err)
  }
}

// 实时扫描条码
const startRealtimeScan = async () => {
  if (!cameraStatus.value || !videoRef.value) return
  
  scanning.value = true
  
  // 优先使用浏览器原生 BarcodeDetector API
  if ('BarcodeDetector' in window) {
    try {
      barcodeDetector = new BarcodeDetector({
        formats: ['ean_13', 'ean_8', 'code_128', 'code_39', 'code_93', 'codabar', 'upc_a', 'upc_e']
      })
      scanWithBarcodeDetector()
      return
    } catch (e) {
      console.warn('BarcodeDetector 初始化失败，使用备选方案:', e)
    }
  }
  
  // 备选方案：使用 canvas 截图上传识别
  scanWithCanvas()
}

// 使用 BarcodeDetector 实时扫描
const scanWithBarcodeDetector = async () => {
  if (!scanning.value || !cameraStatus.value) return
  
  try {
    const barcodes = await barcodeDetector.detect(videoRef.value)
    if (barcodes.length > 0) {
      const detectedBarcode = barcodes[0].rawValue
      barcode.value = detectedBarcode
      fromCamera.value = true
      sessionStorage.setItem('scanFlag', 'valid')
      ElMessage.success('识别成功：' + detectedBarcode)
      scanning.value = false
      await queryBookInfo()
      return
    }
  } catch (e) {
    console.error('扫描错误:', e)
  }
  
  // 继续扫描
  scanTimer = requestAnimationFrame(scanWithBarcodeDetector)
}

// 备选方案：canvas 截图上传识别
const scanWithCanvas = () => {
  if (!scanning.value || !cameraStatus.value) return
  
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
    if (blob) {
      uploadPhotoForScan(blob)
    }
  }, 'image/jpeg', 0.8)
}

// 上传图片识别（备选方案）
const uploadPhotoForScan = async (blob) => {
  const file = new File([blob], "barcode.jpg", { type: "image/jpeg" })
  const formData = new FormData()
  formData.append('file', file)
  
  try {
    const res = await axios.post('/barcode/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      timeout: 3000
    })
    if (res.data.code === 200 && res.data.data.bookBarcode) {
      barcode.value = res.data.data.bookBarcode
      fromCamera.value = true
      sessionStorage.setItem('scanFlag', 'valid')
      ElMessage.success('识别成功：' + res.data.data.bookBarcode)
      scanning.value = false
      await queryBookInfo()
      return
    }
  } catch (err) {
    // 忽略错误，继续扫描
  }
  
  // 继续扫描
  if (scanning.value && cameraStatus.value) {
    setTimeout(() => {
      scanTimer = requestAnimationFrame(scanWithCanvas)
    }, 500) // 每500ms扫描一次
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
  // 重新开始扫描
  if (cameraStatus.value) {
    startRealtimeScan()
  }
}

const uploadPhoto = async (file) => {
  const formData = new FormData()
  formData.append('file', file)
  try {
    const res = await axios.post('/barcode/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    if (res.data.code === 200) {
      barcode.value = res.data.data.bookBarcode
      fromCamera.value = true
      sessionStorage.setItem('scanFlag', 'valid')
      ElMessage.success('识别成功')
      scanning.value = false
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
    const res = await axios.get('/barcode/book?barcode=' + barcode.value)
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
  if (!fromCamera.value) {
    ElMessage.warning('请通过摄像头识别图书条形码')
    return
  }
  try {
    const res = await axios.post('/borrow/add', null, {
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
  return userId.value && bookInfo.value && Number(bookInfo.value?.remainStock) > 0
})
</script>

<style scoped>
.camera-wrapper {
  background: #333;
}
.scan-frame {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 280px;
  height: 80px;
  border: 2px solid #409eff;
  border-radius: 8px;
  pointer-events: none;
  background: transparent;
}
/* 扫描线动画 */
.scan-line {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 280px;
  height: 80px;
  pointer-events: none;
  overflow: hidden;
  border-radius: 8px;
}
.scan-line::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, #409eff, transparent);
  animation: scanMove 2s linear infinite;
}
@keyframes scanMove {
  0% { top: 0; }
  50% { top: calc(100% - 2px); }
  100% { top: 0; }
}
/* 四角标记 */
.scan-corner {
  position: absolute;
  width: 20px;
  height: 20px;
  border: 3px solid #409eff;
  pointer-events: none;
}
.scan-corner.top-left {
  top: calc(50% - 43px);
  left: calc(50% - 143px);
  border-right: none;
  border-bottom: none;
  border-radius: 8px 0 0 0;
}
.scan-corner.top-right {
  top: calc(50% - 43px);
  left: calc(50% + 123px);
  border-left: none;
  border-bottom: none;
  border-radius: 0 8px 0 0;
}
.scan-corner.bottom-left {
  top: calc(50% + 23px);
  left: calc(50% - 143px);
  border-right: none;
  border-top: none;
  border-radius: 0 0 8px 0;
}
.scan-corner.bottom-right {
  top: calc(50% + 23px);
  left: calc(50% + 123px);
  border-left: none;
  border-top: none;
  border-radius: 0 0 0 8px;
}
</style>