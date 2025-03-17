<template>
  <div class="container">
    <h1 class="title">パッとシネマ</h1>

    <div v-if="movies.length === 0">
      <div class="form-group" v-for="(label, key) in searchOptions" :key="key">
        <label>{{ label }}</label>
        <div class="button-group" :class="{ 'two-column': ['genre', 'provider', 'language'].includes(key) }">
          <button
              v-for="option in options[key]"
              :key="option.value"
              :class="{ selected: selectedOptions[key] === option.value }"
              @click="selectedOptions[key] = option.value">
            {{ option.label }}
          </button>
        </div>
      </div>

      <!-- メッセージエリア -->
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      <p v-if="isSearchExhausted" class="exhausted-message">この条件での検索結果はすべて表示されました。</p>

      <button @click="fetchMovies" :disabled="loading">映画を探す</button>
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
      <button @click="resetSearch">検索画面に戻る</button>
    </div>

    <!-- TMDbクレジット表記 -->
    <footer class="tmdb-credit">
      <img src="/images/tmdb-logo.png" alt="TMDb Logo" width="100"/>
      <p>
        このアプリは TMDb API を使用していますが、TMDb によって承認、認定、またはその他の承認は受けていません。
      </p>
      <p>
        <NuxtLink to="/privacy">プライバシーポリシー</NuxtLink>
      </p>
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
      // const response = await fetch(`http://localhost:8080/api/movies`,{
      const response = await fetch(`https://pattocinema-api.onrender.com/api/movies`, {
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
  font-size: 24px;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

.button-group {
  display: grid;
  grid-template-columns: repeat(2, auto);
  gap: 10px;
  justify-content: center;
}

button {
  padding: 8px 12px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
  margin-top: 5px;
  border-radius: 5px;
  min-width: 140px;
  text-align: center;
}

button.selected {
  background-color: #0056b3;
  font-weight: bold;
}

button:disabled {
  background-color: #ccc;
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
  font-size: 11px; /* 少し大きくして可読性向上 */
  padding: 15px 0; /* 余白を少し増やす */
  margin-top: 25px;
  line-height: 1.6; /* 行間を広げて読みやすく */
}

.tmdb-logo {
  width: 160px; /* 少し大きく */
  display: block;
  margin: 15px auto; /* 上下の余白を調整 */
  filter: brightness(1.4); /* ロゴを少し明るくして視認性UP */
}


</style>