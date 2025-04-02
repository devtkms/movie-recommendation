<template>
  <div v-if="show" class="modal">
    <div class="modal-content">
      <h2>配信中のサービス</h2>
      <div v-if="providers.length > 0">
        <ul class="provider-list">
          <li v-for="(provider, index) in providers" :key="index">
            <img :src="getLogoUrl(provider.logoPath)" :alt="provider.providerName" class="logo" />
            <span>{{ provider.providerName }}</span>
          </li>
        </ul>
      </div>
      <div v-else>
        <p>現在、日本で配信中のサービスは見つかりませんでした。</p>
      </div>
      <!-- ✅ JustWatch attribution -->
      <p class="justwatch-attribution">
        ※ Streaming information provided by <strong>JustWatch</strong>.<br>
        ※ 配信情報は <strong>JustWatch</strong> より提供されています。
      </p>
      <button @click="$emit('close')" class="close-button">閉じる</button>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  show: Boolean,
  providers: {
    type: Array,
    required: true
  }
});

const getLogoUrl = (path) => `https://image.tmdb.org/t/p/w92${path}`;
</script>

<style>
.modal {
  position: fixed;
  z-index: 9999; /* ←追加 */
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 12px;
  max-width: 400px;
  text-align: center;
}

.provider-list {
  list-style: none;
  padding: 0;
  margin: 20px 0;
}

.provider-list li {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 10px;
  justify-content: flex-start; /* ← center から変更 */
}

.logo {
  width: 40px;
  height: auto;
  border-radius: 5px;
}

.close-button {
  background-color: #333;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
}

.justwatch-attribution {
  font-size: 11px;
  color: #999;
  margin-top: 8px;
  text-align: left;
  line-height: 1.4;
}
</style>