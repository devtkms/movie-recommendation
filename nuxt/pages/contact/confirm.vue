<template>
  <Header />
  <div class="form-container">
    <h2 class="form-title">お問い合わせ内容の確認</h2>

    <!-- 🔽 エラーメッセージを画面に表示（改行対応） -->
    <p v-if="errorMessage" class="error-message" v-html="errorMessage"></p>

    <form @submit.prevent="handleSubmit" class="form">
      <div class="form-group">
        <label class="form-label">ニックネーム</label>
        <div class="form-value">{{ form.nickname }}</div>
      </div>

      <div class="form-group">
        <label class="form-label">カテゴリ</label>
        <div class="form-value">{{ form.category }}</div>
      </div>

      <div class="form-group">
        <label class="form-label">お問い合わせ内容</label>
        <div class="form-value whitespace-pre-wrap">{{ form.message }}</div>
      </div>

      <div class="form-actions">
        <button type="submit" class="form-button" :disabled="loading">
          {{ loading ? '送信中...' : '送信' }}
        </button>
      </div>
    </form>
  </div>
  <Footer />
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'

const router = useRouter()
const form = ref({ nickname: '', category: '', message: '' })
const errorMessage = ref('')
const loading = ref(false)

onMounted(() => {
  const savedForm = sessionStorage.getItem('contactForm')
  if (savedForm) {
    form.value = JSON.parse(savedForm)
  } else {
    router.replace('/contact') // 直接来たら戻す
  }
})

const handleSubmit = async () => {
  errorMessage.value = ''
  loading.value = true

  try {
    const res = await fetch(`https://movie-recommendation-uybc.onrender.com/api/contact/submit`, {
    // const res = await fetch('http://localhost:8080/api/contact/submit', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value),
    })

    if (res.ok) {
      router.push('/contact/complete')
    } else {
      errorMessage.value = '送信に失敗しました。<br>しばらくしてからもう一度お試しください。'
    }
  } catch (error) {
    errorMessage.value = 'ネットワークエラーが発生しました。<br>安定した通信環境で再度お試しください。'
    console.error(error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.form-container {
  max-width: 600px;
  margin: 60px auto;
  padding: 32px;
  background-color: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.form-title {
  text-align: center;
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 24px;
}

.error-message {
  color: red;
  font-size: 0.95rem;
  text-align: center;
  margin-bottom: 16px;
  font-weight: bold;
  white-space: pre-line;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  color: #333;
}

.form-value {
  font-size: 1rem;
  color: #222;
  padding: 8px 4px;
  background-color: #f9f9f9;
  border-radius: 6px;
}

.whitespace-pre-wrap {
  white-space: pre-wrap;
}

.form-actions {
  text-align: center;
  margin-top: 24px;
}

.form-button {
  background-color: #10b981;
  color: white;
  padding: 10px 24px;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.form-button:hover {
  background-color: #059669;
}

.form-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>