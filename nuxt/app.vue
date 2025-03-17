<script setup>
import { useHead } from 'nuxt/app'

useHead({
  script: [
    {
      src: 'https://www.googletagmanager.com/gtag/js?id=G-K98QZEB845',
      async: true
    },
    {
      innerHTML: `
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());
        gtag('config', 'G-K98QZEB845');
      `,
      type: 'text/javascript'
    }
  ]
})
</script>

<template>
  <div>
    <NuxtRouteAnnouncer />
    <NuxtPage />
  </div>
</template>