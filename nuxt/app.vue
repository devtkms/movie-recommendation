<script setup>
import { useHead } from 'nuxt/app'

useHead({
  script: [
    {
      src: 'https://www.googletagmanager.com/gtag/js?id=G-T54VW72S0C',
      async: true
    },
    {
      innerHTML: `
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());
        gtag('config', 'G-T54VW72S0C');
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