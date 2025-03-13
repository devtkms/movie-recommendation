# ベースイメージの指定
FROM node:22.9-alpine3.20

# 作業ディレクトリの設定
WORKDIR /app

# package.jsonとpackage-lock.jsonをコピー
COPY package*.json ./

# 依存関係のインストール
RUN npm install

# アプリケーションのソースコードをコピー
COPY . .

# 環境変数PATHにNode.jsのbinパスを追加
ENV PATH /app/node_modules/.bin:$PATH

# アプリケーションを起動（一時的にコメントアウト）
CMD ["npm", "run", "dev"]