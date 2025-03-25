<template>
  <header class="header">
    <div class="logo-title">
      <img src="/images/logo.png" alt="MoviRecoロゴ" class="logo-image" />
      <h1 class="title">MoviReco</h1>
    </div>
    <div class="nav-container">
      <button class="hamburger" @click="toggleMenu">☰</button>
      <nav :class="{ open: menuOpen }" class="nav">
        <NuxtLink to="/" class="nav-link">ホーム</NuxtLink>
        <NuxtLink to="/privacy" class="nav-link">プライバシーポリシー</NuxtLink>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'
const menuOpen = ref(false)
const toggleMenu = () => {
  menuOpen.value = !menuOpen.value
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
  padding: 0 20px;
  margin-bottom: 30px;
}

.logo-title {
  display: flex;
  align-items: center;
  gap: 12px;
  height: 100%;
}

.logo-image {
  height: 35px;
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
</style>
