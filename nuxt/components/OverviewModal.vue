

<template>
  <div v-if="show" class="modal" @click.self="$emit('close')">
    <div class="modal-content">
      <p>{{ content }}</p>
    </div>
  </div>
</template>

<script setup>
defineProps({
  show: Boolean,
  content: String
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
  border-radius: 10px;
  text-align: center;
  max-width: 400px;
}

.modal-content p {
  margin-bottom: 10px;
}

</style>