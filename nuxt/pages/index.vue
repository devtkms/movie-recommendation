<template>
  <div class="container">
    <Header />
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
        <img :src="getMoviePoster(currentMovie.posterPath)" alt="映画ポスター" class="movie-poster fixed-size" />
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
import { ref, computed } from 'vue';
import Header from '~/components/Header.vue';
import Footer from '~/components/Footer.vue';
import OverviewModal from '~/components/OverviewModal.vue';

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

const selectedOptions = ref({ genre: '', provider: '', language: '' });
const currentMovie = ref(null);
const moviePool = ref([]);
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
  return isSwiping.value
      ? `transform: translateX(${dx}px) rotate(${dx / 20}deg); transition: none;`
      : '';
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
    nextMovie();
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

const getMoviePoster = (path) => path ? `https://image.tmdb.org/t/p/w500${path}` : 'https://via.placeholder.com/500';

const getGenreLabel = (genre) => options.genre.find(opt => opt.value === genre)?.label || "未選択";
const getProviderLabel = (provider) => options.provider.find(opt => opt.value === provider)?.label || "未選択";
const getLanguageLabel = (language) => options.language.find(opt => opt.value === language)?.label || "未選択";

const getGenreClass = (genre) => {
  return {
    '35': 'laugh',
    '18': 'cry',
    '53': 'thrill',
    '10749': 'romance'
  }[genre] || '';
};

const getProviderClass = (provider) => {
  return {
    '8': 'netflix',
    '9': 'amazon',
    '337': 'disney',
    '15': 'hulu'
  }[provider] || '';
};

const getLanguageClass = (language) => {
  return {
    'en': 'western',
    'ja': 'japanese',
    'ko': 'korean'
  }[language] || '';
};

const generateStorageKey = () => `movies_genre_${selectedOptions.value.genre}_provider_${selectedOptions.value.provider}_language_${selectedOptions.value.language}`;

const nextMovie = () => {
  if (moviePool.value.length === 0) {
    isSearchExhausted.value = true;
    currentMovie.value = null;
    return;
  }
  const index = Math.floor(Math.random() * moviePool.value.length);
  currentMovie.value = moviePool.value.splice(index, 1)[0];
  localStorage.setItem(generateStorageKey(), JSON.stringify({ pool: moviePool.value }));
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
  let stored = JSON.parse(localStorage.getItem(storageKey) || '{}');

  if (stored.pool && stored.pool.length > 0) {
    moviePool.value = stored.pool;
    nextMovie();
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
    const combined = [...(data.trend || []), ...(data.toprated || [])];
    moviePool.value = [...combined];
    localStorage.setItem(storageKey, JSON.stringify({ pool: combined }));
    nextMovie();
  } catch (error) {
    console.error("❌ 映画データの取得に失敗:", error);
    errorMessage.value = "映画データの取得に失敗しました。しばらくしてから再試行してください。";
  }
  loading.value = false;
};

const resetSearch = () => {
  moviePool.value = [];
  currentMovie.value = null;
  isSearchExhausted.value = false;
};
</script>



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
  gap: 20px; /* カード間のスペース */
  justify-content: center; /* 🔥 カードを中央に配置 */
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

  /* ↓ 追加（中央揃え用） */
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

  /* ✨ 高さはautoでOK（中身に応じて伸縮） */
  height: auto;

  display: flex;
  flex-direction: column;
  align-items: center;

  /* ✨ 余白調整 */
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
  max-width: 80%; /* 🔥 見やすいバランスに調整 */
  height: auto; /* 🔥 縦横比を維持 */
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
  margin-top: 60px; /* 🔥 もっと下に下げる */
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
  width: 100%; /* 🔥 タイトルがコンテナ内で適切に表示される */
  text-align: center; /* 🔥 タイトルを中央揃え */
  font-size: 25px;
  font-weight: bold;
  margin-top: 30px;
  margin-bottom: 20px;
  display: flex;
  justify-content: center; /* 🔥 タイトルを中央配置 */
  align-items: center;
}

.category-title {
  font-size: 30px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 10px;
}

.selected-options {
  display: flex;
  justify-content: space-between; /* 🔥 均等配置 */
  width: 100%; /* 🔥 横幅いっぱい */
  max-width: 600px; /* 🔥 コンテナ幅を統一 */
  margin: 0 auto 15px; /* 🔥 中央配置 */
}

.selected-option {
  flex: 1; /* 🔥 各要素を均等幅に */
  max-width: 200px; /* 🔥 最大幅 */
  min-width: 100px; /* 🔥 最小幅 */
  padding: 8px 12px; /* 🔥 ボタンのサイズ統一 */
  color: white;
  font-size: 14px; /* 🔽 文字サイズを少し小さくする */
  font-weight: bold;
  border-radius: 8px;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: default;
  opacity: 0.9;
  border: none;
  white-space: nowrap; /* 🔥 折り返し防止 */
}

/* 🎨 各オプションの色（ボタンと統一） */
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
</style>