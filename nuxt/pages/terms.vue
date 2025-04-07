<template>
  <div class="terms-page">
    <Header />

    <div class="terms-container">
      <h1 class="terms-title">利用規約</h1>

      <section class="terms-section">
        <h2>第1条（適用）</h2>
        <p>
          本規約は、MoviReco（以下「当サービス」といいます）の利用条件を定めるものです。
          ご利用いただく全てのユーザーに適用されます。
        </p>
      </section>

      <section class="terms-section">
        <h2>第2条（禁止事項）</h2>
        <ul>
          <li>法令または公序良俗に違反する行為</li>
          <li>不正アクセスやスパム行為</li>
          <li>当サービスの運営を妨害する行為</li>
          <li>他のユーザーの個人情報を収集または蓄積する行為</li>
        </ul>
      </section>

      <section class="terms-section">
        <h2>第3条（免責事項）</h2>
        <p>
          当サービスは、正確な情報の提供に努めますが、その内容について保証するものではありません。
          利用に関連して発生した損害等について、当サービスは一切の責任を負いません。
        </p>
      </section>

      <section class="terms-section">
        <h2>第4条（サービスの提供停止等）</h2>
        <p>
          運営上または技術的理由により、事前の告知なくサービスの一部または全部を停止することがあります。
        </p>
      </section>

      <section class="terms-section">
        <h2>第5条（規約の変更）</h2>
        <p>
          当サービスは、必要と判断した場合には、本規約を変更することができます。
          変更後の規約は、本ページに掲載された時点で効力を生じます。
        </p>
      </section>

      <p class="terms-footer">制定日：2025年4月</p>
    </div>

    <Footer />
  </div>
</template>

<script setup>
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'
</script>

<style scoped>
.terms-page {
  background-color: #f9fafb;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.terms-container {
  max-width: 720px;
  margin: 40px auto;
  padding: 0 1.5rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
  padding-bottom: 2rem;
}

.terms-title {
  font-size: 1.8rem;
  font-weight: bold;
  text-align: center;
  margin: 1.5rem 0;
}

.terms-section {
  margin-bottom: 1.75rem;
}

.terms-section h2 {
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
  color: #111827;
}

.terms-section p,
.terms-section li {
  font-size: 0.95rem;
  line-height: 1.6;
  color: #374151;
}

.terms-section ul {
  list-style: disc;
  padding-left: 1.5rem;
}

.terms-footer {
  font-size: 0.85rem;
  color: #6b7280;
  text-align: right;
  margin-top: 2rem;
  padding-right: 1rem;
}
</style>
