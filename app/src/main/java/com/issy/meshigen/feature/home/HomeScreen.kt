package com.issy.meshigen.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.issy.meshigen.R

@Composable
internal fun HomeScreen() {
    var moodText by rememberSaveable { mutableStateOf("") }
    var resultUiState by remember { mutableStateOf<HomeRecommendationUiState>(HomeRecommendationUiState.Initial) }
    val keyboardController = LocalSoftwareKeyboardController.current

    HomeScreenContent(
        moodText = moodText,
        onMoodTextChange = { moodText = it },
        resultUiState = resultUiState,
        onRecommendClick = {
            resultUiState = HomeRecommendationUiState.Success(
                items = HomeDummyDataSource.createDummyRecommendations()
            )
            keyboardController?.hide()
        },
        onImeDone = { keyboardController?.hide() }
    )
}

@Composable
fun HomeScreenContent(
    moodText: String,
    onMoodTextChange: (String) -> Unit,
    resultUiState: HomeRecommendationUiState,
    onRecommendClick: () -> Unit,
    modifier: Modifier = Modifier,
    onImeDone: () -> Unit = { },
){

    val isButtonEnabled = moodText.isNotBlank()

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.home_title),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.home_description),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = moodText,
                onValueChange = { onMoodTextChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(stringResource(R.string.home_mood_label))
                },
                placeholder = {
                    Text(stringResource(R.string.home_mood_placeholder))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onImeDone() }
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    onRecommendClick()
                },
                enabled = isButtonEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.home_recommend_button))
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (resultUiState) {
                HomeRecommendationUiState.Initial -> {
                    Text(
                        text = stringResource(R.string.home_initial_hint),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                is HomeRecommendationUiState.Success -> {
                    RecommendationList(recs = resultUiState.items)
                }
            }
        }
    }
}

@Composable
private fun RecommendationList(
    recs: List<RecommendationUiModel>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(recs, key = { it.id }){ item ->
            RecommendationCard(item = item)
        }
    }
}

@Composable
private fun RecommendationCard(
    item: RecommendationUiModel,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column (
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.category,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
