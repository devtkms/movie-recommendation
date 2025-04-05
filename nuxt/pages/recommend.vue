<template>
  <div class="container">
    <Header />

    <!-- currentMovieがnullでない場合に表示 -->
    <div v-if="movies.length" class="movie-results">
      <div
          class="movie-card"
          @touchstart="onTouchStart"
          @touchmove="onTouchMove"
          @touchend="onTouchEnd"
          :style="cardStyle"
      >
        <h3 class="movie-title">{{ currentMovie.title }}</h3>
        <div class="poster-wrapper">
          <ArrowLeftCircleIcon class="icon-left" @click="prevMovie" />
          <img :src="getMoviePoster(currentMovie.posterPath)" alt="映画ポスター" class="movie-poster fixed-size" />
          <ArrowRightCircleIcon class="icon-right" @click="nextMovie" />
        </div>
        <div class="overview-container">
          <button
              class="overview-button"
              :disabled="!currentMovie.overview"
              :class="{ disabled: !currentMovie.overview }"
              @click="showOverview(currentMovie.overview)"
          >
            {{ currentMovie.overview ? '概要を見る' : '概要なし' }}
          </button>
          <button class="overview-button action" @click="showProviders">
            配信サービス
          </button>
        </div>
      </div>
    </div>

    <OverviewModal :show="showModal" :content="modalContent" @close="closeModal" />
    <WatchProvidersModal
        :show="showProviderModal"
        :providers="providerList"
        @close="showProviderModal = false"
    />

    <!-- ✅ タブとフッター -->
    <div class="bottom-bar">
      <TabBar :current="'recommend'" />
      <Footer />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import Header from '~/components/Header.vue';
import Footer from '~/components/Footer.vue';
import OverviewModal from '~/components/OverviewModal.vue';
import WatchProvidersModal from '~/components/WatchProvidersModal.vue';
import TabBar from '~/components/TabBar.vue';
import { ArrowLeftCircleIcon, ArrowRightCircleIcon } from '@heroicons/vue/24/solid';

// 映画リストの管理
const movies = ref([]);  // 映画のリスト
const currentIndex = ref(0);  // 現在表示している映画のインデックス
const showModal = ref(false);
const modalContent = ref('');
const providerList = ref([]);
const showProviderModal = ref(false);

const showOverview = (overview) => {
  modalContent.value = overview;
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

const showProviders = async () => {
  providerList.value = [{ name: 'Netflix' }, { name: 'Amazon Prime' }];
  showProviderModal.value = true;
};

// スワイプ用の状態
const touchStartX = ref(0);
const touchCurrentX = ref(0);
const isSwiping = ref(false);

const cardStyle = computed(() => {
  const dx = touchCurrentX.value - touchStartX.value;
  return isSwiping.value ? `transform: translateX(${dx}px) rotate(${dx / 20}deg); transition: none;` : '';
});

// スワイプ操作の開始
const onTouchStart = (e) => {
  touchStartX.value = e.touches[0].clientX;
  touchCurrentX.value = touchStartX.value;
  isSwiping.value = true;
};

// スワイプ操作の中
const onTouchMove = (e) => {
  touchCurrentX.value = e.touches[0].clientX;
};

// スワイプ操作の終了
const onTouchEnd = () => {
  isSwiping.value = false;
  const dx = touchCurrentX.value - touchStartX.value;
  if (dx > 100) {
    prevMovie();  // 右にスワイプしたら前の映画
  } else if (dx < -100) {
    nextMovie();  // 左にスワイプしたら次の映画
  }
};

// 映画のポスターパスの取得
const getMoviePoster = (path) =>
    path ? `https://image.tmdb.org/t/p/w500${path}` : 'https://via.placeholder.com/500';

// 映画を表示するためのAPIリクエスト
const fetchRecommendedMovies = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/recommendations/personalize');
    const data = await response.json();

    // レスポンスのデータをローカルストレージに保存
    if (data && data.combined && data.combined.length > 0) {
      movies.value = data.combined;  // 映画リストを更新
      localStorage.setItem('movies', JSON.stringify(data.combined));  // ローカルストレージに保存
      currentIndex.value = 0;  // 最初の映画を表示
    } else {
      console.error('映画データが見つかりません');
    }

  } catch (error) {
    console.error('Error fetching recommended movies:', error);
  }
};

// 映画を前に進める
const nextMovie = () => {
  if (currentIndex.value < movies.value.length - 1) {
    currentIndex.value++;
  } else {
    currentIndex.value = 0;  // 最後まで行ったら最初に戻る
  }
};

// 映画を後ろに戻す
const prevMovie = () => {
  if (currentIndex.value > 0) {
    currentIndex.value--;
  } else {
    currentIndex.value = movies.value.length - 1;  // 最初に戻ったら最後に戻る
  }
};

// currentMovieの計算
const currentMovie = computed(() => movies.value[currentIndex.value]);

// レコメンドタブを押したときに映画を取得
onMounted(() => {
  const storedMovies = localStorage.getItem('movies');

  if (storedMovies) {
    // ローカルストレージに映画データがあれば、それを使用
    movies.value = JSON.parse(storedMovies);
    currentIndex.value = 0;  // 最初の映画を表示
  } else {
    // ローカルストレージにデータがない場合、APIから取得
    fetchRecommendedMovies();
  }
});

</script>

<style scoped>
/* 共通スタイル */
.container {
  max-width: 600px;
  margin: auto;
  text-align: center;
  padding-top: 40px;
  padding-bottom: 40px;
  position: relative;
}

.movie-results {
  padding-bottom: 0;
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

.poster-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.icon-left,
.icon-right {
  width: 32px;
  height: 32px;
  color: rgba(100, 100, 100, 0.4);
  cursor: pointer;
}

.overview-container {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 40px;
  gap: 12px;
}

.overview-button {
  background-color: #2196F3;
  color: white;
  font-size: 14px;
  padding: 8px 16px;
  border-radius: 5px;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s ease-in-out;
  white-space: nowrap;
  min-width: 120px;
}

.overview-button:hover {
  background-color: #1976D2;
}

.overview-button.action {
  background-color: #28a745;
}

.overview-button.disabled {
  background-color: #ccc !important;
  cursor: not-allowed;
  color: #666;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #fff;
  border-top: 1px solid #ccc;
  z-index: 100;
}
</style>