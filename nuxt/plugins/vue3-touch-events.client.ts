import { defineNuxtPlugin } from '#app'
import Vue3TouchEvents from 'vue3-touch-events'
import type { Plugin } from 'vue'

export default defineNuxtPlugin(nuxtApp => {
    nuxtApp.vueApp.use(Vue3TouchEvents as Plugin)
})