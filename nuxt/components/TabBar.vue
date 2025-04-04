<template>
  <div class="tab-bar">
    <NuxtLink
        to="/"
        class="tab-button"
        :class="{ active: current === 'main' }"
    >メイン</NuxtLink>
    <NuxtLink
        to="/recommend"
        class="tab-button"
        :class="{ active: current === 'recommend' }"
    >レコメンド</NuxtLink>
  </div>
</template>

<script setup>
defineProps({
  current: String
});
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
}

.tab-button.active {
  color: #2196F3;
  border-bottom: 3px solid #2196F3;
}
</style>