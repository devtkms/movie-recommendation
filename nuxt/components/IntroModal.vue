<template>
  <div class="intro-modal">
    <div class="intro-content">
      <h2>🎬 MoviRecoへようこそ！</h2>
      <p>映画迷子にさよなら。<br>3つの質問に答えるだけで、あなたにピッタリの映画をおすすめします。</p>
      <p>登録も不要。<br>スワイプ操作で映画をチェックしてみよう！</p>
      <button @click="$emit('close')">👉 はじめる</button>
    </div>
  </div>
</template>

<script setup>
// 特になし。親から表示制御するだけなのでprops/emitのみでOK
defineEmits(['close'])
</script>

<style scoped>
.intro-modal {
  position: fixed;
  z-index: 9999;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
}

.intro-content {
  background: #fff;
  padding: 30px 20px;
  border-radius: 12px;
  max-width: 90%;
  text-align: center;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
  font-size: 16px;
  line-height: 1.6;
}

.intro-content h2 {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 16px;
}

.intro-content button {
  margin-top: 20px;
  padding: 10px 20px;
  background: #333;
  color: #fff;
  border-radius: 8px;
  border: none;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.intro-content button:hover {
  background: #555;
}
</style>