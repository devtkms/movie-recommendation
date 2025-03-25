<template>
  <header class="header">
    <h1 class="title">MoviReco</h1>
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
  background-color: #ffffff;
  text-align: center;
  padding: 20px 0;
  border-bottom: 2px solid #ddd;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 30px;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}

.title {
  font-size: 28px;
  font-weight: bold;
  margin: 0;
  color: #333;
}

.nav-container {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
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
  text-align: left;
}

.nav-link:hover {
  background-color: #f5f5f5;
}
</style>
