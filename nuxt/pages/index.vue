<template>
  <div class="container">
    <h1 class="title">MoviReco</h1>

    <div v-if="movies.length === 0">
      <div class="form-group" v-for="(label, key) in searchOptions" :key="key">
        <label>{{ label }}</label>
        <div class="button-group">
          <button
              v-for="option in options[key]"
              :key="option.value"
              :class="[
                'button', // 統一デザイン
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

    <div v-if="movies.length > 0" class="movie-list">
      <ul>
        <li v-for="movie in movies" :key="movie.id">
          <h3>{{ movie.title }}</h3>
          <img :src="getMoviePoster(movie.posterPath)" alt="映画ポスター">
          <p>{{ movie.overview }}</p>
        </li>
      </ul>
      <button @click="resetSearch" class="search-button">検索画面に戻る</button>
    </div>

    <footer class="tmdb-credit">
      <img src="/images/tmdb-logo.png" alt="TMDb Logo" width="100"/>
      <p>このアプリは TMDb API を使用していますが、TMDb によって承認、認定、またはその他の承認は受けていません。</p>
      <p><NuxtLink to="/privacy">プライバシーポリシー</NuxtLink></p>
    </footer>
  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue';

const searchOptions = {
  genre: '今の気分を教えてください',
  provider: '配信サービスを選んでください',
  language: '洋画・邦画を選んでください',
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

const movies = ref([]);
const loading = ref(false);
const errorMessage = ref("");
const isSearchExhausted = ref(false);

const getProviderClass = (provider) => {
  switch (provider) {
    case '8': return 'netflix';
    case '9': return 'amazon';
    case '337': return 'disney';
    case '15': return 'hulu';
    default: return '';
  }
};

const getGenreClass = (genre) => {
  switch (genre) {
    case '35': return 'laugh';
    case '18': return 'cry';
    case '53': return 'thrill';
    case '10749': return 'romance';
    default: return '';
  }
};

const getLanguageClass = (language) => {
  switch (language) {
    case 'en': return 'western';
    case 'ja': return 'japanese';
    case 'ko': return 'korean';
    default: return '';
  }
};

const generateStorageKey = () => {
  return `movies_genre_${selectedOptions.value.genre}_provider_${selectedOptions.value.provider}_language_${selectedOptions.value.language}`;
};

// 検索時にローカルストレージをチェックし、なければAPIから取得
const fetchMovies = async () => {
  if (!selectedOptions.value.genre || !selectedOptions.value.provider || !selectedOptions.value.language) {
    errorMessage.value = "必須の質問に回答してください。";
    return;
  }

  loading.value = true;
  movies.value = [];
  errorMessage.value = "";
  isSearchExhausted.value = false;

  const storageKey = generateStorageKey();
  let storedMovies = JSON.parse(localStorage.getItem(storageKey) || '[]');

  if (storedMovies.length > 0) {
    // ローカルストレージから3件取得し、残りを保存
    movies.value = storedMovies.splice(0, 3);
    localStorage.setItem(storageKey, JSON.stringify(storedMovies));

    // すべて消費した場合のメッセージ処理
    if (storedMovies.length === 0) {
      isSearchExhausted.value = true;
      movies.value = [];
    }
  } else {
    try {
      // const response = await fetch(`${config.public.apiBase}/movies`,{
      const response = await fetch(`http://localhost:8080/api/movies`,{
      // const response = await fetch(`https://movie-recommendation-uybc.onrender.com/api/movies`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(selectedOptions.value),
      });

      if (!response.ok) {
        throw new Error("映画データの取得に失敗しました");
      }

      const data = await response.json();

      if (data.length === 0) {
        errorMessage.value = "検索結果がありませんでした。";
      } else {
        // 取得した30件から3件表示、残りを保存
        movies.value = data.slice(0, 3);
        localStorage.setItem(storageKey, JSON.stringify(data.slice(3)));
      }

    } catch (error) {
      errorMessage.value = "映画データの取得に失敗しました。しばらくしてから再試行してください。"
    }
  }
  loading.value = false;
};

// 初回マウント時にローカルストレージのデータを適用
onMounted(() => {
  const storageKey = generateStorageKey();
  const storedMovies = JSON.parse(localStorage.getItem(storageKey) || '[]');
  if (storedMovies.length > 0) {
    movies.value = storedMovies.splice(0, 3);
    localStorage.setItem(storageKey, JSON.stringify(storedMovies));
  }
});

const resetSearch = () => {
  movies.value = [];
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