<template>
  <div class="tab-bar">
    <NuxtLink
        to="/"
        class="tab-button"
        :class="{ active: current === 'main' }"
        @click.prevent="emit('click-main')"
    >
      <HomeIcon class="icon" />
    </NuxtLink>

    <a
        href="#"
        class="tab-button"
        :class="{ active: current === 'recommend' }"
        @click.prevent="handleRecommendClick"
    >
      <FilmIcon class="icon" />
    </a>

    <a
        href="#"
        class="tab-button"
        :class="{ active: current === 'save' }"
        @click.prevent="handleSaveClick"
    >
      <BookmarkIcon class="icon" />
    </a>
  </div>
</template>

<script setup>
import { HomeIcon, FilmIcon, BookmarkIcon } from '@heroicons/vue/24/outline'

const config = useRuntimeConfig()
const apiBase = config.public.apiBase
const props = defineProps({
  current: String
})

const emit = defineEmits(['require-login', 'click-main'])

const handleRecommendClick = async () => {
  if (props.current === 'recommend') return

  try {
    const res = await fetch(`${apiBase}/api/users/me`, {
      method: 'GET',
      credentials: 'include',
    })

    if (res.ok) {
      window.location.href = '/recommend'
    } else {
      emit('require-login', 'recommend')
    }
  } catch (err) {
    emit('require-login', 'recommend')
  }
}

const handleSaveClick = async () => {
  if (props.current === 'save') return

  try {
    const res = await fetch(`${apiBase}/api/users/me`, {
      credentials: 'include',
    })

    if (res.ok) {
      window.location.href = '/savedMovies'
    } else {
      emit('require-login', 'save')
    }
  } catch {
    emit('require-login', 'save')
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

.icon {
  width: 24px;
  height: 24px;
  display: inline-block;
}


</style>