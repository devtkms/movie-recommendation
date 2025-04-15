export default defineNuxtConfig({
  compatibilityDate: "2024-11-01",
  devtools: { enabled: false },
  ssr: true,  // ✅ SSR を有効化
  nitro: {
    preset: "node-server",  // ✅ Render で動作するように Nitro を設定
  },
  devServer: {
    port: Number(process.env.PORT) || 3000, // ✅ PORT を number に変換
  },
  runtimeConfig: {
    public: {
      // apiBase: 'http://localhost:8080'　// 開発環境
      // apiBase: 'https://movie-recommendation-uybc.onrender.com'　// 本番環境
      apiBase: 'https://api.movireco.com'　// 本番環境
    }
  }
});

