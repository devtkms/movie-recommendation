<template>
  <div v-if="show" class="modal" @click.self="$emit('close')">
    <div class="modal-content">
      <div class="overview-block" v-if="parsed.releaseDate">
        <h3>📅 公開日</h3>
        <p>{{ parsed.releaseDate }}</p>
      </div>
      <div class="overview-block" v-if="parsed.runtime">
        <h3>⏱ 上映時間</h3>
        <p>{{ parsed.runtime }} 分</p>
      </div>
      <div class="overview-block" v-if="parsed.productionCountries?.length">
        <h3>🌍 制作国</h3>
        <p>{{ parsed.productionCountries.join(', ') }}</p>
      </div>
      <div class="overview-block" v-if="parsed.overview">
        <h3>📝 あらすじ</h3>
        <p>{{ parsed.overview }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  show: Boolean,
  content: String,
});

const parsed = computed(() => {
  const lines = props.content?.split('\n') ?? [];
  const obj = {};
  let currentKey = null;

  lines.forEach(line => {
    if (line.startsWith('📝')) {
      currentKey = 'overview';
      obj.overview = line.replace('📝 あらすじ', '').trim();
    } else if (line.startsWith('📅')) {
      currentKey = null;
      obj.releaseDate = line.replace('📅 公開日:', '').trim();
    } else if (line.startsWith('⏱')) {
      currentKey = null;
      obj.runtime = line.replace('⏱ 上映時間:', '').replace('分', '').trim();
    } else if (line.startsWith('🌍')) {
      currentKey = null;
      obj.productionCountries = line.replace('🌍 制作国:', '').trim().split(', ');
    } else if (currentKey === 'overview') {
      // 複数行あらすじを連結
      obj.overview += '\n' + line.trim();
    }
  });

  return obj;
});

defineEmits(['close']);
</script>

<style scoped>
.modal {
  position: fixed;
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
  width: 90%;
  text-align: left;
  font-size: 14px;
  line-height: 1.7;
}

.overview-block {
  margin-bottom: 18px;
}

.overview-block h3 {
  font-size: 15px;
  margin-bottom: 6px;
  color: #333;
}
</style>