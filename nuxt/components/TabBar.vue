<template>
  <div class="tab-bar">
    <NuxtLink
        to="/"
        class="tab-button"
        :class="{ active: current === 'main' }"
    >メイン</NuxtLink>

    <a
        href="#"
        class="tab-button"
        :class="{ active: current === 'recommend' }"
        @click.prevent="handleRecommendClick"
    >レコメンド</a>
  </div>
</template>

<script setup>
const props = defineProps({
  current: String
})

const emit = defineEmits(['require-login'])

const handleRecommendClick = () => {
  const token = localStorage.getItem('token')
  if (!token) {
    emit('require-login') // モーダル表示要求
  } else {
    window.location.href = '/recommend' // 通常遷移
  }
}
</script>

<style scoped>
.tab-bar {
  display: flex;
  justify-content: space-around;
  background-color: #ffffff;
  border-top: 1px solid #ccc;
}

.tab-button {
  flex: 1;
  padding: 12px 0;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  text-align: center;
  text-decoration: none;
  cursor: pointer;
}

.tab-button.active {
  color: #2196F3;
  border-bottom: 3px solid #2196F3;
}
</style>