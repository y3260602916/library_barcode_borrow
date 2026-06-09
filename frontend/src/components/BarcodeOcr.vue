<template>
  <el-card shadow="hover" title="📷 图书条码/二维码 AI 识别">
    <el-upload
      :auto-upload="false"
      accept="image/*"
      list-type="picture"
      :on-change="handleFileChange"
      style="margin-bottom: 16px"
    >
      <el-button type="primary">选择截图上传</el-button>
    </el-upload>

    <el-input
      v-model="barcode"
      placeholder="识别后的图书编码"
      clearable
      style="width: 320px"
    >
      <template #append>
        <el-button @click="emitBarcode">使用该条码</el-button>
      </template>
    </el-input>
  </el-card>
</template>

<script setup>
import { ref, defineEmits, getCurrentInstance } from 'vue'
const { proxy } = getCurrentInstance()

const emit = defineEmits(['get-barcode'])
const barcode = ref('')

const handleFileChange = async (file) => {
  const formData = new FormData()
  formData.append('file', file.raw)
  try {
    const res = await proxy.$axios.post('/ocr/recognize', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    barcode.value = res.data
    proxy.$message.success('AI 识别成功')
  } catch (err) {
    proxy.$message.error('识别失败，请更换图片重试')
  }
}

const emitBarcode = () => {
  if (!barcode.value) return proxy.$message.warning('暂无条码')
  emit('get-barcode', barcode.value)
}
</script>