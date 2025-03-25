<template>
  <div class="nav-wrapper">
    <button class="hamburger" @click="toggleMenu">☰</button>
    <nav :class="{ open: menuOpen }" class="nav">
      <NuxtLink to="/" class="nav-link">ホーム</NuxtLink>
      <NuxtLink to="/privacy" class="nav-link">プライバシーポリシー</NuxtLink>
<!--      <NuxtLink to="/login" class="nav-link">ログイン</NuxtLink>-->
    </nav>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const menuOpen = ref(false)
const toggleMenu = () => {
  menuOpen.value = !menuOpen.value
}
</script>

<style scoped>
.nav-wrapper {
  position: relative;
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

  /* 🔽 追加するといい */
  min-width: 120px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
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
  text-align: left; /* 🔽 ここを追加！ */
}

.nav-link:hover {
  background-color: #f5f5f5;
}
</style>