<!-- /pages/contact/complete.vue -->
<template>
  <Header />
  <div class="form-container text-center">
    <h2 class="form-title">お問い合わせありがとうございます</h2>
    <p class="text-lg mt-6">お問い合わせが送信されました。</p>
    <p class="mt-4">
      トップページに戻るには
      <router-link to="/" class="text-blue-600 hover:underline">こちら</router-link>
    </p>
  </div>
  <Footer />
</template>

<script setup>
import { onMounted } from 'vue'
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'

onMounted(() => {
  // 入力情報を破棄（再送信防止）
  sessionStorage.removeItem('contactForm')
})
</script>

<style scoped>
.form-container {
  max-width: 600px;
  margin: 80px auto;
  padding: 40px;
  background-color: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.form-title {
  font-size: 1.8rem;
  font-weight: bold;
}
</style>