<template>
  <div class="tab-bar">
    <NuxtLink
        to="/"
        class="tab-button"
        :class="{ active: current === 'main' }"
        @click.prevent="emit('click-main')"
    >
      メイン
    </NuxtLink>

    <a
        href="#"
        class="tab-button"
        :class="{ active: current === 'recommend' }"
        @click.prevent="handleRecommendClick"
    >レコメンド</a>
  </div>
</template>

<script setup>
const config = useRuntimeConfig()
const apiBase = config.public.apiBase
const props = defineProps({
  current: String
})

const emit = defineEmits(['require-login', 'click-main'])

const handleRecommendClick = async () => {
  // すでに recommend にいるなら何もしない
  if (props.current === 'recommend') return

  try {
    const res = await fetch(`${apiBase}/api/users/me`, {
      method: 'GET',
      credentials: 'include', // ✅ Cookie送信
    })

    if (res.ok) {
      // ログイン済み → 遷移
      window.location.href = '/recommend'
    } else {
      // 未ログイン → モーダル表示
      emit('require-login')
    }
  } catch (err) {
    emit('require-login')
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