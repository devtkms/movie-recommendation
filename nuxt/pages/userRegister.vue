<template>
  <div class="register-page">
    <Header />
    <div class="register-container">
      <div class="register-card">
        <h1 class="register-title">ユーザー登録</h1>
        <form @submit.prevent="register" class="register-form">
          <div v-for="(item, key) in formItems" :key="key">
            <label :for="key" class="form-label">{{ item.label }}</label>
            <component
                :is="item.type === 'select' ? 'select' : 'input'"
                :type="item.inputType"
                :id="key"
                :required="item.required"
                class="form-input"
                :value="form[key]"
                @input="(e) => form[key] = e.target.value"
                @change="key === 'useProviderName' && updateProviderId()"
            >
              <template v-if="item.type === 'select'">
                <option disabled value="">選択してください</option>
                <option
                    v-for="option in item.options || []"
                    :key="option"
                    :value="option"
                >
                  {{ option }}
                </option>
              </template>
            </component>
          </div>

          <!-- 🎬 好きな映画検索 -->
          <div ref="searchArea">
            <label class="form-label">好きな映画</label>
            <div class="flex gap-2">
              <input
                  v-model="searchQuery"
                  type="text"
                  placeholder="映画名を入力"
                  class="form-input flex-1"
              />
              <button type="button" @click="searchMovies" class="bg-gray-200 rounded px-3 text-sm hover:bg-gray-300">🔍検索</button>
            </div>
            <ul v-if="searchResults.length" class="search-result-list">
              <li
                  v-for="movie in searchResults"
                  :key="movie.id"
                  @click="selectMovie(movie)"
                  class="search-result-item"
              >
                {{ movie.title }}（{{ movie.release_date?.slice(0, 4) || '年不明' }}）
              </li>
            </ul>
          </div>

          <button type="submit" class="submit-button">登録する</button>
        </form>
      </div>
    </div>
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'

const providerMap = {
  'Netflix': 8,
  'Amazon Prime': 9,
  'Disney+': 337,
  'Hulu': 15,
  'U-NEXT': 999,
}

const form = ref({
  email: '',
  password: '',
  nickname: '',
  useProviderName: '',
  useProviderId: null,
  favoriteMovieName: '',
  favoriteMovieId: null,
  gender: '',
  ageGroup: '',
})

const formItems = {
  email: { label: 'Email', type: 'input', inputType: 'email', required: true },
  password: { label: 'パスワード', type: 'input', inputType: 'password', required: true },
  nickname: { label: 'ニックネーム', type: 'input', inputType: 'text', required: true },
  useProviderName: {
    label: '使っている配信サービス',
    type: 'select',
    required: false,
    options: ['Netflix', 'Amazon Prime', 'Disney+', 'Hulu', 'U-NEXT'],
  },
  gender: {
    label: '性別',
    type: 'select',
    required: false,
    options: ['男性', '女性', '回答しない'],
  },
  ageGroup: {
    label: '年代',
    type: 'select',
    required: false,
    options: ['10代', '20代', '30代', '40代', '50代以上'],
  },
}

const updateProviderId = () => {
  form.value.useProviderId = providerMap[form.value.useProviderName] ?? null
}

watch(() => form.value.useProviderName, updateProviderId)

const searchQuery = ref('')
const searchResults = ref([])

const searchMovies = async () => {
  if (!searchQuery.value.trim()) return
  try {
    const { results } = await $fetch('http://localhost:8080/api/search/movies', {
      params: { query: searchQuery.value },
    })
    searchResults.value = results || []
  } catch (err) {
    console.error('検索に失敗しました', err)
    searchResults.value = []
  }
}

const selectMovie = (movie) => {
  form.value.favoriteMovieName = movie.title
  form.value.favoriteMovieId = movie.id
  searchQuery.value = movie.title
  searchResults.value = []
}

const register = async () => {
  try {
    // 入力補完
    if (!form.value.favoriteMovieName && searchQuery.value) {
      form.value.favoriteMovieName = searchQuery.value
    }

    // 🔽 MovieId の補完処理（1件だけ検索結果があれば自動設定）
    if (!form.value.favoriteMovieId && searchQuery.value) {
      const { results } = await $fetch('http://localhost:8080/api/search/movies', {
        params: { query: searchQuery.value },
      })
      if (results?.length === 1) {
        form.value.favoriteMovieId = results[0].id
        form.value.favoriteMovieName = results[0].title
      }
    }

    form.value.useProviderId = providerMap[form.value.useProviderName] ?? null

    console.log('📦 最終送信フォーム', JSON.stringify(form.value, null, 2))

    await $fetch('http://localhost:8080/api/users/register', {
      method: 'POST',
      body: {
        email: form.value.email?.trim() ?? '',
        password: form.value.password ?? '',
        nickname: form.value.nickname?.trim() ?? '',
        useProviderName: form.value.useProviderName ?? '',
        useProviderId: form.value.useProviderId ?? null,
        favoriteMovieName: form.value.favoriteMovieName ?? '',
        favoriteMovieId: form.value.favoriteMovieId ?? null,
        gender: form.value.gender ?? '',
        ageGroup: form.value.ageGroup ?? ''
      }
    })

    alert('登録が完了しました')
  } catch (err) {
    alert('登録に失敗しました')
    console.error('❌ 登録失敗:', err)
  }
}

// 検索候補外クリックで非表示
const searchArea = ref(null)
const handleClickOutside = (e) => {
  if (searchArea.value && !searchArea.value.contains(e.target)) {
    searchResults.value = []
  }
}
onMounted(() => window.addEventListener('click', handleClickOutside))
onBeforeUnmount(() => window.removeEventListener('click', handleClickOutside))
</script>

<style scoped>
.register-page {
  background-color: #f3f4f6;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.register-container {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 1rem 0.5rem;
}

.register-card {
  background-color: white;
  border-radius: 1rem;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.06);
  padding: 1.25rem;
  width: 100%;
  max-width: 460px;
}

.register-title {
  text-align: center;
  font-size: 1.4rem;
  font-weight: 600;
  margin-bottom: 1.25rem;
}

.register-form > div {
  margin-bottom: 0.75rem;
}

.form-label {
  display: block;
  font-size: 0.9rem;
  margin-bottom: 0.2rem;
  color: #374151;
}

.form-input {
  box-sizing: border-box;
  width: 100%;
  padding: 0.45rem 0.7rem;
  border: 1px solid #d1d5db;
  border-radius: 0.5rem;
  font-size: 0.95rem;
  background-color: white;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.form-input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
}

.submit-button {
  width: 100%;
  padding: 0.65rem;
  background-color: #3b82f6;
  color: white;
  font-weight: 600;
  border: none;
  border-radius: 0.5rem;
  transition: background-color 0.2s;
  margin-top: 0.75rem;
}

.submit-button:hover {
  background-color: #2563eb;
}

.search-result-list {
  margin-top: 0.5rem;
  background-color: white;
  border: 1px solid #d1d5db;
  border-radius: 0.5rem;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
  max-height: 200px;
  overflow-y: auto;
}

.search-result-item {
  padding: 0.6rem 1rem;
  border-bottom: 1px solid #e5e7eb;
  cursor: pointer;
  transition: background-color 0.2s;
}

.search-result-item:last-child {
  border-bottom: none;
}

.search-result-item:hover {
  background-color: #f3f4f6;
  border-radius: 0.5rem;
}
</style>