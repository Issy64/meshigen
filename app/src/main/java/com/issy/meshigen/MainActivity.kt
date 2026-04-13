package com.issy.meshigen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.issy.meshigen.data.local.DatabaseProvider
import com.issy.meshigen.navigation.MeshigenAppShell
import com.issy.meshigen.ui.theme.MeshigenTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch(Dispatchers.IO) {
            DatabaseProvider.get(applicationContext)
        }

        setContent {
            MeshigenTheme {
                MeshigenAppShell()
            }
        }
    }
}
