package com.issy.meshigen.feature.detail

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.issy.meshigen.ui.theme.MeshigenTheme

private val previewDetailNormal = DetailUiModel(
    name = "小倉焼うどん",
    category = "麺",
    area = "小倉北区",
    description = "小倉発祥の焼うどん。香ばしいソースの香りともちもち食感が魅力です。",
    aiComment = "今日はしっかり食べたい気分にぴったり。鉄板で香ばしく仕上がる満足感が高い一品です。",
    moodText = "ガッツリ食べたい",
    suggestedDate = "2026-04-09", // yyyy-MM-dd
    favorite = false,
)

private val previewDetailLongText = DetailUiModel(
    name = "門司港特製スパイス香る海鮮ミックス焼きカレー（半熟卵トッピング）",
    category = "焼きカレーと鉄板グルメ",
    area = "門司区レトロ地区の海沿いエリア",
    description = "じっくり煮込んだ濃厚なカレールーに魚介の旨みを重ね、香ばしいチーズをのせてオーブンで焼き上げた一皿です。熱々のまま最後まで楽しめる、満足感の高いご当地グルメです。",
    aiComment = "集中して頑張った日のご褒美に向いています。コクのある味わいと香ばしい焼き目が気分を切り替えやすく、ゆっくり食べることで満足感も高まりそうです。",
    moodText = "今日はちょっと疲れたけど、しっかり食べて気分を上げたい",
    suggestedDate = "2026-04-09", // yyyy-MM-dd
    favorite = true,
)

@Preview(name = "Detail Normal", showBackground = true)
@Composable
private fun DetailScreenNormalPreview() {
    MeshigenTheme {
        DetailScreenContent(
            uiModel = previewDetailNormal,
            onBackClick = {},
            onToggleFavoriteClick = {},
            onDeleteClick = {},
            onOpenMapClick = {},
        )
    }
}

@Preview(name = "Detail Long Text", showBackground = true)
@Composable
private fun DetailScreenLongTextPreview() {
    MeshigenTheme {
        DetailScreenContent(
            uiModel = previewDetailLongText,
            onBackClick = {},
            onToggleFavoriteClick = {},
            onDeleteClick = {},
            onOpenMapClick = {},
        )
    }
}

@Preview(name = "Detail Narrow", widthDp = 320, heightDp = 640, showBackground = true)
@Composable
private fun DetailScreenNarrowPreview() {
    MeshigenTheme {
        DetailScreenContent(
            uiModel = previewDetailLongText,
            onBackClick = {},
            onToggleFavoriteClick = {},
            onDeleteClick = {},
            onOpenMapClick = {},
        )
    }
}

@Preview(name = "Detail Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DetailScreenDarkPreview() {
    MeshigenTheme {
        DetailScreenContent(
            uiModel = previewDetailNormal,
            onBackClick = {},
            onToggleFavoriteClick = {},
            onDeleteClick = {},
            onOpenMapClick = {},
        )
    }
}
