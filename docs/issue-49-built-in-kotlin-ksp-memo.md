# Issue #49: AGP 9 built-in Kotlin + KSP sourceSets 警告対応メモ

## 1. 目的
- 対象Issue: [#49](https://github.com/Issy64/meshigen/issues/49)
- 目的: `android.disallowKotlinSourceSets=false` に依存しない状態で、Room + KSP の build/sync を安定化する

## 2. 発生していた現象
- ビルド/Sync時に以下メッセージが出る

```text
Using kotlin.sourceSets DSL to add Kotlin sources is not allowed with built-in Kotlin.
Kotlin source set 'debug/release' contains: app/build/generated/ksp/...
Solution: Use android.sourceSets DSL instead.
To suppress this error, set android.disallowKotlinSourceSets=false in gradle.properties.
```

## 3. 再現手順（過去状態）
1. `gradle.properties` に `android.disallowKotlinSourceSets=false` を入れない状態にする
2. 以下を実行する

```bash
./gradlew -Pandroid.disallowKotlinSourceSets=true :app:compileDebugKotlin
```

3. 上記の `kotlin.sourceSets DSL` エラーが出る

## 4. 最終設定（2026-04-20）
- `gradle.properties`
  - `android.disallowKotlinSourceSets=false` を使わない（コメントアウト）
- `gradle/libs.versions.toml`
  - `agp = "9.0.1"`
  - `kotlin = "2.3.20"`
  - `ksp = "2.3.6"`
  - `composeBom = "2026.03.01"`
  - `navigationCompose = "2.9.7"`
  - `androidx-compose-material-icons-extended` を追加
- `app/build.gradle.kts`
  - `implementation(libs.androidx.compose.material.icons.extended)` を追加

## 5. 検証結果
### 5.1 CLI build
```bash
./gradlew :app:compileDebugKotlin
```
- 結果: `BUILD SUCCESSFUL`

### 5.2 Android Studio Sync
- `Sync Project with Gradle Files` 実行
- 結果: `BUILD SUCCESSFUL` を確認

## 6. 補足
- `Icons.*` を利用するUI実装に対し、`material-icons-extended` 依存が必要だったため追加した
- 本対応で、Issue #49 のDone criteriaにある「回避設定なしで build/sync 成功」を満たす
