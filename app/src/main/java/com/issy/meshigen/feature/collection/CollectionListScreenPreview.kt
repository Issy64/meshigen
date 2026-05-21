package com.issy.meshigen.feature.collection

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.issy.meshigen.ui.theme.MeshigenTheme

private val previewFilters = listOf(
    CollectionFilterUiModel(
        id = "all",
        label = "すべて",
        selected = true,
    ),
    CollectionFilterUiModel(
        id = "undiscovered",
        label = "未発見",
        selected = false,
    ),
    CollectionFilterUiModel(
        id = "discovered",
        label = "発見済み",
        selected = false,
    ),
    CollectionFilterUiModel(
        id = "favorite",
        label = "お気に入り",
        selected = false,
    ),
)

private val previewItems = listOf(
    CollectionListUiModel(
        gourmetId = "kokura-yakiudon",
        collectionId = "1",
        name = "小倉焼うどん",
        category = "麺",
        area = "小倉北区",
        suggestedDate = "2026-04-09", // yyyy-MM-dd
        discovered = false,
        favorite = true,
    )
)

private val previewItemsLongState = listOf(
    CollectionListUiModel(
        gourmetId = "kokura-yakiudon",
        collectionId = "1",
        name = "門司港発祥・特製スパイス香る超濃厚スタミナ焼うどん（大盛り・追い玉子付き）",
        category = "麺料理と鉄板グルメの合わせ技",
        area = "小倉北区魚町銀天街アーケード沿いの老舗エリア",
        suggestedDate = "2026-04-09", // yyyy-MM-dd
        discovered = true,
        favorite = true,
    )
)

@Preview(name = "Collection Normal", showBackground = true)
@Composable
private fun CollectionListScreenPreview() {
    MeshigenTheme {
        CollectionListScreenContent(
            filters = previewFilters,
            items = previewItemsLongState,
            discoveredCount = 1,
            onFilterClick = {},
            onItemClick = {},
            onFavoriteClick = {},
        )
    }
}

@Preview(name = "Collection Empty", showBackground = true)
@Composable
private fun CollectionEmptyListScreenPreview() {
    MeshigenTheme {
        CollectionListScreenContent(
            filters = previewFilters,
            items = emptyList(),
            discoveredCount = 1,
            onFilterClick = {},
            onItemClick = {},
            onFavoriteClick = {},
        )
    }
}

@Preview(name = "Collection Narrow", widthDp = 320, heightDp = 640, showBackground = true)
@Composable
private fun CollectionNarrowListScreenPreview() {
    MeshigenTheme {
        CollectionListScreenContent(
            filters = previewFilters,
            items = previewItems,
            discoveredCount = 1,
            onFilterClick = {},
            onItemClick = {},
            onFavoriteClick = {},
        )
    }
}

@Preview(name = "Collection Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun CollectionDarkListScreenPreview() {
    MeshigenTheme {
        CollectionListScreenContent(
            filters = previewFilters,
            items = previewItems,
            discoveredCount = 1,
            onFilterClick = {},
            onItemClick = {},
            onFavoriteClick = {},
        )
    }
}
