<template>
  <div class="modal" v-if="show" @click.self="$emit('close')">
    <div class="modal-card" @click.stop>
      <h3 class="title">{{ overview.title }}</h3>

      <div class="overview-section">
        <div class="info" v-if="overview.releaseDate">
          <h4>📅 公開日</h4>
          <p>{{ overview.releaseDate }}</p>
        </div>
        <div class="info" v-if="overview.runtime">
          <h4>⏱ 上映時間</h4>
          <p>{{ overview.runtime }}分</p>
        </div>
        <div class="info" v-if="overview.productionCountries?.length">
          <h4>🌍 制作国</h4>
          <p>{{ overview.productionCountries.join(', ') }}</p>
        </div>
        <div class="info" v-if="overview.overview">
          <h4>📝 あらすじ</h4>
          <p>{{ overview.overview }}</p>
        </div>
      </div>

      <div class="providers">
        <h4>配信中のサービス</h4>
        <ul class="provider-list" v-if="providers.length">
          <li v-for="provider in providers" :key="provider.providerName">
            <img :src="getLogoUrl(provider.logoPath)" :alt="provider.providerName" class="logo" />
            <span>{{ provider.providerName }}</span>
          </li>
        </ul>
        <p v-else>現在、配信中のサービスは見つかりませんでした。</p>
        <p class="justwatch-attribution">
          ※ Streaming information provided by <strong>JustWatch</strong>.<br />
          ※ 配信情報は <strong>JustWatch</strong> より提供されています。
        </p>
      </div>

      <button class="close-button" @click="$emit('close')">閉じる</button>
    </div>
  </div>
</template>

<script setup>
defineProps({
  show: Boolean,
  overview: Object,
  providers: Array
})

const getLogoUrl = (path) => `https://image.tmdb.org/t/p/w92${path}`;
</script>

<style scoped>
.modal {
  position: fixed;
  inset: 0;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.modal-card {
  background: white;
  padding: 20px;
  border-radius: 12px;
  max-width: 420px;
  width: 90%;
  max-height: 90%;
  overflow-y: auto;
  box-sizing: border-box;
}

.title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
}

.overview-section {
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 20px;
  white-space: pre-wrap;
}

.info {
  margin-bottom: 14px;
}

.info h4 {
  margin-bottom: 4px;
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.providers {
  margin-top: 20px;
  text-align: left;
}

.providers h4 {
  margin-bottom: 10px;
}

.provider-list {
  list-style: none;
  padding: 0;
  margin: 0 0 10px;
}

.provider-list li {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.logo {
  width: 40px;
  height: auto;
  border-radius: 4px;
  object-fit: contain;
}

.close-button {
  background-color: #333;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  margin-top: 16px;
  width: 100%;
}

.justwatch-attribution {
  font-size: 11px;
  color: #999;
  line-height: 1.4;
  margin-top: 8px;
}
</style>