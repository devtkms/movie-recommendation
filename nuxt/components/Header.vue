<template>
  <header class="header">
    <div class="logo-title">
      <img src="/images/logo.png" alt="MoviRecoロゴ" class="logo-image" />
    </div>

    <div class="nav-container">
      <!-- 🔽 ログイン＆登録ボタン -->
      <div class="auth-buttons" v-if="!isLoggedIn">
        <!-- loginページでなければ「ログイン」も表示 -->
        <NuxtLink
            v-if="route.path !== '/userRegister'"
            to="/userRegister"
            class="auth-button register-button"
        >
          新規登録
        </NuxtLink>
        <NuxtLink
            v-if="route.path !== '/login'"
            to="/login"
            class="auth-button login-button"
        >
          ログイン
        </NuxtLink>
      </div>

      <div class="auth-buttons" v-else>
        <button class="auth-button logout-button" @click="logout">ログアウト</button>
      </div>

      <!-- ハンバーガーメニュー -->
      <button class="hamburger" @click="toggleMenu">☰</button>
      <nav :class="{ open: menuOpen }" class="nav">
        <NuxtLink to="/" class="nav-link">ホーム</NuxtLink>
        <NuxtLink to="/contact" class="nav-link">お問い合わせ</NuxtLink>
        <NuxtLink to="/privacy" class="nav-link">プライバシーポリシー</NuxtLink>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'


const menuOpen = ref(false)
const router = useRouter()
const toggleMenu = () => {
  menuOpen.value = !menuOpen.value
}
const route = useRoute()
const isLoggedIn = ref(false)

onMounted(() => {
  const token = localStorage.getItem('token')

  isLoggedIn.value = !!token
})

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('nickname')
  isLoggedIn.value = false
  router.push('/') // ← ホームに遷移
}
</script>

<style scoped>
.header {
  position: sticky;
  top: 0;
  z-index: 1000;
  background-color: #ffffff;
  border-bottom: 1px solid #e0e0e0; /* ← 明るいグレーでフラットな線 */
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 64px;
  padding: 0 10px;
  margin-bottom: 30px;
}

.logo-title {
  display: flex;
  align-items: center;
  gap: 12px;
  height: 100%;
}

.logo-image {
  height: 110px;
  width: auto;
}

.title {
  font-size: 23px;
  font-weight: bold;
  margin: 0;
  color: #333;
}

.nav-container {
  display: flex;
  align-items: center;
}

.hamburger {
  font-size: 24px;
  background: none;
  border: none;
  cursor: pointer;
}

.nav {
  position: absolute;
  top: 40px;
  right: 0;
  background: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 10px;
  display: none;
  min-width: 120px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);

  /* 🔽 これを追加 */
  z-index: 9999;
}

.nav.open {
  display: block;
}

.nav-link {
  display: block;
  width: 100%;
  padding: 10px 16px;
  text-decoration: none;
  color: #333;
  white-space: nowrap;
  text-align: left;
}

.nav-link:hover {
  background-color: #f5f5f5;
}

.auth-button {
  display: inline-block;
  padding: 6px 10px;
  font-size: 12px;
  font-weight: 600;
  border-radius: 6px;
  text-decoration: none;
  transition: background-color 0.2s;
  color: white;
}

.auth-buttons {
  display: flex;
  gap: 8px; /* 🔽 ボタン間のスペース（px単位で調整可） */
  margin-right: 12px; /* ナビとの間に少し余白もつけるなら */
}

/* ✅ 新しいクラスで色指定 */
.register-button {
  background-color: #10b981; /* 緑 */
}
.register-button:hover {
  background-color: #059669;
}

.login-button {
  background-color: #3b82f6; /* 青 */
}
.login-button:hover {
  background-color: #2563eb;
}
.logout-button {
  background-color: #ef4444; /* 赤 */
}
.logout-button:hover {
  background-color: #dc2626;
}

</style>
