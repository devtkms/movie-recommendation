<template>
  <div class="container">
    <Header />
    <h2 class="page-title">📌 気になる映画リスト</h2>

    <div v-if="loading" class="loading">読み込み中...</div>
    <div v-else-if="movies.length === 0" class="empty-message">
      まだ気になる映画はありません。
    </div>
    <div v-else class="movie-list">
      <div v-for="movie in movies" :key="movie.movieId" class="movie-card">
        <img :src="getMoviePoster(movie.posterPath)" alt="poster" class="poster" />
        <div class="title">{{ movie.title }}</div>
      </div>
    </div>

    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import Header from '~/components/Header.vue';
import Footer from '~/components/Footer.vue';
import { useRouter } from 'vue-router';

const movies = ref([]);
const loading = ref(true);
const config = useRuntimeConfig();
const apiBase = config.public.apiBase;
const router = useRouter();

onMounted(async () => {
  try {
    const res = await fetch(`${apiBase}/api/movies/saved`, {
      credentials: 'include'
    });

    if (res.status === 401) {
      router.push('/login');
      return;
    }

    const data = await res.json();
    movies.value = data || [];
  } catch (e) {
    console.error('❌ 保存映画取得エラー:', e);
  } finally {
    loading.value = false;
  }
});

const getMoviePoster = (path) =>
    path ? `https://image.tmdb.org/t/p/w500${path}` : 'https://via.placeholder.com/300x450';
</script>

<style scoped>
.container {
  max-width: 600px;
  margin: auto;
  padding: 100px 20px 60px;
  text-align: center;
}
.page-title {
  font-size: 20px;
  margin-bottom: 20px;
  font-weight: bold;
}
.loading,
.empty-message {
  margin-top: 40px;
  font-size: 16px;
  color: #666;
}
.movie-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}
.movie-card {
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  padding: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.poster {
  width: 100%;
  max-width: 140px;
  height: auto;
  border-radius: 6px;
}
.title {
  margin-top: 8px;
  font-size: 14px;
  font-weight: bold;
  color: #333;
  text-align: center;
}
</style>
