<template>
  <header class="header">
    <div class="logo-title">
      <img src="/images/logo.png" alt="MoviRecoロゴ" class="logo-image" />
    </div>

    <div class="nav-container">
      <!-- ✅ ハンバーガーメニュー -->
      <button class="hamburger" @click="toggleMenu">☰</button>
      <nav :class="{ open: menuOpen }" class="nav">
        <NuxtLink to="/" class="nav-link">ホーム</NuxtLink>

        <template v-if="!isLoggedIn">
          <NuxtLink
              v-if="route.path !== '/userRegister'"
              to="/userRegister"
              class="nav-link"
          >
            新規登録
          </NuxtLink>
          <NuxtLink
              v-if="route.path !== '/login'"
              to="/login"
              class="nav-link"
          >
            ログイン
          </NuxtLink>
        </template>

        <NuxtLink to="/contact" class="nav-link">お問い合わせ</NuxtLink>
        <NuxtLink to="/privacy" class="nav-link">プライバシーポリシー</NuxtLink>
        <NuxtLink to="/terms" class="nav-link">利用規約</NuxtLink>

        <template v-if="isLoggedIn">
          <button class="nav-link logout-menu-button" @click="logout">ログアウト</button>
        </template>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const menuOpen = ref(false)
const isLoggedIn = ref(false)
const route = useRoute()
const router = useRouter()
const config = useRuntimeConfig()
const apiBase = config.public.apiBase

const toggleMenu = () => {
  menuOpen.value = !menuOpen.value
}

const checkLoginStatus = async () => {
  try {
    const res = await fetch(`${apiBase}/api/users/me`, {
      method: 'GET',
      credentials: 'include'
    })

    isLoggedIn.value = res.ok
  } catch (err) {
    isLoggedIn.value = false
    console.error('ログイン状態の確認に失敗', err)
  }
}

onMounted(() => {
  checkLoginStatus()
})

const logout = async () => {
  try {
    await fetch(`${apiBase}/api/users/logout`, {
      method: 'POST',
      credentials: 'include'
    })

    localStorage.removeItem('nickname') // 任意
    isLoggedIn.value = false
    router.push('/')
  } catch (err) {
    console.error('ログアウト失敗', err)
  }
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

.logout-menu-button {
  background: none;
  border: none;
  color: #ef4444;
  font-weight: 600;
  padding: 10px 16px;
  text-align: left;
  width: 100%;
  cursor: pointer;
}

.logout-menu-button:hover {
  background-color: #fee2e2; /* 薄い赤 */
}



</style>
