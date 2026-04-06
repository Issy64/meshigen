package com.issy.meshigen.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    var moodText by rememberSaveable { mutableStateOf("") }
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
                onClick = { }, // TODO: 提案ロジックを実装
                enabled = isButtonEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("おすすめを見る")
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "気分を入力すると、おすすめ結果がここに表示されます。",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
