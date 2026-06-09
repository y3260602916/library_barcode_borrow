export function voiceSpeak(text) {
  if (!window.speechSynthesis) {
    alert('当前浏览器不支持语音播报')
    return
  }
  const utterance = new SpeechSynthesisUtterance(text)
  utterance.lang = 'zh-CN'
  utterance.rate = 1
  speechSynthesis.speak(utterance)
}

export function voiceRecognition() {
  return new Promise((resolve, reject) => {
    const Rec = window.SpeechRecognition || window.webkitSpeechRecognition
    if (!Rec) {
      reject('浏览器不支持语音识别')
      return
    }
    const rec = new Rec()
    rec.lang = 'zh-CN'
    rec.interimResults = false

    rec.onresult = (e) => {
      const text = e.results[0][0].transcript
      resolve(text)
      rec.stop()
    }
    rec.onerror = () => {
      reject('语音识别失败')
      rec.stop()
    }
    rec.start()
  })
}