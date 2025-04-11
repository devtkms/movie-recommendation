<template>
  <div class="wrapper">
    <!-- 固定ヘッダー -->
    <div class="header-fixed">
      <Header />
      <TabBar
          :current="currentTab"
          @click-main="resetToSearch"
          @click-recommend="handleClickRecommendTab"
          @click-save="handleClickSaveTab"
          @require-login="handleRequireLogin"
      />
    </div>

    <!-- スクロール可能な映画リスト + 固定で表示されるフッター -->
    <div class="main-scroll-area">


      <div v-if="loading" class="loading">読み込み中...</div>
      <div v-else-if="movies.length === 0" class="empty-message">
        まだ気になる映画はありません。
      </div>
      <div v-else class="movie-list">
        <div v-for="movie in movies" :key="movie.movieId" class="movie-card">
          <img
              :src="getMoviePoster(movie.posterPath)"
              alt="poster"
              class="poster"
              @click="handleMovieClick(movie.movieId)"
          />
          <div class="title">{{ movie.title }}</div>
          <BookmarkSlashIcon class="bookmark-icon" @click="deleteMovie(movie.movieId)" />
        </div>
      </div>

      <MovieDetailModal
          :show="showDetailModal"
          :overview="modalOverview"
          :providers="modalProviders"
          @close="showDetailModal = false"
      />

        <Footer />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import Header from '~/components/Header.vue';
import Footer from '~/components/Footer.vue';
import { useRouter } from 'vue-router';
import TabBar from "~/components/TabBar.vue";
import { BookmarkSlashIcon } from '@heroicons/vue/24/outline'
import MovieDetailModal from '~/components/MovieDetailModal.vue'

const movies = ref([]);
const loading = ref(true);
const config = useRuntimeConfig();
const apiBase = config.public.apiBase;
const router = useRouter();

const showDetailModal = ref(false)
const modalOverview = ref({})
const modalProviders = ref([])

const currentTab = ref('save')  // 例として現在タブを "save" に

const resetToSearch = () => {
  router.push('/')
}

const handleClickRecommendTab = () => {
  router.push('/recommend')
}

const handleClickSaveTab = () => {
  router.push('/savedMovies')
}

const handleRequireLogin = (type) => {
  // loginModalType とかを使ってモーダル表示
}

onMounted(async () => {
  try {
    const res = await fetch(`${apiBase}/api/movies/saved`, {
      credentials: 'include'
    });

    if (!res.ok) {
      if (res.status === 403) {
        router.push('/login');
        return;
      }

      const text = await res.text();
      console.error('❌ APIエラー:', res.status, text);
      return;
    }

    const data = await res.json(); // ← ここはOKな時だけ呼ぶ
    movies.value = data || [];
  } catch (e) {
    console.error('❌ 保存映画取得エラー:', e);
  } finally {
    loading.value = false;
  }
});

const deleteMovie = async (movieId) => {
  try {
    await fetch(`${apiBase}/api/movies/delete/${movieId}`, {
      method: 'DELETE',
      credentials: 'include',
    });
    // ローカル状態からも削除
    movies.value = movies.value.filter(movie => movie.movieId !== movieId);
  } catch (e) {
    console.error('❌ 削除エラー:', e);
  }
};

const handleMovieClick = async (movieId) => {
  try {
    const res = await fetch(`${apiBase}/api/movies/${movieId}/detail`, {
      credentials: 'include'
    });
    if (!res.ok) throw new Error('API失敗');

    const data = await res.json();
    modalOverview.value = data.overview;
    modalProviders.value = data.providers;
    showDetailModal.value = true;
  } catch (e) {
    console.error("❌ 詳細取得エラー:", e);
  }
};

const getMoviePoster = (path) =>
    path ? `https://image.tmdb.org/t/p/w500${path}` : 'https://via.placeholder.com/300x450';
</script>

<style scoped>
.wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.header-fixed {
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 1000;
  background-color: #fff;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
}

.main-scroll-area {
  margin-top: 100px; /* ← 60px → 100px に変更してヘッダーの高さを避ける */
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
  box-sizing: border-box;
  padding: 20px;
  padding-bottom: 150px;
  padding-left: 8px;
  padding-right: 8px;
  overflow-x: hidden;
}

.movie-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);  /* ← 2列を等幅に */
  gap: 12px;
}

.loading,
.empty-message {
  margin-top: 40px;
  font-size: 16px;
  color: #666;
  text-align: center;
}


.movie-card {
  width: 100%;
  box-sizing: border-box;
  padding: 8px;
  border-radius: 10px;
  background-color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}
.poster {
  width: 100%;
  height: auto;
  aspect-ratio: 2 / 3;
  object-fit: cover;
  border-radius: 6px;
}
.title {
  margin-top: 8px;
  font-size: 10px;
  font-weight: bold;
  color: #333;
  text-align: center;

  white-space: nowrap;      /* 改行させない */
  overflow: hidden;         /* はみ出し部分を非表示 */
  text-overflow: ellipsis;  /* 「…」を表示 */
  max-width: 100%;          /* 幅制限（親にフィット） */
}

.bookmark-icon {
  width: 28px;
  height: 28px;
  margin-top: 8px;
  color: #666;
  cursor: pointer;
  padding: 6px;
  border-radius: 50%;
  border: 1px solid transparent;
  transition: background-color 0.2s ease, border-color 0.2s ease;
}

.bookmark-icon:hover {
  background-color: #f8d7da;
  color: #c62828;
  border-color: #f5c6cb;
}
</style>

<style>
html, body {
  height: 100%;
  margin: 0;
  padding: 0;
}
</style>