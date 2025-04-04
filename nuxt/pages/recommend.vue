
<template>
  <div class="container">
    <Header />

    <div v-if="currentMovie" class="movie-results">

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
import { useRoute } from 'vue-router';
import Header from '~/components/Header.vue';
import Footer from '~/components/Footer.vue';
import OverviewModal from '~/components/OverviewModal.vue';
import WatchProvidersModal from '~/components/WatchProvidersModal.vue';
import TabBar from '~/components/TabBar.vue';
import { ArrowLeftCircleIcon, ArrowRightCircleIcon } from '@heroicons/vue/24/solid';

const currentTab = ref('recommend');
const route = useRoute();

const selectedOptions = ref({ mood: 'light', tone: 'fast', after: 'refresh' });
const currentMovie = ref({
  title: 'デモ映画タイトル',
  posterPath: '/dummy.jpg',
  overview: 'これはデモ映画の概要です。',
  id: 123,
});
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
  isSwiping.value = false;
};

const resetSearch = () => {
  // ダミー処理
  console.log('検索画面に戻る');
};

const getMoviePoster = (path) =>
    path ? `https://image.tmdb.org/t/p/w500${path}` : 'https://via.placeholder.com/500';

const getMoodLabel = (mood) => ({ light: '気軽に楽しみたい' }[mood] || '未選択');
const getToneLabel = (tone) => ({ fast: 'テンポよく進んでほしい' }[tone] || '未選択');
const getAfterLabel = (after) => ({ refresh: 'スカッとしたい' }[after] || '未選択');

const getMoodClass = (mood) => ({ light: 'light' }[mood] || '');
const getToneClass = (tone) => ({ fast: 'fast' }[tone] || '');
const getAfterClass = (after) => ({ refresh: 'refresh' }[after] || '');
</script>

<style scoped>
/* 共通スタイル */
.container {
  max-width: 600px;
  margin: auto;
  text-align: center;
  padding-top: 40px;      /* ✅ 上余白 */
  padding-bottom: 40px;   /* ✅ 下余白（フッターとの間隔） */
  position: relative;
}


.movie-results {
  padding-bottom: 0;      /* ✅ containerに任せる */
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

/* mood */
.light    { background-color: #FFD700; }
.emotional{ background-color: #FF69B4; }
.escape   { background-color: #6A5ACD; }
.thrill   { background-color: #FF4500; }

/* tone */
.slow     { background-color: #87CEFA; }
.fast     { background-color: #00CED1; }
.deep     { background-color: #191970; }
.casual   { background-color: #90EE90; }

/* after */
.refresh  { background-color: #32CD32; }
.warm     { background-color: #FFB347; }
.cry      { background-color: #1E90FF; }
.think    { background-color: #9DC183; }

/* 戻るボタン */
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

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #fff;
  border-top: 1px solid #ccc;
  z-index: 100;
  /* ✅ 不要なマージンは削除 */
}
</style>

