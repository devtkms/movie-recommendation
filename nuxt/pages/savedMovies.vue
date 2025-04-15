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
          <BookmarkSlashIcon class="bookmark-icon" @click="openConfirmModal(movie.movieId)" />
        </div>
      </div>

      <MovieDetailModal
          :show="showDetailModal"
          :overview="modalOverview"
          :providers="modalProviders"
          @close="showDetailModal = false"
      />

      <!-- 削除確認モーダル -->
      <div v-if="showConfirmModal" class="confirm-modal-backdrop" @click.self="showConfirmModal = false">
        <div class="confirm-modal">
          <p>気になるリストから解除しますか？</p>
          <div class="modal-buttons">
            <button @click="executeDelete">はい</button>
            <button @click="showConfirmModal = false">キャンセル</button>
          </div>
        </div>
      </div>

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

const showConfirmModal = ref(false);
const targetMovieId = ref(null);

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

const openConfirmModal = (movieId) => {
  targetMovieId.value = movieId;
  showConfirmModal.value = true;
};

const executeDelete = async () => {
  if (targetMovieId.value !== null) {
    await deleteMovie(targetMovieId.value);
    showConfirmModal.value = false;
    targetMovieId.value = null;
  }
};

// 削除処理本体
const deleteMovie = async (movieId) => {
  try {
    await fetch(`${apiBase}/api/movies/delete/${movieId}`, {
      method: 'DELETE',
      credentials: 'include',
    });

    // UI上の一覧から削除
    movies.value = movies.value.filter(movie => movie.movieId !== movieId);

    // ✅ 全ての localStorage キャッシュを対象に isSaved を false に更新
    const allKeys = Object.keys(localStorage);
    allKeys.forEach((key) => {
      if (key.startsWith('movies_mood_')) {
        const stored = JSON.parse(localStorage.getItem(key) || '{}');
        if (stored.pool) {
          const target = stored.pool.find(m => m.id === movieId);
          if (target) {
            target.isSaved = false;
            localStorage.setItem(key, JSON.stringify(stored));
          }
        }
      }
    });
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
  max-width: 100%; /* ← 拡張防止 */
  box-sizing: border-box;
  padding: 8px;
  border-radius: 10px;
  background-color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  height: 340px; /* ← 高さを固定 */
  justify-content: space-between; /* ← 上下に均等配置 */
}
.poster {
  width: 100%;
  height: 225px;
  object-fit: cover;
  border-radius: 6px;
}
.title {
  margin-top: 8px;
  font-size: 10px;
  font-weight: bold;
  color: #333;
  text-align: center;
  min-height: 28px; /* ← タイトル高さを確保 */

  white-space: normal;         /* ← nowrap を解除して折り返し許可 */
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
  display: -webkit-box;        /* ← 行数制限 */
  -webkit-line-clamp: 2;       /* ← 2行まで表示 */
  -webkit-box-orient: vertical;
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

.confirm-modal-backdrop {
  position: fixed;
  inset: 0;
  background-color: rgba(0,0,0,0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.confirm-modal {
  background: white;
  padding: 20px 24px;
  border-radius: 12px;
  text-align: center;
  width: 280px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.2);
}

.modal-buttons {
  margin-top: 16px;
  display: flex;
  justify-content: space-between;
  gap: 12px;
}

.modal-buttons button {
  flex: 1;
  padding: 8px 0;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
}

.modal-buttons button:first-child {
  background-color: #c62828;
  color: white;
}

.modal-buttons button:last-child {
  background-color: #eee;
  color: #333;
}


</style>

<style>
html, body {
  height: 100%;
  margin: 0;
  padding: 0;
}
</style>