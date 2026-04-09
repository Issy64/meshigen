package com.issy.meshigen.feature.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.issy.meshigen.R

data class DetailUiModel(
    val name: String,
    val category: String,
    val area: String,
    val description: String,
    val aiComment: String,
    val moodText: String,
    val suggestedDate: String, // yyyy-MM-dd
    val favorite: Boolean,
)

@Composable
internal fun DetailScreen() {
    val baseUiModel = remember {
        DetailUiModel(
            name = "小倉焼うどん",
            category = "麺",
            area = "小倉北区",
            description = "小倉発祥の焼うどん。香ばしいソースの香りともちもち食感が魅力です。",
            aiComment = "今日はしっかり食べたい気分にぴったり。鉄板で香ばしく仕上がる満足感が高い一品です。",
            moodText = "ガッツリ食べたい",
            suggestedDate = "2026-04-07",
            favorite = false,
        )
    }
    var favorite by rememberSaveable { mutableStateOf(baseUiModel.favorite) }

    DetailScreenContent(
        uiModel = baseUiModel.copy(favorite = favorite),
        onBackClick = { },
        onFavoriteClick = { favorite = !favorite },
        onDeleteClick = { },
        onOpenMapsClick = { },
    )
}

@Composable
internal fun DetailScreenContent(
    uiModel: DetailUiModel,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onOpenMapsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Header(
                    onBackClick = onBackClick,
                )
            }

            item {
                Text(
                    text = stringResource(R.string.detail_description),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            item {
                DetailMainCard(uiModel = uiModel)
            }

            item {
                SectionCard(
                    title = stringResource(R.string.detail_ai_comment_title),
                    body = uiModel.aiComment,
                )
            }

            item {
                Text(
                    text = stringResource(R.string.detail_mood_value, uiModel.moodText),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = stringResource(R.string.detail_suggested_date_value, uiModel.suggestedDate),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            item {
                ActionButtons(
                    favorite = uiModel.favorite,
                    onFavoriteClick = onFavoriteClick,
                    onDeleteClick = onDeleteClick,
                    onOpenMapsClick = onOpenMapsClick,
                )
            }
        }
    }
}

@Composable
private fun Header(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.detail_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        TextButton(onClick = onBackClick) {
            Text(text = stringResource(R.string.detail_back))
        }
    }
}

@Composable
private fun DetailMainCard(
    uiModel: DetailUiModel,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = uiModel.name,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = stringResource(R.string.detail_category_value, uiModel.category),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(R.string.detail_area_value, uiModel.area),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(R.string.detail_gourmet_description_value, uiModel.description),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun ActionButtons(
    favorite: Boolean,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onOpenMapsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val favoriteLabel = if (favorite) {
        stringResource(R.string.detail_action_remove_favorite)
    } else {
        stringResource(R.string.detail_action_add_favorite)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Button(
            onClick = onOpenMapsClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.detail_action_open_maps))
        }
        OutlinedButton(
            onClick = onFavoriteClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = favoriteLabel)
        }
        OutlinedButton(
            onClick = onDeleteClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.detail_action_delete))
        }
    }
}
