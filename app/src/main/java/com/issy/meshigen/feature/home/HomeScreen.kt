package com.issy.meshigen.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

data class RecommendationUiModel(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
)
sealed interface HomeResultUiState {
    data object Initial : HomeResultUiState
    data class Success(val items: List<RecommendationUiModel>) : HomeResultUiState
}

private fun createDummyRecommendations(moodText: String): List<RecommendationUiModel> {
    return listOf(
        RecommendationUiModel(
            id = "kokura_yaki_udon",
            name = "小倉焼うどん",
            description = "香ばしくて満足感があり、ガッツリ食べたい気分に合います。",
            category = "麺"
        ),
        RecommendationUiModel(
            id = "yaki_curry",
            name = "焼きカレー",
            description = "熱々で濃厚なので、ちょっと元気を出したい時に向いています。",
            category = "ご飯もの"
        )
    )
}


@Composable
fun HomeScreen() {
    var moodText by rememberSaveable { mutableStateOf("") }
    var resultUiState by remember { mutableStateOf<HomeResultUiState>(HomeResultUiState.Initial) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val isButtonEnabled = moodText.isNotBlank()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "今の気分は？",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "今の気分を入力すると、おすすめの北九州市のB級グルメを提案します。",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = moodText,
                onValueChange = { moodText = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("今の気分")
                },
                placeholder = {
                    Text("たとえば (ガッツリ食べたい / 甘いものが欲しい)")
                },
                singleLine = false,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    resultUiState = HomeResultUiState.Success(
                        items = createDummyRecommendations(moodText),
                    )
                    keyboardController?.hide()
                },
                enabled = isButtonEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("おすすめを見る")
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (val state = resultUiState) {
                HomeResultUiState.Initial -> {
                    Text(
                        text = "気分を入力すると、おすすめ結果がここに表示されます。",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                is HomeResultUiState.Success -> {
                    RecommendationList(items = state.items)
                }
            }
        }
    }
}

@Composable
private fun RecommendationList(
    items: List<RecommendationUiModel>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items.forEach { item ->
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
        Column(
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
