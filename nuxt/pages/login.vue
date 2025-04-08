<template>
  <div class="login-page">
    <Header />

    <div class="login-container">
      <div class="login-card">
        <h1 class="login-title">ログイン</h1>

        <form @submit.prevent="submitLogin" class="login-form">
          <label class="form-label">ユーザーID</label>
          <input
              v-model="userId"
              type="text"
              required
              class="form-input"
              placeholder="yourname123"
          />

          <label class="form-label">パスワード</label>
          <input
              v-model="password"
              type="password"
              required
              class="form-input"
              placeholder="パスワード"
          />

          <button type="submit" class="submit-button">ログイン</button>
        </form>

        <p v-if="error" class="error-message">{{ error }}</p>

        <p class="register-link">
          アカウントをお持ちでない方は
          <NuxtLink to="/userRegister">新規登録はこちら</NuxtLink>
        </p>
      </div>
    </div>

    <Footer />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'
import { useRouter } from 'vue-router'

const userId = ref('')
const password = ref('')
const error = ref(null)
const router = useRouter()
const config = useRuntimeConfig()
const apiBase = config.public.apiBase

const submitLogin = async () => {
  try {
    const response = await $fetch(`${apiBase}/api/users/login`, {
      method: 'POST',
      body: {
        userId: userId.value,
        password: password.value
      },
      credentials: 'include'
    })

    // 以降は変更不要
    const res = await fetch(`${apiBase}/api/users/me`, {
      method: 'GET',
      credentials: 'include'
    })

    if (res.ok) {
      const user = await res.json()
      localStorage.setItem('nickname', user.nickname)
    }

    error.value = null
    await router.push('/')
  } catch (err) {
    console.error('ログインエラー:', err)
    error.value = 'ログインに失敗しました。ユーザーIDまたはパスワードを確認してください。'
  }
}
</script>

<style scoped>
.login-page {
  background-color: #f3f4f6;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.login-container {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 1.5rem 1rem;
}

.login-card {
  background: white;
  padding: 1.5rem;
  border-radius: 1rem;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.06);
  max-width: 400px;
  width: 100%;
}

.login-title {
  font-size: 1.4rem;
  font-weight: bold;
  text-align: center;
  margin-bottom: 1rem;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.form-label {
  font-size: 0.9rem;
  color: #374151;
}

.form-input {
  padding: 0.5rem 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 0.5rem;
  font-size: 1rem;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.form-input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
}

.submit-button {
  background-color: #3b82f6;
  color: white;
  font-weight: 600;
  padding: 0.6rem;
  border: none;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.submit-button:hover {
  background-color: #2563eb;
}

.error-message {
  margin-top: 0.75rem;
  color: #dc2626;
  text-align: center;
  font-size: 0.9rem;
}

.register-link {
  margin-top: 1rem;
  text-align: center;
  font-size: 0.9rem;
  color: #4b5563;
}

.register-link a {
  color: #3b82f6;
  font-weight: bold;
  text-decoration: none;
  margin-left: 4px;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>