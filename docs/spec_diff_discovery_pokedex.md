# 仕様差分メモ: 図鑑を「保存履歴」から「発見図鑑」に変更する

更新日: 2026-04-18
対象Issue: #48（詳細画面のRoom接続）に先行する仕様整理

## 今回固定する仕様（合意事項）
1. ID方針を固定する
- `gourmetId` をアプリ層の主IDとする（画面導線・更新導線・Repository契約）。
- `collectionId` は DB 内部キーとしてのみ扱う。

2. 仕様差分ドキュメントの正本をこのファイルに固定する
- 図鑑仕様の差分は `docs/spec_diff_discovery_pokedex.md` を一次情報とする。

3. Issue #48 のDoD補足を固定する
- 「詳細導線・更新導線は `gourmetId` 基準」を #48 のDoDに追記し、実装と検証の基準を統一する。

## 背景
現在の実装/仕様は、`gourmet_collection` を「提案を保存したレコード」として扱っている。
一方で本来目指す図鑑は、ポケモン図鑑のように「初めて出会ったグルメを1種類につき1件だけ記録し、発見数を見せる」体験である。

## 差分（Before / After）
1. 図鑑の意味
- Before: 保存した提案の一覧（履歴寄り）
- After: 発見済みグルメ一覧（1グルメ1レコード）

2. 保存ルール
- Before: 同じ `gourmet_id` を複数回保存できる
- After: `gourmet_id` は一意。初回発見のみ登録し、再提案では重複登録しない

3. 日付の意味
- Before: その保存レコードを作った日時
- After: 初回発見日時（`first_discovered_at` と同義。既存の `created_at` をこの意味で運用）

4. お気に入り
- Before: 保存レコード単位
- After: 発見済みグルメ単位（結果的に1グルメ1件なので同じ）

5. 図鑑の主要指標
- Before: 保存件数
- After: 発見数 / 30（例: 8/30）

## DB設計方針（最小変更）
- テーブル追加はせず、`gourmet_collection` をそのまま発見図鑑テーブルとして運用する
- `gourmet_id` に UNIQUE 制約（Unique Index）を付与する
- 挿入は `IGNORE` を使い、重複発見は無視して既存レコードを維持する

## 既存データの移行方針
重複データが存在した場合は、次を採用する。
- 残すレコード: `created_at` が最小（最初に発見した1件）
- お気に入り: 同一 `gourmet_id` の中で1件でも `is_favorite = 1` があれば残すレコードを `1` に更新
- それ以外の重複レコードは削除

## 非対象（この変更ではやらない）
- ホーム画面の「図鑑に保存」ボタン連携
- 図鑑画面での進捗UI（例: 8/30 のプログレス）
- Gemini APIとの接続

## 今回の実装反映範囲
- `GourmetCollectionEntity` に `gourmet_id` 一意制約を追加
- `CollectionDao.insert` を `OnConflictStrategy.IGNORE` に変更
- DBバージョンを上げ、重複整理 + UNIQUE Index 追加のMigrationを実装
