package com.issy.meshigen.feature.collection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.issy.meshigen.R
import kotlin.collections.listOf

data class CollectionListUiModel(
    val id: String,
    val name: String,
    val category: String,
    val area: String,
    val suggestedDate: String, // yyyy-MM-dd
    val favorite: Boolean,
)

data class CollectionFilterUiModel(
    val label: String,
    val selected: Boolean,
)

@Composable
internal fun CollectionListScreen() {
    val filters = remember {
        listOf(
            CollectionFilterUiModel(label = "すべて", selected = true),
            CollectionFilterUiModel(label = "麺", selected = false),
            CollectionFilterUiModel(label = "ごはん", selected = false),
        )
    }

    val items = remember {
        listOf(
            CollectionListUiModel(
                id = "kokura-yakiudon",
                name = "小倉焼うどん",
                category = "麺",
                area = "小倉北区",
                suggestedDate = "2026-04-07", // yyyy-MM-dd
                favorite = false,
            )
        )
    }

    CollectionListScreenContent(
        filters = filters,
        items = items,
        onFilterClick = {},
        onItemClick = {},
        onFavoriteClick = {},
    )
}

@Composable
private fun CollectionListScreenContent(
    filters: List<CollectionFilterUiModel>,
    items: List<CollectionListUiModel>,
    onFilterClick: (CollectionFilterUiModel) -> Unit,
    onItemClick: (CollectionListUiModel) -> Unit,
    onFavoriteClick: (CollectionListUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.collection_list_title),
                style = MaterialTheme.typography.headlineMedium,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.collection_list_description),
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // TODO: フィルタのUIを実装(ISSUE#12)
            
        }
    }
}