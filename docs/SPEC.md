# 北九州B級グルメ案内アプリ 仕様書

**作成日**: 2026-03-30
**改訂日**: 2026-04-18
**仕様バージョン**: v1.3.0
**対象**: カラビナテクノロジー株式会社 2次面接（技術面談）
**ステータス**: コンセプト変更 → 仕様確定

---

## 概要

今の気分を入力すると、AIが北九州の実在B級グルメからぴったりの一品を提案してくれるアプリ。
「こんな気分なら、このB級グルメはどう？」という案内人の役割を担う。
実店舗の検索はGoogle Mapsに委ねる。

---

## コンセプト

**「気分テキスト」→「AIがマッチング」→「実在B級グルメと出会う」→「図鑑を埋める」**

- 入力例: 「雨でだるい、ガッツリ食べたい」
- 出力例: 「小倉の肉うどん」＋ AIによる紹介文＋ Google Mapsリンク

食べログやGoogle Mapsは「店を探す」アプリ。
このアプリは「北九州のB級グルメとの出会い」を提供する。

---

## このアプリを作る理由

| 理由 | 説明 |
|------|------|
| **ユーザー課題が明確** | 「今日何食べよう」は日常の反復課題。リピート性がある |
| **北九州ローカル性** | 地元企業の面接で「北九州のために作った」と言える |
| **技術デモ** | AI連携 + Room + Navigation + ViewModel を自然に使える |
| **設計判断を示せる** | 店舗DBを持たない設計判断＝データ鮮度リスクの回避を説明できる |

---

## 責任範囲の設計

| 対象 | アプリがやる | アプリがやらない |
|------|------------|----------------|
| グルメ知識 | 30種類のB級グルメデータ（名前・説明・カテゴリ・エリア） | 店舗DB・営業時間・レビュー |
| AI | 気分テキスト → グルメのマッチング＋紹介文生成 | 店舗検索・予約 |
| 店舗案内 | Google Mapsへのdeeplink | 自前の地図・ナビ |

> **設計意図**: 店舗情報の鮮度維持コストはMVPの範囲外。責任範囲を「グルメとの出会い」に限定し、店舗探しは既存サービスに委ねる。

## ID方針（仕様固定）

- `gourmetId`: アプリ層の主ID（画面導線・更新導線・Repository契約で使用）
- `collectionId`: DB内部キー（`gourmet_collection.id`）。アプリ導線では主IDとして扱わない

---

## 仕様バージョン運用・改定履歴方針

- 本仕様書は `docs/SPEC.md` を正本とし、仕様変更時は必ずこのファイルを更新する。
- 仕様変更時は `仕様バージョン` と `改訂日` を同時に更新する。
- バージョンは `vMAJOR.MINOR.PATCH` 形式で採番する。
  - MAJOR: 互換性を壊す仕様変更（ID方針変更・責務再定義など）
  - MINOR: 機能追加/方針追加（互換性を保つ）
  - PATCH: 文言修正・誤記修正・非機能な明確化
- 変更内容は末尾の「仕様改定履歴」に追記し、過去行は編集・削除しない（追記のみ）。
- 履歴は最小でも「日付 / バージョン / 区分 / 概要」を記録する。

---

## 主要機能（MVP）

### 1. 気分入力画面
- フリーテキストで今の気分を入力
- 「おすすめを聞く」ボタンをタップ

### 2. AIマッチング
- **API**: Gemini API（Firebase AI Logic SDK経由）
- **モデル**: `gemini-2.5-flash-lite`（最安・最速。thinkingデフォルト無効）
- **仕組み**: アプリ内のグルメデータ（30種類）を軽量フォーマットでプロンプトに含め、気分テキストに合うグルメを1〜3件選定
- **AIの返却**: `gourmet_id` + `reason`（紹介文）のみ。グルメの名前・説明等の表示データはローカルDBから取得
- **幻覚対策**: AIは候補IDの選択と紹介文生成のみ。存在しないグルメの創作を防ぐ
- **structured output**: `responseMimeType = "application/json"` + `responseSchema` でJSON構造を強制
- **フォールバック**: パース失敗・空配列・未知ID時はカテゴリベースのランダム提案に落とす

### 3. 提案結果表示
- グルメ名・カテゴリ・エリア
- AIが生成した紹介文（気分に寄り添った文章）
- 「Google Mapsで探す」ボタン（deeplink）
- 「図鑑に登録」ボタン（未発見のときのみ新規登録）

### 4. グルメ図鑑
- 発見済みグルメをカード形式で一覧（1グルメ1件）
- 発見数 / 30 を表示
- お気に入り登録・フィルタ
- タップで詳細表示

### 5. グルメ詳細
- グルメ名・カテゴリ・エリア・説明
- 初回発見時のAI紹介文・元の気分テキスト
- Google Mapsリンク
- お気に入り登録/解除
- 詳細導線・更新導線は `gourmetId` 基準で統一する（`collectionId` は内部キーとしてのみ利用）

---

## 画面構成（3〜4画面）

```
┌─────────────────────────────────────┐
│ [ホーム画面]                         │
│ ┌─────────────────────────────────┐ │
│ │ 「今の気分は？」                 │ │
│ │ テキスト入力欄                   │ │
│ │ [おすすめを聞く]                 │ │
│ └─────────────────────────────────┘ │
│                                     │
│ ┌─ 提案結果（1〜3件） ────────────┐ │
│ │ ┌──────────────────────────────┐ │ │
│ │ │「小倉の肉うどん」            │ │ │
│ │ │ 麺類 / 小倉南区              │ │ │
│ │ │ AI: ガッツリ気分にぴったり。 │ │ │
│ │ │     牛すじと生姜で元気出る... │ │ │
│ │ │ [📍 Mapsで探す] [📚 登録]   │ │ │
│ │ └──────────────────────────────┘ │ │
│ └─────────────────────────────────┘ │
│                                     │
│ [🏠 ホーム] [📚 図鑑]              │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ [図鑑一覧画面]                       │
│ フィルタ: [全て] [麺類] [甘味] ...   │
│ ┌──────────────────────────────────┐│
│ │「小倉の肉うどん」      4/2 発見  ││
│ │ 麺類 / 小倉南区           ❤     ││
│ └──────────────────────────────────┘│
│ ┌──────────────────────────────────┐│
│ │「門司港焼きカレー」    4/1 発見  ││
│ │ カレー / 門司区                  ││
│ └──────────────────────────────────┘│
│                                      │
│ [🏠 ホーム] [📚 図鑑]               │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ [図鑑詳細画面]                       │
│ ┌─────────────────────────────────┐ │
│ │ 【小倉の肉うどん】                │ │
│ │ カテゴリ: 麺類                    │ │
│ │ エリア: 小倉南区・北方            │ │
│ │                                   │ │
│ │ 説明:                             │ │
│ │ 牛すじ・ほほ肉を濃いだしで煮込   │ │
│ │ んだ小倉名物。おろし生姜をたっ   │ │
│ │ ぷり入れるのが定番。              │ │
│ │                                   │ │
│ │ AIの紹介文:                       │ │
│ │ 「ガッツリ気分のあなたには...」   │ │
│ │                                   │ │
│ │ 元の気分: 「雨でだるい」         │ │
│ │ 提案日: 2026-04-02               │ │
│ │                                   │ │
│ │ [📍 Mapsで探す]                  │ │
│ │ [❤ お気に入り] [🗑 削除]         │ │
│ └─────────────────────────────────┘ │
└─────────────────────────────────────┘
```

---

## グルメデータ（Room プリセット）

アプリ初回起動時にRoomにプリセットとして投入する。

### テーブル: `gourmets`（マスタデータ）

```sql
CREATE TABLE gourmets (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,              -- 「肉うどん」
    area TEXT NOT NULL,              -- 「小倉南区・北方」
    category TEXT NOT NULL,          -- 「麺類」
    description TEXT NOT NULL,       -- 説明文
    search_keyword TEXT NOT NULL     -- Google Maps検索用キーワード
);
```

### テーブル: `gourmet_collection`（発見図鑑）

ユーザーが提案結果から初めて出会ったグルメを記録するテーブル。
1グルメにつき1件のみ保持し、全提案履歴は持たない。

```sql
CREATE TABLE gourmet_collection (
    id INTEGER PRIMARY KEY AUTOINCREMENT, -- DB内部キー（アプリ層では主に使わない）
    gourmet_id INTEGER NOT NULL UNIQUE,   -- アプリ層の主ID（発見対象）
    mood_text TEXT NOT NULL,              -- 初回発見時の気分テキスト
    ai_comment TEXT NOT NULL,             -- 初回発見時のAI紹介文
    is_favorite BOOLEAN DEFAULT 0,
    created_at INTEGER NOT NULL,          -- 初回発見日時
    FOREIGN KEY (gourmet_id) REFERENCES gourmets(id)
);
```

### Entity (Kotlin)

```kotlin
@Entity(tableName = "gourmets")
data class Gourmet(
    @PrimaryKey val id: Int,
    val name: String,
    val area: String,
    val category: String,
    val description: String,
    val searchKeyword: String        // "肉うどん 北九州 小倉" など
)

@Entity(
    tableName = "gourmet_collection",
    foreignKeys = [ForeignKey(
        entity = Gourmet::class,
        parentColumns = ["id"],
        childColumns = ["gourmetId"]
    )]
)
data class GourmetCollection(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // DB内部キー
    @ColumnInfo(name = "gourmet_id") val gourmetId: Int, // アプリ層の主ID
    val moodText: String, // 初回発見時
    val aiComment: String, // 初回発見時
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis() // 初回発見日時
)
```

---

## プリセットデータ一覧（30種類）

| # | name | area | category |
|---|------|------|----------|
| 1 | 焼うどん | 小倉北区・鳥町 | 麺類 |
| 2 | 焼きカレー | 門司区・門司港 | カレー・洋食 |
| 3 | かしわうどん | 小倉北区・小倉駅 | 麺類 |
| 4 | 肉うどん | 小倉南区・北方 | 麺類 |
| 5 | 戸畑チャンポン | 戸畑区・中本町 | 麺類 |
| 6 | 八幡ぎょうざ | 八幡西区・黒崎 | 点心・揚げ焼き |
| 7 | ぬか炊き | 小倉北区・旦過 | 惣菜・郷土料理 |
| 8 | かしわめし | 八幡西区・折尾 | 駅弁・ご飯もの |
| 9 | サニーパン | 小倉北区・京町 | パン・軽食 |
| 10 | オムレット | 小倉北区・京町 | 甘味 |
| 11 | くろがね堅パン | 八幡東区 | 菓子・保存食 |
| 12 | くろがね羊羹 | 八幡東区 | 甘味 |
| 13 | ぽんつく | 小倉南区・下曽根 | 甘味 |
| 14 | 栗饅頭 | 小倉北区・魚町 | 甘味 |
| 15 | カナッペ | 小倉北区・旦過市場 | 揚げ物 |
| 16 | 豚まん | 小倉北区 | 点心 |
| 17 | 門司港バナナスイーツ | 門司区・門司港 | 甘味・飲み物 |
| 18 | バナナようかん | 門司区・門司港 | 甘味 |
| 19 | 河豚最中 | 門司区 | 甘味 |
| 20 | 門司港地ビール | 門司区・門司港 | 飲み物 |
| 21 | 関門ふぐ料理 | 門司区・門司港 | 魚介・鍋 |
| 22 | 豊前海一粒かき | 門司区・恒見 | 魚介 |
| 23 | かき飯 | 門司区・恒見 | ご飯もの |
| 24 | 豊前本ガニ | 門司区・恒見 | 魚介 |
| 25 | 合馬たけのこ | 小倉南区・合馬 | 山菜・食材 |
| 26 | 合馬筍ごはん | 小倉南区・合馬 | ご飯もの |
| 27 | 若松水切りトマト | 若松区 | 野菜・食材 |
| 28 | 瓦そば | 門司区・門司港（下関発祥） | 麺類 |
| 29 | かしわおにぎり | 八幡西区・折尾 | 軽食・ご飯もの |
| 30 | 小倉牛 | 小倉北区 | 肉料理 |

---

## Google Maps連携

### deeplinkの実装

```kotlin
fun openGoogleMaps(context: Context, keyword: String) {
    val uri = Uri.parse("geo:0,0?q=${Uri.encode(keyword)}")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps")
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        // Google Mapsアプリがない場合はブラウザで開く
        val webUri = Uri.parse("https://www.google.com/maps/search/${Uri.encode(keyword)}")
        context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
    }
}
```

- `searchKeyword` 例: `"肉うどん 北九州 小倉"`

---

## AIプロンプト設計

### 候補データの送信フォーマット（軽量化）

JSONの冗長な繰り返しキーを避け、1行フォーマットで送信する。

```
1|焼うどん|小倉北区・鳥町|麺類|小倉発祥、干しうどんを使ったB級グルメの代表格
2|焼きカレー|門司区・門司港|カレー|カレー+チーズ+卵をオーブンで焼く門司港名物
...
```

### プロンプト

```
あなたは北九州のB級グルメに詳しい案内人です。

以下のグルメリスト（id|name|area|category|description）の中から、
ユーザーの気分に合うものを1〜3件選んでください。

## グルメリスト
{軽量フォーマットで全候補を挿入}

## ユーザーの気分
{ユーザーの入力テキスト}
```

### structured output（Kotlin実装）

```kotlin
val schema = Schema.obj(
    mapOf(
        "selected" to Schema.array(
            Schema.obj(
                mapOf(
                    "id" to Schema.integer(),
                    "reason" to Schema.string()
                )
            )
        )
    )
)

val model = Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
    modelName = "gemini-2.5-flash-lite",
    systemInstruction = content {
        text("候補リストにあるグルメだけから選び、必ずidで返してください。リストにないグルメを創作しないでください。")
    },
    generationConfig = generationConfig {
        responseMimeType = "application/json"
        responseSchema = schema
        temperature = 0.2f
        maxOutputTokens = 256
    }
)
```

### AI出力例

```json
{
  "selected": [
    { "id": 4, "reason": "ガッツリ気分のあなたには、小倉の肉うどんがぴったり。牛すじと生姜で元気が出ます。" }
  ]
}
```

- AIは`id`と`reason`のみ返す。グルメの名前・説明・エリアはローカルDBから取得して表示する
- `responseMimeType` + `responseSchema` でJSON構造を強制（自由形式のJSON出力指示より信頼性が高い）
- パース失敗時・未知ID時はカテゴリベースのランダム提案にフォールバック

### トークン数・コストの見積もり

| 項目 | 値 |
|------|-----|
| 入力トークン数（30件候補 + プロンプト） | 約1,000〜2,500 tokens |
| 出力トークン数（1〜3件選定） | 約100〜200 tokens |
| 1回あたりコスト（flash-lite） | 約$0.0003 |
| 1万回使用時の累計コスト | 約$3 |

- `countTokens()` で実測して確認すること
- 30件程度ではトークン効率の問題は実用上発生しない

---

## 技術スタック

| 技術 | 用途 | 学習Unit | 状況 |
|------|------|---------|------|
| **Jetpack Compose + Material 3** | UI構築 | Unit 1-3 | ✅ 習得済み |
| **ViewModel + StateFlow (UDF)** | 状態管理 | Unit 4 | 🔄 学習中 |
| **Navigation Compose** | 画面遷移 | Unit 4 | 🔄 学習中 |
| **Room** | ローカルDB（マスタ＋コレクション） | Unit 6 | ⏳ これから |
| **Firebase AI Logic SDK (Gemini)** | AIマッチング（structured output） | 独自学習 | 🔄 学習中 |
| **Coroutines** | 非同期処理 | Unit 5 | ⏳ これから |

---

## システム構成

```
┌──────────────────────────────────┐
│   Android App                    │
│   (Jetpack Compose)              │
└──────────┬───────────┬───────────┘
           │           │
           ▼           ▼
┌──────────────┐ ┌──────────────────────┐
│  Room DB     │ │  Firebase AI Logic   │
│ (Local)      │ │  SDK + Gemini API    │
│              │ │                      │
│ gourmets           │ │ 気分 → マッチング   │
│ gourmet_collection │ │ ID+紹介文を返却      │
└──────────────┘ └──────────────────────┘
           │
           ▼
┌──────────────────────┐
│  Google Maps          │
│ (deeplink で遷移)     │
│ 店舗検索は委譲        │
└──────────────────────┘
```

---

## 面接での説明ストーリー（30秒版）

> 「今日何食べよう」を北九州のB級グルメで解決するアプリを作りました。気分をテキストで入力すると、Gemini AIが30種類の実在グルメから最適なものを提案し、紹介文を添えて表示します。
>
> 店舗情報は鮮度維持コストが高いため、アプリでは持たず、Google Mapsへのdeeplinkで委ねる設計にしました。この判断は「MVPで責任範囲をどこまで持つか」を意識した結果です。
>
> AIにはグルメのIDと紹介文だけを返させ、表示データはローカルDBから引くことで幻覚を防いでいます。技術構成は、Firebase AI Logic SDKのstructured outputでGeminiを呼び出し、Roomでグルメマスタと発見図鑑を管理、Navigation Composeで画面遷移、ViewModelで状態管理しています。

---

## リスクと対策

| リスク | 対策 |
|--------|------|
| **AIが存在しないグルメを提案する** | AIはIDのみ返却。表示データはローカルDBから取得。structured outputでJSON構造を強制。未知IDはフォールバック |
| **API呼び出しが遅い** | ローディング表示 + オフライン時は既存の発見図鑑を表示 |
| **無料枠を超える** | Gemini Free: 1,000req/日。デモ用途なら十分 |
| **API依存の開発** | モック実装でオフラインでもUI開発可能 |
| **グルメデータの追加・修正** | プリセットデータなので、アプリ更新で対応可能 |

---

## 将来構想（Phase 2以降）

- [ ] **店舗情報の軽量統合**: Google Places APIで近くの店を表示
- [ ] **シェア機能**: SNSに提案結果を画像で共有
- [ ] **地域拡張**: 北九州以外の都市対応
- [ ] **ユーザー投稿**: 新しいB級グルメの追加提案
- [ ] **季節・天候連動**: 天気APIと組み合わせた提案

---

## 実装ロードマップ（1週間MVP）

### Day 1-2: UI + Navigation
- [ ] Compose画面レイアウト（ホーム・図鑑一覧・詳細）
- [ ] Navigation Compose で画面遷移
- [ ] ViewModel + StateFlow で状態管理

### Day 3-4: Room + データ
- [ ] Room DB セットアップ（gourmets + gourmet_collection）
- [ ] countTokens() でプロンプトのトークン数を実測
- [ ] プリセットデータ投入
- [ ] 図鑑の表示・お気に入り・削除

### Day 5-6: AI連携 + Google Maps
- [ ] Firebase AI Logic SDK で Gemini 呼び出し
- [ ] structured output + responseSchema でJSON出力強制
- [ ] パース失敗時のフォールバック実装
- [ ] Google Maps deeplink実装

### Day 7: 仕上げ
- [ ] エラーハンドリング
- [ ] README・スクリーンショット
- [ ] GitHub公開

---

## 参考資料

- [Android Developers - Jetpack Compose](https://developer.android.com/courses/android-basics-compose)
- [Firebase AI Logic SDK](https://firebase.google.com/docs/ai-logic)
- [Firebase AI Logic - Structured Output](https://firebase.google.com/docs/vertex-ai/generate-structured-output)
- [Firebase AI Logic - Count Tokens](https://firebase.google.com/docs/ai-logic/count-tokens)
- [Firebase AI Logic - System Instructions](https://firebase.google.com/docs/ai-logic/system-instructions)
- [Google AI Studio](https://aistudio.google.com)
- [Gemini API Documentation](https://ai.google.dev)

---

## 仕様改定履歴

| 日付 | バージョン | 区分 | 概要 |
|------|----------|------|------|
| 2026-03-30 | v1.0.0 | 初版 | inboxから整理・ファイル化 |
| 2026-04-02 | v1.1.0 | コンセプト変更 | 架空→実在グルメ提案に転換。責任範囲を設計 |
| 2026-04-02 | v1.2.0 | AI設計改訂 | structured output採用。AIはID+理由のみ返却。テーブル名をgourmet_collectionに変更 |
| 2026-04-18 | v1.3.0 | 図鑑仕様改訂 | 発見図鑑（1グルメ1件）に統一。`gourmetId`をアプリ層主ID、`collectionId`をDB内部キーに固定 |
