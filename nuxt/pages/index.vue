<template>
  <div class="container">
    <Header />
    <IntroModal v-if="showIntroModal" @close="closeIntroModal" />

    <div v-if="!currentMovie">
      <div class="form-group" v-for="(label, key) in searchOptions" :key="key">
        <label>{{ label }}</label>
        <div class="button-group">
          <button
              v-for="option in options[key]"
              :key="option.value"
              :class="[
              'button',
              key === 'mood' ? getMoodClass(option.value) : '',
              key === 'tone' ? getToneClass(option.value) : '',
              key === 'after' ? getAfterClass(option.value) : '',
              { selected: selectedOptions[key] === option.value }
            ]"
              @click="selectedOptions[key] = option.value"
          >
            {{ option.label }}
          </button>
        </div>
      </div>

      <p class="error-message">
        <span v-if="errorMessage">{{ errorMessage }}</span>
      </p>
      <p v-if="isSearchExhausted" class="exhausted-message">この条件での検索結果はすべて表示されました。</p>

      <button @click="fetchMovies" :disabled="loading" class="search-button">映画を探す</button>
    </div>

    <div v-if="loading">ロード中...</div>

    <div v-if="currentMovie" class="movie-results">
      <div class="selected-options">
        <div class="selected-option" :class="getMoodClass(selectedOptions.mood)">
          {{ getMoodLabel(selectedOptions.mood) }}
        </div>
        <div class="selected-option" :class="getToneClass(selectedOptions.tone)">
          {{ getToneLabel(selectedOptions.tone) }}
        </div>
        <div class="selected-option" :class="getAfterClass(selectedOptions.after)">
          {{ getAfterLabel(selectedOptions.after) }}
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
            {{ currentMovie.overview ? '概要' : '概要' }}
          </button>
          <button class="overview-button action" @click="showProviders">
            配信
          </button>
        </div>
      </div>

    </div>

    <!-- モーダルなど -->
    <OverviewModal :show="showModal" :content="modalContent" @close="closeModal" />
    <WatchProvidersModal
        :show="showProviderModal"
        :providers="providerList"
        @close="showProviderModal = false"
    />

    <div
        v-if="showLoginRequiredModal"
        class="modal-overlay"
        @click.self="showLoginRequiredModal = false"
    >
      <div class="login-alert-card" @click.stop>
        <h3>ようこそ MoviReco へ 👋</h3>
        <p>この機能を使うには<br><strong>新規登録</strong>または<strong>ログイン</strong>が必要です。</p>
        <button class="login-alert-button" @click="redirectToLogin">登録 / ログインする</button>
      </div>
    </div>

    <!-- ✅ タブとフッター -->
    <div class="bottom-bar">
      <TabBar :current="'main'" @require-login="showLoginRequiredModal = true" />
      <Footer />
    </div>
  </div>
</template>

  <script setup>
  import { ref, onMounted, computed } from 'vue';
  import Header from '~/components/Header.vue';
  import Footer from '~/components/Footer.vue';
  import OverviewModal from '~/components/OverviewModal.vue';
  import WatchProvidersModal from '~/components/WatchProvidersModal.vue';
  import TabBar from '~/components/TabBar.vue';
  import { useRouter } from 'vue-router'
  import { ArrowLeftCircleIcon, ArrowRightCircleIcon } from '@heroicons/vue/24/solid';

  /* ------------------------------
    初期状態
  ------------------------------ */
  const selectedOptions = ref({ mood: '', tone: '', after: '' });
  const currentMovie = ref(null);
  const moviePool = ref([]);
  const currentIndex = ref(0);
  const loading = ref(false);
  const errorMessage = ref("");
  const isSearchExhausted = ref(false);

  const showIntroModal = ref(false);
  const showModal = ref(false);
  const modalContent = ref("");
  const providerList = ref([]);
  const showProviderModal = ref(false);

  const router = useRouter()
  const showLoginRequiredModal = ref(false)

  const config = useRuntimeConfig()
  const apiBase = config.public.apiBase

  const redirectToLogin = () => {
    showLoginRequiredModal.value = false
    router.push('/login')
  }

  /* ------------------------------
    ライフサイクル
  ------------------------------ */
  onMounted(() => {
    const hasVisited = localStorage.getItem('visited');
    if (!hasVisited) {
      showIntroModal.value = true;
      localStorage.setItem('visited', 'true');
    }
  });

  /* ------------------------------
    モーダル操作
  ------------------------------ */
  const closeIntroModal = () => {
    showIntroModal.value = false;
  };

  const showOverview = (overview) => {
    modalContent.value = overview;
    showModal.value = true;
  };

  const closeModal = () => {
    showModal.value = false;
  };

  const showProviders = async () => {
    if (!currentMovie.value?.id) return;

    try {
      const res = await fetch(`${apiBase}/movie/${currentMovie.value.id}/watch/providers`);
      if (!res.ok) throw new Error("配信サービス取得に失敗");

      const providers = await res.json();
      providerList.value = Array.isArray(providers) ? providers : [];
    } catch (e) {
      console.error("❌ 配信サービス取得失敗", e);
      providerList.value = [];
    } finally {
      showProviderModal.value = true;
    }
  };

  /* ------------------------------
    スワイプ操作
  ------------------------------ */
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
    const dx = touchCurrentX.value - touchStartX.value;
    if (Math.abs(dx) > 80) {
      dx > 0 ? prevMovie() : nextMovie();
    }
    isSwiping.value = false;
    touchStartX.value = 0;
    touchCurrentX.value = 0;
  };

  /* ------------------------------
    質問定義
  ------------------------------ */
  const searchOptions = {
    mood: '今の気分を教えてください',
    tone: '映画の雰囲気はどんな感じがいいですか',
    after: '観終わった後、どんな気持ちになりたいですか？'
  };

  const options = {
    mood: [
      { value: 'light', label: '気軽に楽しみたい' },
      { value: 'emotional', label: '感情を動かされたい' },
      { value: 'escape', label: '非日常を味わいたい' },
      { value: 'thrill', label: 'スリルを感じたい' }
    ],
    tone: [
      { value: 'slow', label: 'ゆったり観たい' },
      { value: 'fast', label: 'テンポよく進んでほしい' },
      { value: 'deep', label: 'どっぷり浸りたい' },
      { value: 'casual', label: '軽めに流したい' }
    ],
    after: [
      { value: 'refresh', label: 'スカッとしたい' },
      { value: 'warm', label: '心が温まりたい' },
      { value: 'cry', label: '泣いてスッキリしたい' },
      { value: 'think', label: 'ちょっと考えたい' }
    ]
  };

  /* ------------------------------
    クラス & ラベル取得関数
  ------------------------------ */
  const getMoviePoster = (path) =>
      path ? `https://image.tmdb.org/t/p/w500${path}` : 'https://via.placeholder.com/500';

  const getMoodLabel = (mood) => options.mood.find(opt => opt.value === mood)?.label || "未選択";
  const getToneLabel = (tone) => options.tone.find(opt => opt.value === tone)?.label || "未選択";
  const getAfterLabel = (after) => options.after.find(opt => opt.value === after)?.label || "未選択";

  const getMoodClass = (mood) => ({
    'light': 'light',
    'emotional': 'emotional',
    'escape': 'escape',
    'thrill': 'thrill'
  }[mood] || '');

  const getToneClass = (tone) => ({
    'slow': 'slow',
    'fast': 'fast',
    'deep': 'deep',
    'casual': 'casual'
  }[tone] || '');

  const getAfterClass = (after) => ({
    'refresh': 'refresh',
    'warm': 'warm',
    'cry': 'cry',
    'think': 'think'
  }[after] || '');

  /* ------------------------------
    映画取得処理
  ------------------------------ */
  const generateStorageKey = () =>
      `movies_mood_${selectedOptions.value.mood}_tone_${selectedOptions.value.tone}_after_${selectedOptions.value.after}`;

  const fetchMovies = async () => {
    if (!selectedOptions.value.mood || !selectedOptions.value.tone || !selectedOptions.value.after) {
      errorMessage.value = "必須の質問に回答してください。";
      return;
    }

    loading.value = true;
    errorMessage.value = "";
    isSearchExhausted.value = false;
    currentMovie.value = null;

    const storageKey = generateStorageKey();
    const today = new Date().toISOString().slice(0, 10);
    const stored = JSON.parse(localStorage.getItem(storageKey) || '{}');

    if (stored.pool && stored.savedDate === today) {
      moviePool.value = stored.pool;
      currentIndex.value = stored.index || 0;
      currentMovie.value = moviePool.value[currentIndex.value];
      loading.value = false;
      return;
    }

    try {
      const response = await fetch(`${apiBase}/api/recommendations`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ...selectedOptions.value,
          isMyData: localStorage.getItem('isDevUser') === 'true'
        }),
        credentials: 'include'
      });

      if (!response.ok) throw new Error("API リクエストが失敗しました");

      const data = await response.json();
      const combined = [...(data.combined || [])];

      for (let i = combined.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [combined[i], combined[j]] = [combined[j], combined[i]];
      }

      moviePool.value = combined;
      currentIndex.value = 0;
      currentMovie.value = moviePool.value[0];

      localStorage.setItem(storageKey, JSON.stringify({ pool: combined, index: 0, savedDate: today }));
    } catch (error) {
      console.error("❌ 映画データの取得に失敗:", error);
      errorMessage.value = "映画データの取得に失敗しました。しばらくしてから再試行してください。";
    }

    loading.value = false;
  };

  /* ------------------------------
    ナビゲーション
  ------------------------------ */
  const nextMovie = () => {
    if (currentIndex.value < moviePool.value.length - 1) {
      currentIndex.value++;
      currentMovie.value = moviePool.value[currentIndex.value];
    } else {
      isSearchExhausted.value = true;
    }
  };

  const prevMovie = () => {
    if (currentIndex.value > 0) {
      currentIndex.value--;
      currentMovie.value = moviePool.value[currentIndex.value];
      isSearchExhausted.value = false;
    }
  };

  </script>

  <style scoped>
  .container {
    max-width: 600px;
    margin: auto;
    text-align: center;
    padding-bottom: 100px;
    padding-top: 40px; /* ← 上部余白を追加 */
    position: relative;
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
    min-width: 170px;   /* ← 統一したい幅に調整 */
    max-width: 170px;   /* ← 同じにして幅を固定 */
    text-align: center;
    white-space: nowrap; /* ← テキスト折り返し防止 */
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

  .movie-poster {
    max-width: 80%;
    height: auto;
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
    min-height: 20px; /* 高さを固定 */
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
    margin-top: 20px;
  }

  .search-button:hover {
    background-color: #555;
  }

  .search-button:disabled {
    background-color: #999;
    cursor: not-allowed;
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

  .overview-button.info {
    background-color: #2196F3;
  }
  .overview-button.action {
    background-color: #28a745;
  }

  .overview-button:hover {
    background-color: #1976D2;
  }

  .overview-button.disabled {
    background-color: #ccc !important;
    cursor: not-allowed;
    color: #666;
  }

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
    font-size: 12px;
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

  /* 🎨 mood（気分） */
  .light    { background-color: #FFD700; }  /* 明るくポップな黄色 */
  .emotional{ background-color: #FF69B4; }  /* 感情 → ピンク系 */
  .escape   { background-color: #6A5ACD; }  /* 非日常 → ミステリアスな紫 */
  .thrill   { background-color: #FF4500; }  /* スリル → 鮮やかな赤橙 */

  /* 🎬 tone（雰囲気） */
  .slow     { background-color: #87CEFA; }  /* ゆったり → 空色 */
  .fast     { background-color: #00CED1; }  /* テンポよく → 爽やかな青緑 */
  .deep     { background-color: #191970; }  /* どっぷり浸かる → 深い藍色 */
  .casual   { background-color: #90EE90; }  /* 軽く観たい → 柔らかい緑 */

  /* 🎭 after（気持ち） */
  .refresh  { background-color: #32CD32; }  /* スカッと → 元気な緑 */
  .warm     { background-color: #FFB347; }  /* 温かい気持ち → オレンジ系 */
  .cry      { background-color: #1E90FF; }  /* 泣く → さわやかな青 */
  .think    { background-color: #9DC183; }  /* 考える → グレー（落ち着き） */

  .icon-left,
  .icon-right {
    width: 32px;
    height: 32px;
    color: rgba(100, 100, 100, 0.4);
  }

  .poster-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
  }

  /* ✅ チェックボックス表示用追加 */
  .checkbox-wrapper {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 10px;
    margin-top: 12px;
    font-size: 14px;
  }

  .checkbox-wrapper.providers,
  .checkbox-wrapper.languages {
    display: grid;
    grid-template-columns: 140px 140px;
    column-gap: 20px;
    row-gap: 8px;
    justify-content: center;
    margin-top: 24px;
    padding-top: 4px;
    max-width: 300px;
    margin-left: auto;
    margin-right: auto;
  }

  /* ✅ 各チェックボックス：左揃え */
  .checkbox-label {
    font-size: 14px;
    display: flex;
    align-items: center;
    gap: 6px;
    justify-content: flex-start;
    white-space: nowrap;
  }

  .filter-toggle {
    text-align: left;          /* ← 左寄せに変更 */
    font-weight: bold;
    font-size: 16px;
    margin: 24px auto 10px;
    padding-left: 20px;        /* ← 左に少し余白 */
    cursor: pointer;
    color: #333;
    user-select: none;
    max-width: 300px;          /* ← 中央寄せの最大幅に合わせる */
  }

  .filter-toggle:hover {
    opacity: 0.8;
  }

  .bottom-bar {
    bottom: 0;
    left: 0;
    right: 0;
    background-color: #fff;
    border-top: 1px solid #ccc;
    z-index: 100;
    margin-top: 40px; /* ← この行を追加 */
  }

  .login-alert-card {
    position: fixed;
    bottom: 100px;
    left: 50%;
    transform: translateX(-50%);
    background-color: #fff;
    color: #333;
    padding: 20px 24px;
    border-radius: 12px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
    font-size: 15px;
    text-align: center;
    animation: fadeInUp 0.3s ease-out;
    z-index: 9999;
    width: 90%;
    max-width: 300px;
  }

  .login-alert-card h3 {
    font-size: 18px;
    margin-bottom: 8px;
    font-weight: bold;
  }

  .login-alert-card p {
    font-size: 14px;
    margin-bottom: 16px;
    line-height: 1.6;
  }

  .login-alert-button {
    background-color: #3b82f6;
    color: white;
    font-weight: bold;
    padding: 10px 16px;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-size: 14px;
    transition: background-color 0.2s ease-in-out;
  }

  .login-alert-button:hover {
    background-color: #2563eb;
  }

  @keyframes fadeInUp {
    from {
      opacity: 0;
      transform: translateX(-50%) translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateX(-50%) translateY(0);
    }
  }

  .modal-overlay {
    position: fixed;
    inset: 0;
    background-color: rgba(0, 0, 0, 0.4); /* 半透明の背景 */
    display: flex;
    justify-content: center;
    align-items: flex-end;
    z-index: 9998;
  }

  </style>