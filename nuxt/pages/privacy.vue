<template>
  <div>
    <Header />

    <div class="container">
      <h1>プライバシーポリシー</h1>

      <p>当サイトでは、Google Analytics を使用して、ユーザーの利用状況を分析しています。</p>

      <p>
        Google Analytics は、トラフィックデータの収集のために Cookie を使用しています。
        収集される情報には、IPアドレス（一部マスキングされ匿名化）や閲覧ページ、利用時間などが含まれます。
        これらのデータは、サービスの改善のために使用され、個人を特定するものではありません。
      </p>

      <p>
        Google Analytics の詳細は
        <a href="https://marketingplatform.google.com/about/analytics/terms/jp/" target="_blank" rel="noopener noreferrer">
          Google アナリティクス利用規約
        </a>
        をご確認ください。また、
        <a href="https://tools.google.com/dlpage/gaoptout" target="_blank" rel="noopener noreferrer">
          Google のオプトアウトアドオン
        </a>
        により収集を拒否できます。
      </p>

      <p>
        本サービスは TMDb の API を利用していますが、TMDb により承認・認定されたものではありません。
      </p>

      <h2>個人情報の収集と利用について</h2>

      <p>当サービスでは、ユーザー登録時に以下の情報を収集することがあります：</p>
      <ul>
        <li>メールアドレス</li>
        <li>ニックネーム</li>
        <li>パスワード（ハッシュ化して保存）</li>
        <li>好きな映画や視聴傾向に関する情報（任意）</li>
      </ul>

      <p>収集した情報は、以下の目的で利用します：</p>
      <ul>
        <li>ユーザー認証およびサービスの提供</li>
        <li>お問い合わせ対応</li>
        <li>サービスの改善および新機能の案内</li>
      </ul>

      <h2>情報の管理と保護について</h2>
      <p>
        取得した個人情報は、以下のような措置により安全に管理されます：
      </p>
      <ul>
        <li>暗号化通信（TLS）によるデータの送受信</li>
        <li>パスワードはハッシュ化して保存</li>
        <li>管理画面やデータベースへのアクセス制限</li>
      </ul>

      <p>
        また、法令に基づく場合を除き、ご本人の同意なく第三者に提供することはありません。
      </p>

      <h2>ユーザーの権利について</h2>
      <p>
        ユーザーは、登録された個人情報について、開示・訂正・利用停止・削除を求めることができます。ご希望の際は、お問い合わせフォームよりご連絡ください。
      </p>

      <h2>プライバシーポリシーの変更</h2>
      <p>
        本プライバシーポリシーは、法令の改正やサービス内容の変更に応じて、予告なく改定されることがあります。重要な変更がある場合は、当サイト上で告知いたします。
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
  line-height: 1.8;
}
</style>