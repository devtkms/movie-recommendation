export default defineNuxtConfig({
  compatibilityDate: "2024-11-01",
  devtools: { enabled: true },
  ssr: true,  // ✅ SSR を有効化
  nitro: {
    preset: "node-server",  // ✅ Render で動作するように Nitro を設定
  },
  devServer: {
    port: Number(process.env.PORT) || 3000, // ✅ PORT を number に変換
  },
  runtimeConfig: {
    public: {
      apiBase: process.env.API_BASE_URL || "http://localhost:8080/api",  // ✅ 環境変数がなければローカル用
    }
  }
});

