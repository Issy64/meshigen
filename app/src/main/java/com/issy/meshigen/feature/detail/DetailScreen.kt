package com.issy.meshigen.feature.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.issy.meshigen.R
import com.issy.meshigen.data.local.DatabaseProvider
import com.issy.meshigen.data.repository.DetailRepositoryImpl

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
internal fun DetailScreen(
    gourmetId: String,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current

    val factory = remember(context) {
        val db = DatabaseProvider.get(context)
        val repository = DetailRepositoryImpl(db.CollectionDao())
        DetailViewModelFactory(repository)
    }

    val detailViewModel: DetailViewModel = viewModel(factory = factory)
    val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(gourmetId) {
        detailViewModel.load(gourmetId)
    }

    when (val state = uiState) {
        DetailUiState.Loading -> LoadingContent()
        DetailUiState.NotFound -> NotFoundContent(onBackClick)
        is DetailUiState.Ready -> DetailScreenContent(
            uiModel = state.item,
            onBackClick = onBackClick,
            onToggleFavoriteClick = { /* Step9で接続 */ },
            onDeleteClick = { /* Step9で接続 */ },
            onOpenMapClick = { /* Step9で接続 */ },
        )
    }
}
@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.detail_loading),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun NotFoundContent(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.detail_not_found),
                style = MaterialTheme.typography.bodyMedium,
            )
            TextButton(onClick = onBackClick) {
                Text(text = stringResource(R.string.detail_back))
            }
        }
    }
}

@Composable
internal fun DetailScreenContent(
    uiModel: DetailUiModel,
    onBackClick: () -> Unit,
    onToggleFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onOpenMapClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Header(
                onBackClick = onBackClick,
            )

            Text(
                text = stringResource(R.string.detail_description),
                style = MaterialTheme.typography.bodyMedium,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                DetailMainCard(uiModel = uiModel)

                SectionCard(
                    title = stringResource(R.string.detail_section_description),
                    body = uiModel.description,
                    maxLines = Int.MAX_VALUE,
                    overflow = TextOverflow.Clip,
                )
                SectionCard(
                    title = stringResource(R.string.detail_ai_comment_title),
                    body = uiModel.aiComment,
                    maxLines = Int.MAX_VALUE,
                    overflow = TextOverflow.Clip,
                )
                SectionCard(
                    title = stringResource(R.string.detail_section_original_mood),
                    body = uiModel.moodText,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
                SectionCard(
                    title = stringResource(R.string.detail_section_suggested_date),
                    body = uiModel.suggestedDate,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                ActionButtons(
                    favorite = uiModel.favorite,
                    onToggleFavoriteClick = onToggleFavoriteClick,
                    onDeleteClick = onDeleteClick,
                    onOpenMapClick = onOpenMapClick,
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
                text = stringResource(R.string.detail_section_basic_info),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = uiModel.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = stringResource(R.string.detail_category_value, uiModel.category),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = stringResource(R.string.detail_area_value, uiModel.area),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    body: String,
    maxLines: Int,
    overflow: TextOverflow,
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
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
                maxLines = maxLines,
                overflow = overflow,
            )
        }
    }
}

@Composable
private fun ActionButtons(
    favorite: Boolean,
    onToggleFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onOpenMapClick: () -> Unit,
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
            onClick = onOpenMapClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.detail_action_open_maps))
        }
        if (favorite) {
            Button(
                onClick = onToggleFavoriteClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
            ) {
                Text(text = favoriteLabel)
            }
        } else {
            OutlinedButton(
                onClick = onToggleFavoriteClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = favoriteLabel)
            }
        }
        Button(
            onClick = onDeleteClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
            ),
        ) {
            Text(text = stringResource(R.string.detail_action_delete))
        }
    }
}
