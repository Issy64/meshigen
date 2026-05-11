package com.issy.meshigen.feature.home

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.issy.meshigen.ui.theme.MeshigenTheme

/**
 * 以下はプレビュー用のコードです。実際のアプリでは不要ですが、UIの見た目を確認するために用意しています。
 */

@Preview(showBackground = true, name = "Home Initial")
@Composable
private fun HomeScreenInitialPreview() {
    MeshigenTheme {
        HomeScreenContent(
            uiState = HomeUiState(
                moodText = "",
                recommendationUiState = HomeRecommendationUiState.Initial,
            ),
            onEvent = {},
            onOpenDetailClick = {},
            modifier = Modifier,
        )
    }
}

@Preview(showBackground = true, name = "Home Success")
@Composable
private fun HomeScreenSuccessPreview() {
    MeshigenTheme {
        HomeScreenContent(
            uiState = HomeUiState(
                moodText = "甘い物",
                recommendationUiState = HomeRecommendationUiState.Success(HomeDummyDataSource.createDummyRecommendation()),
            ),
            onEvent = {},
            onOpenDetailClick = {},
            modifier = Modifier,
        )
    }
}

@Preview(name = "Home Initial Narrow", widthDp = 320, heightDp = 640, showBackground = true)
@Composable
private fun HomeScreenInitialNarrowPreview() {
    MeshigenTheme {
        HomeScreenContent(
            uiState = HomeUiState(
                moodText = "",
                recommendationUiState = HomeRecommendationUiState.Initial,
            ),
            onEvent = {},
            onOpenDetailClick = {},
            modifier = Modifier,
        )
    }
}

@Preview(name = "Home Success Narrow", widthDp = 320, heightDp = 640, showBackground = true)
@Composable
private fun HomeScreenSuccessNarrowPreview() {
    MeshigenTheme {
        HomeScreenContent(
            uiState = HomeUiState(
                moodText = "甘い物",
                recommendationUiState = HomeRecommendationUiState.Success(HomeDummyDataSource.createDummyRecommendation()),
            ),
            onEvent = {},
            onOpenDetailClick = {},
            modifier = Modifier,
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun HomeScreenInitialDarkModePreview() {
    MeshigenTheme {
        HomeScreenContent(
            uiState = HomeUiState(
                moodText = "",
                recommendationUiState = HomeRecommendationUiState.Initial,
            ),
            onEvent = {},
            onOpenDetailClick = {},
            modifier = Modifier,
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun HomeScreenSuccessDarkModePreview() {
    MeshigenTheme {
        HomeScreenContent(
            uiState = HomeUiState(
                moodText = "甘い物",
                recommendationUiState = HomeRecommendationUiState.Success(HomeDummyDataSource.createDummyRecommendation()),
            ),
            onEvent = {},
            onOpenDetailClick = {},
            modifier = Modifier,
        )
    }
}
