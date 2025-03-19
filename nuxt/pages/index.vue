<template>
  <div class="container">
    <h1 class="title">MoviReco</h1>

    <div v-if="movies.trend.length === 0 && movies.toprated.length === 0">
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

    <div v-if="movies.trend.length > 0 || movies.toprated.length > 0" class="movie-results">

      <!-- 🔥 選択されたオプションを表示 -->
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

      <h2 class="category-title">📈 今話題の映画</h2>
      <div class="movie-list">
        <div v-for="movie in movies.trend" :key="movie.title" class="movie-card">
          <h3 class="movie-title">{{ movie.title }}</h3>
          <img :src="getMoviePoster(movie.posterPath)" alt="映画ポスター" class="movie-poster">
          <div class="overview-container">
            <p v-if="movie.overview">
              <button class="overview-button" @click="showOverview(movie.overview)">概要を見る</button>
            </p>
            <p v-else class="no-overview">概要なし</p>
          </div>
        </div>
      </div>

      <h2 class="category-title">🏆 名作</h2>
      <div class="movie-list">
        <div v-for="movie in movies.toprated" :key="movie.title" class="movie-card">
          <h3 class="movie-title">{{ movie.title }}</h3>
          <img :src="getMoviePoster(movie.posterPath)" alt="映画ポスター" class="movie-poster">
          <div class="overview-container">
            <p v-if="movie.overview">
              <button class="overview-button" @click="showOverview(movie.overview)">概要を見る</button>
            </p>
            <p v-else class="no-overview">この映画の概要情報はありません。</p>
          </div>
        </div>
      </div>
      <button @click="resetSearch" class="search-button">検索画面に戻る</button>
    </div>

    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <p>{{ modalContent }}</p>
        <button @click="closeModal">閉じる</button>
      </div>
    </div>

    <footer class="tmdb-credit">
      <img src="/images/tmdb-logo.png" alt="TMDb Logo" width="100"/>
      <p>This application uses TMDB and the TMDB APIs but is not endorsed, certified, or otherwise approved by TMDB.</p>
      <p>このアプリは TMDb API を使用していますが、TMDb によって承認、認定、またはその他の承認は受けていません。</p>
      <p>
        <NuxtLink to="/privacy">プライバシーポリシー</NuxtLink>
      </p>
    </footer>
  </div>
</template>


<script setup>
import {ref} from 'vue';

const searchOptions = {
  genre: '今の気分を教えてください',
  provider: '配信サービスを選んでください',
  language: '洋画・邦画・韓国映画を選んでください',
};

const options = {
  genre: [
    {value: '35', label: '笑いたい'},
    {value: '18', label: '泣きたい'},
    {value: '53', label: 'ハラハラしたい'},
    {value: '10749', label: 'キュンキュンしたい'}
  ],
  provider: [
    {value: '8', label: 'Netflix'},
    {value: '9', label: 'Amazonプライム'},
    {value: '337', label: 'ディズニープラス'},
    {value: '15', label: 'Hulu'}
  ],
  language: [
    {value: 'en', label: '洋画'},
    {value: 'ja', label: '邦画'},
    {value: 'ko', label: '韓国映画'}
  ]
};

const selectedOptions = ref({
  genre: '',
  provider: '',
  language: ''
});

const movies = ref({
  trend: [],
  toprated: []
});

const loading = ref(false);
const errorMessage = ref("");
const isSearchExhausted = ref(false);
const showModal = ref(false);
const modalContent = ref("");

const showOverview = (overview) => {
  modalContent.value = overview;
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

const getProviderClass = (provider) => {
  return {
    '8': 'netflix',
    '9': 'amazon',
    '337': 'disney',
    '15': 'hulu'
  }[provider] || '';
};

const getGenreClass = (genre) => {
  return {
    '35': 'laugh',
    '18': 'cry',
    '53': 'thrill',
    '10749': 'romance'
  }[genre] || '';
};

const getLanguageClass = (language) => {
  return {
    'en': 'western',
    'ja': 'japanese',
    'ko': 'korean'
  }[language] || '';
};

// 選択肢のラベルを取得する関数
const getGenreLabel = (genre) => {
  return options.genre.find(opt => opt.value === genre)?.label || "未選択";
};

const getProviderLabel = (provider) => {
  return options.provider.find(opt => opt.value === provider)?.label || "未選択";
};

const getLanguageLabel = (language) => {
  return options.language.find(opt => opt.value === language)?.label || "未選択";
};

const generateStorageKey = () => {
  return `movies_genre_${selectedOptions.value.genre}_provider_${selectedOptions.value.provider}_language_${selectedOptions.value.language}`;
};

const fetchMovies = async () => {
  if (!selectedOptions.value.genre || !selectedOptions.value.provider || !selectedOptions.value.language) {
    errorMessage.value = "必須の質問に回答してください。";
    return;
  }

  loading.value = true;
  movies.value = {trend: [], toprated: []};
  errorMessage.value = "";
  isSearchExhausted.value = false;

  const storageKey = generateStorageKey();
  let storedMovies = JSON.parse(localStorage.getItem(storageKey) || '{}');

  // 🔥 キャッシュがある場合はそれを使用
  if (storedMovies.trend && storedMovies.toprated) {
    // **ローカルストレージから取得する前に先頭の映画を削除する**
    const nextTrend = storedMovies.trend.length > 0 ? storedMovies.trend.shift() : null;
    const nextTopRated = storedMovies.toprated.length > 1 ? [storedMovies.toprated.shift(), storedMovies.toprated.shift()] : [];

    localStorage.setItem(storageKey, JSON.stringify(storedMovies));

    if (!nextTrend && nextTopRated.length === 0) {
      isSearchExhausted.value = true;
      loading.value = false;
      return;
    }

    movies.value = {
      trend: nextTrend ? [nextTrend] : [],
      toprated: nextTopRated
    };

    loading.value = false;
    return; // 🔥 ここで処理を終了し、APIリクエストを送らない
  }

  // 🔥 キャッシュがない場合はAPIリクエスト
  try {
    // const response = await fetch(`${config.public.apiBase}/movies`,{
    const response = await fetch(`http://localhost:8080/api/movies`, {
    // const response = await fetch(`https://movie-recommendation-uybc.onrender.com/api/movies`, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(selectedOptions.value),
    });

    if (!response.ok) throw new Error("API リクエストが失敗しました");

    const data = await response.json();

    if (!data.trend.length && !data.toprated.length) {
      errorMessage.value = "検索結果がありませんでした。";
    } else {
      // **キャッシュを保存する前に、取得データをコピー**
      const storedData = { ...data };

      // 🔥 1回目に表示する映画を取り出し、残りをキャッシュに保存
      const firstTrend = storedData.trend.length > 0 ? storedData.trend.shift() : null;
      const firstTopRated = storedData.toprated.length > 1 ? [storedData.toprated.shift(), storedData.toprated.shift()] : [];

      localStorage.setItem(storageKey, JSON.stringify(storedData));

      movies.value = {
        trend: firstTrend ? [firstTrend] : [],
        toprated: firstTopRated
      };
    }
  } catch (error) {
    console.error("❌ 映画データの取得に失敗:", error);
    errorMessage.value = "映画データの取得に失敗しました。しばらくしてから再試行してください。";
  }
  loading.value = false;
};

const resetSearch = () => {
  movies.value = {trend: [], toprated: []};
};

const getMoviePoster = (path) => {
  return path ? `https://image.tmdb.org/t/p/w500${path}` : 'https://via.placeholder.com/500';
};
</script>

<style scoped>

/* 🌟 共通スタイル */
.container {
  max-width: 600px;
  margin: auto;
  text-align: center;
}

.title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 20px;
}

/* 🔹 フォーム関連 */
.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  font-weight: bold;
  margin-bottom: 8px;
}

.button-group {
  display: grid;
  grid-template-columns: repeat(2, auto);
  gap: 10px;
  justify-content: center;
}

/* 🔥 ボタンスタイル */
.button {
  padding: 8px 12px;
  color: white;
  border: none;
  cursor: pointer;
  margin-top: 5px;
  border-radius: 8px;
  min-width: 140px;
  text-align: center;
  transition: opacity 0.2s;
}

.button:hover {
  opacity: 0.85;
}

button.selected {
  background-color: grey;
  font-weight: bold;
  opacity: 0.9;
}

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

/* 🎨 配信サービス別ボタン */
.netflix { background-color: #E50914; }
.amazon { background-color: #00A8E1; }
.disney { background-color: #113CCF; }
.hulu { background-color: #1CE783; }

/* 🎭 ジャンル別ボタン */
.laugh { background-color: #E50914; }
.cry { background-color: #1E90FF; }
.thrill { background-color: #FF4500; }
.romance { background-color: #FF1493; }

/* 🌍 言語別ボタン */
.western { background-color: #DAA520; }
.japanese { background-color: #C70039; }
.korean { background-color: #003366; }

/* 🔍 検索ボタン */
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
}

.search-button:hover {
  background-color: #555;
}

.search-button:disabled {
  background-color: #999;
  cursor: not-allowed;
}

/* 🎬 映画リスト・カード関連 */
.movie-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  width: 100%;
}

.movie-card {
  background-color: #f8f8ff;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 15px;
  text-align: center;
  width: 280px;
  max-width: 320px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.movie-title {
  font-size: 16px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 10px;
}

.movie-poster {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
}

/* ℹ️ 映画の追加情報 */
.movie-info {
  font-size: 14px;
  color: #333;
  margin: 5px 0;
}

/* 📜 概要関連 */
.overview-container {
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

/* 📢 エラーメッセージ */
.error-message {
  color: red;
  font-weight: bold;
  text-align: center;
  margin-top: 10px;
  font-size: 14px;
}

/* 📌 選択されたオプション */
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

/* 🏆 セクションタイトル */
.category-title {
  width: 100%;
  text-align: center;
  font-size: 25px;
  font-weight: bold;
  margin-top: 30px;
  margin-bottom: 20px;
}

/* 📌 モーダル関連 */
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

/* 📢 TMDb クレジット表示 */
.tmdb-credit {
  text-align: center;
  font-size: 11px;
  padding: 15px 0;
  margin-top: 25px;
  line-height: 1.6;
  border-top: 1px solid #ccc;
  padding-top: 20px;
  background-color: #f8f9fa;
}

</style>