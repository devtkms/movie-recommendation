<template>
  <div>
    <Header />

    <div class="container">
      <h1>プライバシーポリシー</h1>

      <p>当サイトでは、Google Analytics を使用して、ユーザーの利用状況を分析しています。</p>

      <p>
        Google Analytics は、トラフィックデータの収集のために Cookie を使用しています。
        収集される情報には、IPアドレス（一部マスキングされ匿名化）や閲覧したページ、利用時間などが含まれます。
        これらのデータは、サービスの改善のために使用され、個人を特定するものではありません。
      </p>

      <p>
        Google Analytics の詳細については、
        <a href="https://marketingplatform.google.com/about/analytics/terms/jp/" target="_blank" rel="noopener noreferrer">
          Google アナリティクス利用規約
        </a>
        をご確認ください。
      </p>

      <p>
        また、ユーザーは
        <a href="https://tools.google.com/dlpage/gaoptout" target="_blank" rel="noopener noreferrer">
          Google のオプトアウトアドオン
        </a>
        を利用することで、Google Analytics によるデータ収集を拒否できます。
      </p>

      <p>
        本サービスは TMDb の API を利用していますが、TMDb により承認・認定されたものではありません。
      </p>
    </div>

    <Footer />
  </div>
</template>

<script setup>
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'
</script>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}
</style>