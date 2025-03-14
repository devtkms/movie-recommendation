export default defineNuxtConfig({
  compatibilityDate: "2024-11-01",
  devtools: { enabled: true },
  devServer: {
    port: Number(process.env.PORT) || 3000  // ✅ PORT を number に変換
  }
});