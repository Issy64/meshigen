package com.issy.meshigen.feature.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.issy.meshigen.R

data class CollectionListUiModel(
    val id: String,
    val name: String,
    val category: String,
    val area: String,
    val suggestedDate: String, // yyyy-MM-dd
    val favorite: Boolean,
)

data class CollectionFilterUiModel(
    val id: String,
    val label: String,
    val selected: Boolean,
)

@Composable
internal fun CollectionListScreen() {
    val filters = listOf(
        CollectionFilterUiModel(
            id = "all",
            label = stringResource(R.string.collection_filter_all),
            selected = true,
        ),
        CollectionFilterUiModel(
            id = "noodle",
            label = stringResource(R.string.collection_filter_noodle),
            selected = false,
        ),
        CollectionFilterUiModel(
            id = "rice",
            label = stringResource(R.string.collection_filter_rice),
            selected = false,
        ),
    )

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
internal fun CollectionListScreenContent(
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

            FilterRow(
                filters = filters,
                onFilterClick = onFilterClick,
            )

            if (items.isEmpty()) {
                EmptyState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(items = items, key = { it.id }) { item ->
                        CollectionListCard(
                            item = item,
                            onClick = { onItemClick(item) },
                            onFavoriteClick = { onFavoriteClick(item) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterRow(
    filters: List<CollectionFilterUiModel>,
    onFilterClick: (CollectionFilterUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = filters, key = { it.id }) { filter ->
            FilterChip(
                selected = filter.selected,
                onClick = { onFilterClick(filter) },
                label = { Text(text = filter.label) },
            )
        }
    }
}

@Composable
private  fun CollectionListCard(
    item: CollectionListUiModel,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${item.category}/${item.area}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = item.suggestedDate,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            FavoriteIcon(
                favorite = item.favorite,
                onClick = onFavoriteClick,
            )
        }
    }
}

@Composable
private fun FavoriteIcon( // 使い回すかもしれないので切り出し
    favorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val imageVector = if(favorite) Icons.Filled.Favorite  else  Icons.Outlined.FavoriteBorder
    val contentDescription = if(favorite) {
        stringResource(R.string.collection_favorite_remove)
    } else {
        stringResource(R.string.collection_favorite_add)
    }

    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
        )
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column() {
        Text(
            text = stringResource(R.string.collection_list_empty_title),
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.collection_list_empty_message),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
