<template>
  <Header />
  <div class="form-container">
    <h2 class="form-title">お問い合わせフォーム</h2>

    <!-- 🔽 エラーメッセージを画面に表示（改行対応） -->
    <p v-if="errorMessage" class="error-message" v-html="errorMessage"></p>

    <form @submit.prevent="handleConfirm" class="form">
      <div class="form-group">
        <label class="form-label">ニックネーム</label>
        <input v-model="form.nickname" type="text" class="form-input" required />
      </div>

      <div class="form-group">
        <label class="form-label">カテゴリ</label>
        <select v-model="form.category" class="form-input" required>
          <option disabled value="">選択してください</option>
          <option>不具合</option>
          <option>機能追加要望</option>
          <option>操作方法の質問</option>
          <option>その他</option>
        </select>
      </div>

      <div class="form-group">
        <label class="form-label">お問い合わせ内容</label>
        <textarea v-model="form.message" class="form-textarea" required></textarea>
      </div>

      <div class="form-actions">
        <button type="submit" class="form-button">確認</button>
      </div>
    </form>
  </div>
  <Footer />
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'

const router = useRouter()
const form = reactive({
  nickname: '',
  category: '',
  message: ''
})
const errorMessage = ref('')

const handleConfirm = async () => {
  errorMessage.value = '' // エラーリセット

  try {
    const res = await fetch(`https://movie-recommendation-uybc.onrender.com/api/contact/validate`, {
    // const res = await fetch('http://localhost:8080/api/contact/validate', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form)
    })

    if (!response.ok) {
      const error = await response.json()
      errorMessage.value = error.message || '入力に誤りがあります'
      return
    }

    const validated = await response.json()
    sessionStorage.setItem('contactForm', JSON.stringify(validated))
    router.push('/contact/confirm')
  } catch (err) {
    errorMessage.value = '確認画面の表示に失敗しました。<br>しばらくしてからもう一度お試しください。'
    console.error(err)
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

.form-input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1.1rem;
  box-sizing: border-box;
}

.form-input option {
  font-size: 2rem;
}

.form-textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1.1rem;
  box-sizing: border-box;
  height: 140px;
  resize: none;
}

.form-actions {
  text-align: center;
}

.form-button {
  background-color: #2563eb;
  color: white;
  padding: 10px 24px;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.form-button:hover {
  background-color: #1d4ed8;
}
</style>