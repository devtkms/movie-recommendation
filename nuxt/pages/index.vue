<template>
  <div class="container">
    <Header />
    <IntroModal v-if="showIntroModal" @close="closeIntroModal" />

    <div v-if="!currentMovie">
      <div class="form-group" v-for="(label, key) in searchOptions" :key="key">
        <label>{{ label }}</label>
        <div class="button-group">
          <button
              v-for="option in options[key]"
              :key="option.value"
              :class="[
              'button',
              key === 'genre' ? getGenreClass(option.value) : '',
              key === 'provider' ? getProviderClass(option.value) : '',
              key === 'language' ? getLanguageClass(option.value) : '',
              { selected: selectedOptions[key] === option.value }
            ]"
              @click="selectedOptions[key] = option.value"
          >
            {{ option.label }}
          </button>
        </div>
      </div>

      <div class="filter-toggle" @click="showFilters = !showFilters">
        <span>{{ showFilters ? '▲ フィルターを閉じる' : '▼ フィルターを開く' }}</span>
      </div>

      <div v-show="showFilters">
        <div class="checkbox-wrapper providers">
          <label class="checkbox-label" v-for="option in options.provider" :key="option.value">
            <input type="checkbox" :value="option.value" v-model="selectedOptions.providers" />
            {{ option.label }}
          </label>
        </div>

        <div class="checkbox-wrapper languages">
          <label class="checkbox-label" v-for="option in options.language" :key="option.value">
            <input type="checkbox" :value="option.value" v-model="selectedOptions.languages" />
            {{ option.label }}
          </label>
        </div>
      </div>

      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      <p v-if="isSearchExhausted" class="exhausted-message">この条件での検索結果はすべて表示されました。</p>

      <button @click="fetchMovies" :disabled="loading" class="search-button">映画を探す</button>
    </div>

    <div v-if="loading">ロード中...</div>

    <div v-if="currentMovie" class="movie-results">
      <div class="selected-options">
        <div class="selected-option" :class="getGenreClass(selectedOptions.genre)">
          {{ getGenreLabel(selectedOptions.genre) }}
        </div>
        <div class="selected-option" :class="getProviderClass(selectedOptions.provider)">
          {{ getProviderLabel(selectedOptions.provider) }}
        </div>
        <div class="selected-option" :class="getLanguageClass(selectedOptions.language)">
          {{ getLanguageLabel(selectedOptions.language) }}
        </div>
      </div>

      <div
          class="movie-card"
          @touchstart="onTouchStart"
          @touchmove="onTouchMove"
          @touchend="onTouchEnd"
          :style="cardStyle"
      >
        <h3 class="movie-title">{{ currentMovie.title }}</h3>
        <div class="poster-wrapper">
          <ArrowLeftCircleIcon class="icon-left" />
          <img :src="getMoviePoster(currentMovie.posterPath)" alt="映画ポスター" class="movie-poster fixed-size" />
          <ArrowRightCircleIcon class="icon-right" />
        </div>
        <div class="overview-container">
          <p v-if="currentMovie.overview">
            <button class="overview-button" @click="showOverview(currentMovie.overview)">概要を見る</button>
          </p>
          <p v-else class="no-overview">この映画の概要情報はありません。</p>
        </div>
      </div>

      <button @click="resetSearch" class="search-button">検索画面に戻る</button>
    </div>

    <OverviewModal :show="showModal" :content="modalContent" @close="closeModal" />
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import Header from '~/components/Header.vue';
import Footer from '~/components/Footer.vue';
import OverviewModal from '~/components/OverviewModal.vue';
import { ArrowLeftCircleIcon, ArrowRightCircleIcon } from '@heroicons/vue/24/solid';

const showIntroModal = ref(false);
const showFilters = ref(true);

onMounted(() => {
  const hasVisited = localStorage.getItem('visited');
  if (!hasVisited) {
    showIntroModal.value = true;
    localStorage.setItem('visited', 'true');
  }
});

const closeIntroModal = () => {
  showIntroModal.value = false;
};

const searchOptions = {
  genre: '今の気分を教えてください',
  provider: '配信サービスを選んでください',
  language: '洋画・邦画・韓国映画を選んでください',
};

const options = {
  genre: [
    { value: '35', label: '笑いたい' },
    { value: '18', label: '泣きたい' },
    { value: '53', label: 'ハラハラしたい' },
    { value: '10749', label: 'キュンキュンしたい' }
  ],
  provider: [
    { value: '8', label: 'Netflix' },
    { value: '9', label: 'Amazonプライム' },
    { value: '337', label: 'ディズニープラス' },
    { value: '15', label: 'Hulu' }
  ],
  language: [
    { value: 'en', label: '洋画' },
    { value: 'ja', label: '邦画' },
    { value: 'ko', label: '韓国映画' }
  ]
};

const selectedOptions = ref({ genre: '', provider: '', language: '', providers: [], languages: [] });
const currentMovie = ref(null);
const moviePool = ref([]);
const currentIndex = ref(0);
const loading = ref(false);
const errorMessage = ref("");
const isSearchExhausted = ref(false);
const showModal = ref(false);
const modalContent = ref("");

const touchStartX = ref(0);
const touchCurrentX = ref(0);
const isSwiping = ref(false);

const cardStyle = computed(() => {
  const dx = touchCurrentX.value - touchStartX.value;
  return isSwiping.value ? `transform: translateX(${dx}px) rotate(${dx / 20}deg); transition: none;` : '';
});

const onTouchStart = (e) => {
  touchStartX.value = e.touches[0].clientX;
  touchCurrentX.value = touchStartX.value;
  isSwiping.value = true;
};

const onTouchMove = (e) => {
  touchCurrentX.value = e.touches[0].clientX;
};

const onTouchEnd = () => {
  const dx = touchCurrentX.value - touchStartX.value;
  if (Math.abs(dx) > 80) {
    dx > 0 ? prevMovie() : nextMovie();
  }
  isSwiping.value = false;
  touchStartX.value = 0;
  touchCurrentX.value = 0;
};

const showOverview = (overview) => {
  modalContent.value = overview;
  showModal.value = true;
};

const closeModal = () => showModal.value = false;

const getMoviePoster = (path) =>
    path ? `https://image.tmdb.org/t/p/w500${path}` : 'https://via.placeholder.com/500';

const getGenreLabel = (genre) => options.genre.find(opt => opt.value === genre)?.label || "未選択";
const getProviderLabel = (provider) => options.provider.find(opt => opt.value === provider)?.label || "未選択";
const getLanguageLabel = (language) => options.language.find(opt => opt.value === language)?.label || "未選択";

const getGenreClass = (genre) => ({
  '35': 'laugh',
  '18': 'cry',
  '53': 'thrill',
  '10749': 'romance'
}[genre] || '');

const getProviderClass = (provider) => ({
  '8': 'netflix',
  '9': 'amazon',
  '337': 'disney',
  '15': 'hulu'
}[provider] || '');

const getLanguageClass = (language) => ({
  'en': 'western',
  'ja': 'japanese',
  'ko': 'korean'
}[language] || '');

const generateStorageKey = () =>
    `movies_genre_${selectedOptions.value.genre}_provider_${selectedOptions.value.provider}_language_${selectedOptions.value.language}`;

const nextMovie = () => {
  if (currentIndex.value < moviePool.value.length - 1) {
    currentIndex.value++;
    currentMovie.value = moviePool.value[currentIndex.value];
  } else {
    isSearchExhausted.value = true;
  }
};

const prevMovie = () => {
  if (currentIndex.value > 0) {
    currentIndex.value--;
    currentMovie.value = moviePool.value[currentIndex.value];
    isSearchExhausted.value = false;
  }
};

const fetchMovies = async () => {
  if (!selectedOptions.value.genre || !selectedOptions.value.provider || !selectedOptions.value.language) {
    errorMessage.value = "必須の質問に回答してください。";
    return;
  }

  loading.value = true;
  errorMessage.value = "";
  isSearchExhausted.value = false;
  currentMovie.value = null;

  const storageKey = generateStorageKey();
  const today = new Date().toISOString().slice(0, 10);
  const stored = JSON.parse(localStorage.getItem(storageKey) || '{}');

  if (stored.pool && stored.savedDate === today) {
    moviePool.value = stored.pool;
    currentIndex.value = stored.index || 0;
    currentMovie.value = moviePool.value[currentIndex.value];
    loading.value = false;
    return;
  }

  try {
    const response = await fetch(`https://movie-recommendation-uybc.onrender.com/api/movies`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(selectedOptions.value),
    });

    if (!response.ok) throw new Error("API リクエストが失敗しました");
    const data = await response.json();

    const combined = [...(data.combined || [])];
    for (let i = combined.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [combined[i], combined[j]] = [combined[j], combined[i]];
    }

    moviePool.value = combined;
    currentIndex.value = 0;
    currentMovie.value = moviePool.value[0];
    localStorage.setItem(storageKey, JSON.stringify({ pool: combined, index: 0, savedDate: today }));
  } catch (error) {
    console.error("❌ 映画データの取得に失敗:", error);
    errorMessage.value = "映画データの取得に失敗しました。しばらくしてから再試行してください。";
  }

  loading.value = false;
};

const resetSearch = () => {
  moviePool.value = [];
  currentMovie.value = null;
  currentIndex.value = 0;
  isSearchExhausted.value = false;
};
</script>

<!-- CSSは別ファイル or style scoped にて対応中 -->


<style scoped>
.container {
  max-width: 600px;
  margin: auto;
  text-align: center;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
}

.button-group {
  display: grid;
  grid-template-columns: repeat(2, auto);
  gap: 10px;
  justify-content: center;
}

.button {
  padding: 8px 12px;
  color: white;
  border: none;
  cursor: pointer;
  margin-top: 5px;
  border-radius: 8px;
  min-width: 140px;
  text-align: center;
}

button.selected {
  background-color: grey;
  font-weight: bold;
  opacity: 0.9;
}

.button:hover {
  opacity: 0.85;
}

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.movie-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  width: 100%;
}

.movie-list img {
  max-width: 100px;
  display: block;
  margin: auto;
}

.movie-list ul {
  list-style-type: none;
  padding: 0;
}

.movie-title {
  font-size: 16px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 10px;
}

.movie-results {
  text-align: center;
  padding-bottom: 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.movie-card {
  background-color: #f8f8ff;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 15px;
  width: 280px;
  max-width: 320px;
  height: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  transition: transform 0.3s ease;
}

.movie-poster.fixed-size {
  width: 100%;
  max-width: 220px;
  height: 320px;
  object-fit: cover;
  border-radius: 8px;
}

.movie-poster {
  max-width: 80%;
  height: auto;
  border-radius: 8px;
  display: block;
  margin: auto;
}

@media (max-width: 600px) {
  .movie-poster {
    max-width: 150px;
  }
}

.error-message {
  color: red;
  text-align: center;
  font-weight: bold;
  margin-top: 10px;
  font-size: 14px;
}

.search-button {
  background-color: #333;
  color: white;
  font-size: 16px;
  font-weight: bold;
  padding: 12px 24px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s ease-in-out;
  margin-top: 60px;
}

.search-button:hover {
  background-color: #555;
}

.search-button:disabled {
  background-color: #999;
  cursor: not-allowed;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  max-width: 400px;
}

.modal-content p {
  margin-bottom: 10px;
}

.overview-container {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 40px;
}

.no-overview {
  color: #777;
  font-style: italic;
  margin-top: 5px;
}

.overview-button {
  background-color: #007BFF;
  color: white;
  font-size: 14px;
  padding: 8px 16px;
  border-radius: 5px;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s ease-in-out;
}

.overview-button:hover {
  background-color: #0056b3;
}

.category-title {
  width: 100%;
  text-align: center;
  font-size: 25px;
  font-weight: bold;
  margin-top: 30px;
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.selected-options {
  display: flex;
  justify-content: space-between;
  width: 100%;
  max-width: 600px;
  margin: 0 auto 15px;
}

.selected-option {
  flex: 1;
  max-width: 200px;
  min-width: 100px;
  padding: 8px 12px;
  color: white;
  font-size: 14px;
  font-weight: bold;
  border-radius: 8px;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: default;
  opacity: 0.9;
  border: none;
  white-space: nowrap;
}

/* 🎨 オプション別カラー */
.netflix { background-color: #E50914; }
.amazon { background-color: #00A8E1; }
.disney { background-color: #113CCF; }
.hulu { background-color: #1CE783; }

.laugh { background-color: #E50914; }
.cry { background-color: #1E90FF; }
.thrill { background-color: #FF4500; }
.romance { background-color: #FF1493; }

.western { background-color: #DAA520; }
.japanese { background-color: #C70039; }
.korean { background-color: #003366; }

.icon-left,
.icon-right {
  width: 32px;
  height: 32px;
  color: rgba(100, 100, 100, 0.4);
}

.poster-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

/* ✅ チェックボックス表示用追加 */
.checkbox-wrapper {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
  margin-top: 12px;
  font-size: 14px;
}

.checkbox-wrapper.providers,
.checkbox-wrapper.languages {
  display: grid;
  grid-template-columns: 140px 140px;
  column-gap: 20px;
  row-gap: 8px;
  justify-content: center;
  margin-top: 24px;
  padding-top: 4px;
  max-width: 300px;
  margin-left: auto;
  margin-right: auto;
}

/* ✅ 各チェックボックス：左揃え */
.checkbox-label {
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
  justify-content: flex-start;
  white-space: nowrap;
}

.filter-toggle {
  text-align: left;          /* ← 左寄せに変更 */
  font-weight: bold;
  font-size: 16px;
  margin: 24px auto 10px;
  padding-left: 20px;        /* ← 左に少し余白 */
  cursor: pointer;
  color: #333;
  user-select: none;
  max-width: 300px;          /* ← 中央寄せの最大幅に合わせる */
}

.filter-toggle:hover {
  opacity: 0.8;
}
</style>