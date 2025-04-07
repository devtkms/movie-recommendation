<template>
  <div class="register-page">
    <Header />
    <div class="register-container">
      <div class="register-card">
        <h1 class="register-title">ユーザー登録</h1>
        <template v-if="isComplete">
          <div class="text-center text-green-700">
            <p class="text-xl font-semibold mb-2">登録が完了しました！</p>
            <p class="text-sm text-gray-700 mb-4">
              MoviRecoを使って、あなたにぴったりの映画を見つけてみましょう🎬
            </p>
            <NuxtLink to="/" class="home-link-button">
              ホーム画面へ戻る
            </NuxtLink>
          </div>
        </template>
        <form v-else @submit.prevent="isConfirm ? submitForm() : goToConfirm()" class="register-form">
          <div v-if="errorRequiredFields" class="tooltip-error mb-4">
            <span class="tooltip-icon">⚠</span>
            {{ errorRequiredFields }}
          </div>
          <template v-if="!isConfirm">

            <div v-for="(item, key) in formItems" :key="key">
              <label :for="key" class="form-label">
                {{ item.label }}
                <span v-if="item.required" class="required-mark">※</span>
              </label>
              <component
                  :is="item.type === 'select' ? 'select' : 'input'"
                  :type="item.inputType"
                  :id="key"
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

            <div ref="searchArea">
              <label class="form-label">好きな映画</label>
              <div class="flex items-center gap-x-4">
                <input
                    v-model="searchQuery"
                    type="text"
                    placeholder="映画名を入力"
                    class="form-input w-full max-w-[280px]"
                />
                <button
                    type="button"
                    @click="searchMovies"
                    class="search-button"
                >
                  🔍 検索
                </button>
              </div>
              <div v-if="errorFavoriteMovie" class="tooltip-error mt-1">
                <span class="tooltip-icon">⚠</span>
                {{ errorFavoriteMovie }}
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

            <div class="checkbox-area">
              <label class="checkbox-label">
                <input type="checkbox" v-model="agreed" />
                <span>
      <NuxtLink to="/terms" class="terms-link" target="_blank">
        利用規約
      </NuxtLink>
      に同意する
    </span>
              </label>
              <div v-if="errorAgree" class="tooltip-error mt-1">
                <span class="tooltip-icon">⚠</span>
                {{ errorAgree }}
              </div>
            </div>

            <div class="button-wrapper">
              <button type="submit" class="submit-button">確認する</button>
            </div>
          </template>

          <template v-else>
            <div class="space-y-2 text-sm text-gray-700">
              <p><strong>Email:</strong> {{ form.email }}</p>
              <p><strong>パスワード:</strong> ●●●●（非表示）</p>
              <p><strong>ニックネーム:</strong> {{ form.nickname }}</p>
              <p><strong>配信サービス:</strong> {{ form.useProviderName }}</p>
              <p><strong>映画タイトル:</strong> {{ form.favoriteMovieName }}</p>
              <p><strong>性別:</strong> {{ form.gender }}</p>
              <p><strong>年代:</strong> {{ form.ageGroup }}</p>
            </div>

            <div class="button-wrapper flex mt-4 justify-center gap-4">
              <button
                  type="button"
                  class="submit-button bg-red-500 hover:bg-red-600"
                  @click="isConfirm = false"
              >
                戻る
              </button>
              <button
                  type="submit"
                  class="submit-button !bg-emerald-500 hover:!bg-emerald-600"
              >
                登録する
              </button>
            </div>
          </template>
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

const agreed = ref(false)

const isConfirm = ref(false)
const isComplete = ref(false)
const errorMessage = ref('')
const errorRequiredFields = ref('')
const errorFavoriteMovie = ref('')
const errorAgree = ref('')

const config = useRuntimeConfig()
const apiBase = config.public.apiBase

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
    const { results } = await $fetch(`${apiBase}/api/search/movies`, {
      params: { query: searchQuery.value }
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

const validateEmail = (email) => {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return re.test(email)
}

const validatePassword = (password) => {
  return password.length >= 6 && /[a-zA-Z]/.test(password) && /[0-9]/.test(password)
}

const goToConfirm = () => {
  errorRequiredFields.value = ''
  errorFavoriteMovie.value = ''
  errorAgree.value = ''

  if (!form.value.email || !form.value.password || !form.value.nickname) {
    errorRequiredFields.value = '必須項目を入力してください'
    return
  }

  if (!validateEmail(form.value.email)) {
    errorRequiredFields.value = '正しいメールアドレス形式で入力してください'
    return
  }

  if (!validatePassword(form.value.password)) {
    errorRequiredFields.value = 'パスワードは6文字以上の英数字を含めてください'
    return
  }

  if (!agreed.value) {
    errorAgree.value = '利用規約への同意が必要です'
    return
  }

  const hasMovieNameInput = form.value.favoriteMovieName.trim().length > 0 || searchQuery.value.trim().length > 0
  const hasMovieId = !!form.value.favoriteMovieId

  if (hasMovieNameInput && !hasMovieId) {
    errorFavoriteMovie.value = '検索ボタンを押して候補から映画を選択してください'
    return
  }

  if (!hasMovieNameInput) {
    form.value.favoriteMovieName = ''
    form.value.favoriteMovieId = null
  }

  form.value.useProviderId = providerMap[form.value.useProviderName] ?? null
  isConfirm.value = true
}

const submitForm = async () => {
  try {
    const response = await $fetch(`${apiBase}/api/users/register`, {
      method: 'POST',
      body: { ...form.value },
      credentials: 'include'
    })

    localStorage.setItem('id', response.id)
    localStorage.setItem('nickname', response.nickname)

    isConfirm.value = false
    isComplete.value = true
  } catch (err) {
    const message = err?.data || '登録に失敗しました'
    errorRequiredFields.value = message
    isConfirm.value = false
    console.error('❌ 登録失敗:', err)
  }
}

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

.search-button {
  background-color: #10b981; /* エメラルドグリーン */
  color: white;
  font-size: 0.9rem;
  padding: 0.45rem 0.95rem;
  border: none;
  border-radius: 0.5rem;
  font-weight: 600;
  transition: background-color 0.2s ease;
  white-space: nowrap;
}

.search-button:hover {
  background-color: #059669; /* 濃いめのグリーン */
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

.required-mark {
  color: #dc2626;
  margin-left: 4px;
  font-weight: bold;
}

.submit-button {
  display: inline-block; /* ← これで幅を自然に */
  background-color: #3b82f6;
  color: white;
  font-size: 0.95rem;
  padding: 0.55rem 1.25rem;
  font-weight: 600;
  border: none;
  border-radius: 0.5rem;
  transition: background-color 0.2s ease;
  margin-top: 0.75rem;
}

.submit-button:hover {
  background-color: #2563eb;
}

.button-wrapper {
  text-align: center;
}

.tooltip-error {
  position: relative;
  background-color: #fef3c7; /* 薄い黄色 */
  border: 1px solid #fcd34d; /* 黄色枠 */
  color: #92400e; /* 警告文字色 */
  font-size: 0.85rem;
  padding: 0.4rem 0.75rem;
  border-radius: 0.5rem;
  margin-top: 0.5rem;
  display: flex;
  align-items: center;
  gap: 0.4rem;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
  max-width: 300px;
}

.tooltip-icon {
  font-weight: bold;
}

.home-link-button {
  display: inline-block;
  background-color: #10b981; /* エメラルドグリーン */
  color: white;
  font-size: 0.9rem;
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  font-weight: 600;
  transition: background-color 0.2s ease;
  text-decoration: none;
}

.home-link-button:hover {
  background-color: #059669;
}
</style>