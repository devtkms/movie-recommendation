<template>
  <footer class="tmdb-credit">
    <img src="/images/tmdb-logo.png" alt="TMDb Logo" width="100" />
    <p>This application uses TMDB and the TMDB APIs but is not endorsed, certified, or otherwise approved by TMDB.</p>

    <p class="developer-credit">
      Created by
      <a href="https://twitter.com/MoviReco" target="_blank" rel="noopener noreferrer" class="social-link">
        <img src="/images/logo-black.png" alt="X Logo" width="16" height="16" style="vertical-align: middle; margin-left: 4px;" />
      </a>
    </p>

    <p class="version-info">© 2025 MoviReco - ver. 2.3.0</p>
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

.version-info {
  margin-top: 5px;
  font-size: 11px;
  color: #666;
}
</style>