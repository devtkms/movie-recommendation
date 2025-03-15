import fetch from "node-fetch"; // ESモジュールの `fetch`

// const API_URL = "http://localhost:8080/health";
const API_URL = "https://pattocinema-api.onrender.com/health"; // ✅ /health にリクエスト

async function keepAwake() {
    try {
        const response = await fetch(API_URL);
        console.log(`[${new Date().toISOString()}] Ping sent: ${response.status}`);
    } catch (error) {
        console.error(`[${new Date().toISOString()}] Error:`, error);
    }
}


// 5分ごとにリクエストを送る
setInterval(keepAwake, 5 * 60 * 1000);

// 初回実行
keepAwake();