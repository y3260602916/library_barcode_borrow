<template>
  <el-card shadow="hover" title="📖 我的借阅记录" style="margin-top: 20px">
    <!-- 操作按钮 -->
    <div style="margin-bottom: 16px;">
      <el-button type="primary" icon="Refresh" @click="getBorrowList">刷新记录</el-button>
    </div>

    <el-table
      :data="borrowList"
      border
      stripe
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column label="记录ID" prop="recordId" width="80" align="center" />
      <el-table-column label="图书ID" prop="bookId" width="80" align="center" />
      <el-table-column label="图书名称" prop="bookName" />
      <el-table-column label="借阅时间" width="180" align="center">
        <template #default="{ row }">
          {{ formatDate(row.borrowTime) }}
        </template>
      </el-table-column>
      <el-table-column label="归还时间" width="180" align="center">
        <template #default="{ row }">
          {{ formatDate(row.returnTime) }}
        </template>
      </el-table-column>
      <el-table-column label="是否逾期" prop="isOverdue" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.isOverdue === 1 ? 'danger' : 'success'">
            {{ row.isOverdue === 1 ? '已逾期' : '正常' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="逾期罚款(元)" prop="fineMoney" width="120" align="center" />
      <el-table-column label="操作" width="150" align="center">
        <template #default="{ row }">
          <template v-if="row.returnTime !== null && row.returnTime !== '未归还'">
            <el-tag type="success">已归还</el-tag>
          </template>
          <el-button
            v-else
            type="warning"
            size="small"
            @click="openReturnDialog(row.recordId)"
          >
            扫码归还
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty description="暂无借阅记录" v-if="borrowList.length === 0 && !loading" />

    <!-- 归还图书弹窗 -->
    <el-dialog title="扫码归还图书" v-model="showReturnDialog" width="600px" :close-on-click-modal="false">
      <div style="padding: 16px;">
        <div style="margin-bottom: 12px; font-weight: bold; color: #666;">请扫描要归还的图书条形码</div>
        
        <div class="camera-wrapper" style="position: relative; width: 100%; height: 320px; border: 1px solid #ccc; border-radius: 4px; overflow: hidden; margin-bottom: 16px; background: #333;">
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

        <div style="display: flex; gap: 12px; margin-bottom: 12px;">
          <el-button type="primary" @click="openCamera" :disabled="cameraStatus">开启摄像头</el-button>
          <el-button type="danger" @click="closeCamera" :disabled="!cameraStatus">关闭摄像头</el-button>
          <el-button type="success" @click="takePhoto" :disabled="!cameraStatus">拍照识别</el-button>
          <el-button type="text" @click="resetPreview">重新拍摄</el-button>
        </div>

        <div style="display: flex; gap: 8px; align-items: center;">
          <el-input
            v-model="barcode"
            placeholder="识别后的图书编码（仅拍照可录入）"
            clearable
            readonly
            style="flex: 1;"
          ></el-input>
          <el-tag :type="scanValid ? 'success' : 'warning'">
            {{ scanValid ? '✓ 已识别' : '请扫描条形码' }}
          </el-tag>
        </div>
      </div>

      <template #footer>
        <el-button @click="closeReturnDialog">取消</el-button>
        <el-button 
          type="primary" 
          :disabled="!scanValid || btnLoading" 
          @click="handleReturn"
        >
          {{ btnLoading ? '归还中...' : '确认归还' }}
        </el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>import { ref, onUnmounted } from 'vue';
import { ElMessage } from 'element-plus';
import axios from 'axios';

// 时间格式化函数
const formatDate = (timestamp) => {
  if (!timestamp || timestamp === '未归还') {
    return '未归还';
  }
  try {
    const date = new Date(Number(timestamp));
    if (isNaN(date.getTime())) {
      return timestamp;
    }
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  } catch (e) {
    return timestamp;
  }
};

// 摄像头相关
const videoRef = ref(null);
const canvasRef = ref(null);
const cameraStatus = ref(false);
let mediaStream = null;
const FRAME_WIDTH = 360;
const FRAME_HEIGHT = 100;
const barcode = ref('');
const scanValid = ref(false);

// 借阅记录相关
const borrowList = ref([]);
const loading = ref(false);
const btnLoading = ref(false);
const showReturnDialog = ref(false);
const currentRecordId = ref(null);

// 获取用户ID
const getUserId = () => {
  const userInfoStr = localStorage.getItem('userInfo');
  if (!userInfoStr) {
    ElMessage.warning('请先登录');
    return null;
  }
  try {
    const userInfo = JSON.parse(userInfoStr);
    return userInfo.userId;
  } catch (error) {
    ElMessage.warning('用户信息异常，请重新登录');
    localStorage.removeItem('userInfo');
    return null;
  }
};

// 获取借阅列表
const getBorrowList = async () => {
  const userId = getUserId();
  if (!userId) return;
  loading.value = true;
  try {
    const res = await axios.get('/borrow/list', {
      params: { userId }
    });
    if (res.data.code === 200) {
      borrowList.value = res.data.data || [];
    } else {
      ElMessage.error(res.data.msg || '获取借阅记录失败');
    }
  } catch (err) {
    ElMessage.error('网络异常，查询借阅记录失败');
  } finally {
    loading.value = false;
  }
};

// 打开归还对话框
const openReturnDialog = (recordId) => {
  currentRecordId.value = recordId;
  showReturnDialog.value = true;
  // 重置状态
  barcode.value = '';
  scanValid.value = false;
};

// 关闭归还对话框
const closeReturnDialog = () => {
  showReturnDialog.value = false;
  closeCamera();
};

// 归还图书
const handleReturn = async () => {
  if (!currentRecordId.value || !scanValid.value) {
    ElMessage.warning('请先扫描图书条形码');
    return;
  }
  
  btnLoading.value = true;
  try {
    const res = await axios.put('/borrow/return', null, {
      params: { recordId: currentRecordId.value, barcode: barcode.value }
    });
    if (res.data.code === 200) {
      ElMessage.success(res.data.msg || '归还成功');
      closeReturnDialog();
      getBorrowList();
    } else {
      ElMessage.error(res.data.msg || '归还图书失败');
    }
  } catch (err) {
    ElMessage.error('网络异常，归还图书失败');
  } finally {
    btnLoading.value = false;
  }
};

// ========== 摄像头相关方法（参考 BarcodeOcr.vue 的实现）==========
const closeCamera = () => {
  if (mediaStream) {
    mediaStream.getTracks().forEach(track => track.stop());
    mediaStream = null;
  }
  if (videoRef.value) {
    videoRef.value.srcObject = null;
  }
  cameraStatus.value = false;
};

const openCamera = async () => {
  try {
    mediaStream = await navigator.mediaDevices.getUserMedia({
      video: { facingMode: 'environment' },
      audio: false
    });
    videoRef.value.srcObject = mediaStream;
    cameraStatus.value = true;
    ElMessage.success('摄像头开启成功');
  } catch (err) {
    if (err.name === 'NotAllowedError' || err.name === 'PermissionDeniedError') {
      ElMessage.error('摄像头权限被拒绝，请在浏览器设置中允许摄像头权限');
    } else if (err.name === 'NotFoundError') {
      ElMessage.error('未检测到摄像头设备');
    } else {
      ElMessage.error('摄像头调用失败：' + err.message);
    }
    console.error('摄像头错误:', err);
  }
};

const takePhoto = () => {
  if (!cameraStatus.value) return;
  
  const video = videoRef.value;
  const canvas = canvasRef.value;
  const ctx = canvas.getContext('2d');
  const vw = video.videoWidth;
  const vh = video.videoHeight;
  const cropX = (vw - FRAME_WIDTH) / 2;
  const cropY = (vh - FRAME_HEIGHT) / 2;
  
  canvas.width = FRAME_WIDTH;
  canvas.height = FRAME_HEIGHT;
  ctx.drawImage(video, cropX, cropY, FRAME_WIDTH, FRAME_HEIGHT, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
  
  canvas.toBlob((blob) => {
    if (!blob) return;
    const file = new File([blob], "barcode.jpg", { type: "image/jpeg" });
    uploadPhoto(file);
  }, 'image/jpeg');
};

const resetPreview = () => {
  barcode.value = '';
  scanValid.value = false;
  sessionStorage.removeItem('scanFlag');
  if (cameraStatus.value && videoRef.value) {
    videoRef.value.play();
  }
};

const uploadPhoto = async (file) => {
  const formData = new FormData();
  formData.append('file', file);
  
  try {
    const res = await axios.post('/barcode/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    if (res.data.code === 200) {
      barcode.value = res.data.data.bookBarcode;
      scanValid.value = true;
      sessionStorage.setItem('scanFlag', 'valid');
      ElMessage.success('识别成功');
    } else {
      ElMessage.error(res.data.msg || '识别失败');
      scanValid.value = false;
      sessionStorage.removeItem('scanFlag');
    }
  } catch (err) {
    ElMessage.error('上传失败');
    console.error(err);
    scanValid.value = false;
    sessionStorage.removeItem('scanFlag');
  }
};

// 页面加载时获取借阅列表
getBorrowList();

onUnmounted(() => {
  closeCamera();
});
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
  width: 360px;
  height: 100px;
  border: 2px solid #409eff;
  box-shadow: 0 0 0 9999px rgba(0,0,0,0.5);
  pointer-events: none;
}
</style>