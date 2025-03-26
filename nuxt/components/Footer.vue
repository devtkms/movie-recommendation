<template>
  <footer class="tmdb-credit">
    <img src="/images/tmdb-logo.png" alt="TMDb Logo" width="100"/>
    <p>This application uses TMDB and the TMDB APIs but is not endorsed, certified, or otherwise approved by TMDB.</p>
    <p>このアプリは TMDb API を使用していますが、TMDb によって承認、認定、またはその他の承認は受けていません。</p>

    <!-- 🔗 SNSリンクをここに追加 -->
    <p class="developer-credit">
      Created by
      <a href="https://twitter.com/yourusername" target="_blank" rel="noopener noreferrer" class="social-link">
        @MoviReco
      </a>
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
  border-top: 1px solid #ccc;
  padding-top: 20px;
  background-color: #f8f9fa;
}

.developer-credit {
  margin-top: 15px;
  font-size: 12px;
}

.social-link {
  color: #1DA1F2;
  font-weight: bold;
  text-decoration: none;
  margin-left: 4px;
}

.social-link:hover {
  text-decoration: underline;
}
</style>