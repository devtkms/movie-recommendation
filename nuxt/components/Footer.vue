<template>
  <footer class="tmdb-credit">
    <img src="/images/tmdb-logo.png" alt="TMDb Logo" width="100"/>
    <p>This application uses TMDB and the TMDB APIs but is not endorsed, certified, or otherwise approved by TMDB.</p>
    <p>このアプリは TMDb API を使用していますが、TMDb によって承認、認定、またはその他の承認は受けていません。</p>
    <p>
      <NuxtLink to="/privacy">プライバシーポリシー</NuxtLink>
    </p>
  </footer>
</template>

<style scoped>
.tmdb-credit {
  text-align: center;
  font-size: 11px;
  padding: 15px 0;
  margin-top: 25px;
  line-height: 1.6;
  border-top: 1px solid #ccc; /* 上に1pxの線を追加 */
  padding-top: 20px;
  background-color: #f8f9fa; /* 超薄いグレー */
}
</style>