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

    <div v-if="movies.trend.length > 0 || movies.toprated.length > 0" class="movie-list">
      <h2 class="category-title">📈 今話題の映画</h2>
      <ul>
        <li v-if="movies.trend.length > 0">
          <h3>{{ movies.trend[0].title }}</h3>
          <img :src="getMoviePoster(movies.trend[0].posterPath)" alt="映画ポスター">
          <p>{{ movies.trend[0].overview }}</p>
        </li>
      </ul>

      <h2 class="category-title">🏆 名作</h2>
      <ul>
        <li v-if="movies.toprated.length > 0">
          <h3>{{ movies.toprated[0].title }}</h3>
          <img :src="getMoviePoster(movies.toprated[0].posterPath)" alt="映画ポスター">
          <p>{{ movies.toprated[0].overview }}</p>
        </li>
        <li v-if="movies.toprated.length > 1">
          <h3>{{ movies.toprated[1].title }}</h3>
          <img :src="getMoviePoster(movies.toprated[1].posterPath)" alt="映画ポスター">
          <p>{{ movies.toprated[1].overview }}</p>
        </li>
      </ul>

      <button @click="resetSearch" class="search-button">検索画面に戻る</button>
    </div>

    <footer class="tmdb-credit">
      <img src="/images/tmdb-logo.png" alt="TMDb Logo" width="100"/>
      <p>このアプリは TMDb API を使用していますが、TMDb によって承認、認定、またはその他の承認は受けていません。</p>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const searchOptions = {
  genre: '今の気分を教えてください',
  provider: '配信サービスを選んでください',
  language: '洋画・邦画を選んでください',
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

const categoryTitles = {
  trend: "📈 今話題の映画",
  toprated: "🏆 名作"
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

const generateStorageKey = () => {
  return `movies_genre_${selectedOptions.value.genre}_provider_${selectedOptions.value.provider}_language_${selectedOptions.value.language}`;
};

const fetchMovies = async () => {
  if (!selectedOptions.value.genre || !selectedOptions.value.provider || !selectedOptions.value.language) {
    errorMessage.value = "必須の質問に回答してください。";
    return;
  }

  loading.value = true;
  movies.value = { trend: [], toprated: [] };
  errorMessage.value = "";
  isSearchExhausted.value = false;

  const storageKey = generateStorageKey();
  let storedMovies = JSON.parse(localStorage.getItem(storageKey) || '{}');

  // 🔥 キャッシュがある場合はそれを使用
  if (storedMovies.trend && storedMovies.toprated) {
    movies.value = {
      trend: storedMovies.trend.length > 0 ? [storedMovies.trend.shift()] : [],
      toprated: storedMovies.toprated.length > 1 ? [storedMovies.toprated.shift(), storedMovies.toprated.shift()] : []
    };

    localStorage.setItem(storageKey, JSON.stringify(storedMovies));

    if (!storedMovies.trend.length && !storedMovies.toprated.length) {
      isSearchExhausted.value = true;
      movies.value = { trend: [], toprated: [] };
    }
    loading.value = false;
    return;  // 🔥 ここで処理を終了し、APIリクエストを送らない
  }

  // 🔥 キャッシュがない場合はAPIリクエスト
  try {
    // const response = await fetch(`${config.public.apiBase}/movies`,{
    // const response = await fetch(`http://localhost:8080/api/movies`, {
      const response = await fetch(`https://movie-recommendation-uybc.onrender.com/api/movies`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(selectedOptions.value),
    });

    if (!response.ok) throw new Error("API リクエストが失敗しました");

    const data = await response.json();

    if (!data.trend.length && !data.toprated.length) {
      errorMessage.value = "検索結果がありませんでした。";
    } else {
      movies.value = {
        trend: data.trend.length > 0 ? [data.trend[0]] : [],
        toprated: data.toprated.length > 1 ? [data.toprated[0], data.toprated[1]] : []
      };

      localStorage.setItem(storageKey, JSON.stringify(data)); // 🔥 キャッシュを保存
    }
  } catch (error) {
    console.error("❌ 映画データの取得に失敗:", error);
    errorMessage.value = "映画データの取得に失敗しました。しばらくしてから再試行してください。";
  }
  loading.value = false;
};

const resetSearch = () => {
  movies.value = { trend: [], toprated: [] };
};

const getMoviePoster = (path) => {
  return path ? `https://image.tmdb.org/t/p/w500${path}` : 'https://via.placeholder.com/500';
};
</script>

<style scoped>
.container {
  max-width: 600px;
  margin: auto;
  text-align: center;
}

.title {
  font-size: 28px;
  margin-bottom: 20px;
  font-weight: bold;
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
  margin-top: 20px;
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

.error-message {
  color: red;
  text-align: center;
  font-weight: bold;
  margin-top: 10px;
  font-size: 14px;
}

.tmdb-credit {
  text-align: center;
  font-size: 11px;
  padding: 15px 0;
  margin-top: 25px;
  line-height: 1.6;
  border-top: 1px solid #ccc; /* 上に1pxの線を追加 */
  padding-top: 20px;
  background-color: #f8f9fa; /* 超薄いグレー */
}

.tmdb-logo {
  width: 160px;
  display: block;
  margin: 15px auto;
}

.search-button {
  background-color: #333333;
  color: white;
  font-size: 16px;
  font-weight: bold;
  padding: 12px 24px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}

.search-button:hover {
  background-color: #555555;
}

.search-button:disabled {
  background-color: #999999;
  cursor: not-allowed;
}
</style>