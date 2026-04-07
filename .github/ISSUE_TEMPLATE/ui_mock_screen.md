---
name: "UI仮画面テンプレート"
about: "Composeで画面の仮UIを作るIssue用テンプレート"
title: "[UI] <画面名>の仮UIをComposeで作成する"
labels: ""
assignees: ""
---

## 目的
<このIssueで達成したいことを2行程度で書く>

## 対象ファイル
- `app/src/main/java/com/issy/meshigen/feature/<feature>/<ScreenName>.kt`
- `app/src/main/java/com/issy/meshigen/feature/<feature>/<ScreenName>Preview.kt`
- `app/src/main/res/values/strings.xml`

## 非スコープ
- <このIssueではやらないこと1>
- <このIssueではやらないこと2>
- <このIssueではやらないこと3>

## 実装タスク
- [ ] UIモデルを最小定義する
- [ ] `Screen()` を作成し、仮データを供給する
- [ ] `ScreenContent(...)` を作成し、表示とイベント受け口を分離する
- [ ] `Scaffold` で画面骨組みを作成する
- [ ] 主要UI要素を実装する（一覧/詳細/ボタンなど）
- [ ] 空状態または状態差分のUIを実装する
- [ ] Previewを追加する（通常/狭幅/ダーク）
- [ ] 文言を `strings.xml` に追加する

## 検証タスク
- [ ] `./gradlew :app:compileDebugKotlin` が成功する
- [ ] Previewでレイアウト破綻がない
- [ ] 仕様の主要要素が表示される

## 完了条件（DoD）
- [ ] 主要要素が画面上で確認できる
- [ ] 表示責務とイベント受け口が分離されている
- [ ] 文言がハードコードされていない
- [ ] Kotlinコンパイルが通る
