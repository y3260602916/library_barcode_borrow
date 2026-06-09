<template>
  <el-container style="height: 100vh">
    <el-header style="text-align:center;font-size:24px;padding:20px">
      校园图书馆智能管理系统
      <el-button @click="openVoice" style="margin-left:20px">🎤 语音助手</el-button>
    </el-header>

    <el-container>
      <el-main>
        <BarcodeOcr @get-barcode="fillBarcode" />
        <el-divider />

        <el-card title="图书借阅 / 归还" style="margin-bottom:20px">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-input v-model="userId" placeholder="用户ID"></el-input>
            </el-col>
            <el-col :span="8">
              <el-input v-model="bookBarcode" placeholder="图书条码"></el-input>
            </el-col>
            <el-col :span="8">
              <el-button type="success" @click="borrowBook">借阅</el-button>
              <el-button type="warning" @click="returnBook">归还</el-button>
            </el-col>
          </el-row>
        </el-card>

        <DataChart />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, getCurrentInstance } from 'vue'
import BarcodeOcr from './components/BarcodeOcr.vue'
import DataChart from './components/DataChart.vue'
import { voiceSpeak, voiceRecognition } from './utils/voice'

const { proxy } = getCurrentInstance()
const userId = ref('')
const bookBarcode = ref('')

const fillBarcode = (code) => {
  bookBarcode.value = code
}

const openVoice = async () => {
  try {
    const text = await voiceRecognition()
    voiceSpeak(`已识别指令：${text}`)
  } catch (err) {
    proxy.$message.error(err)
  }
}

const borrowBook = async () => {
  if (!userId.value || !bookBarcode.value) {
    proxy.$message.warning('请填写用户ID和图书条码')
    return
  }
  try {
    await proxy.$axios.post('/borrow/add', null, {
      params: {
        userId: userId.value,
        bookBarcode: bookBarcode.value
      }
    })
    proxy.$message.success('借阅成功')
    voiceSpeak('借阅成功')
  } catch (e) {
    proxy.$message.error('借阅失败')
    voiceSpeak('借阅失败，请检查信息')
  }
}

const returnBook = async () => {
  if (!userId.value) {
    proxy.$message.warning('请输入记录ID')
    return
  }
  try {
    await proxy.$axios.put('/borrow/return', null, {
      params: { recordId: userId.value }
    })
    proxy.$message.success('归还成功')
    voiceSpeak('归还成功')
  } catch (e) {
    proxy.$message.error('归还失败')
  }
}
</script>