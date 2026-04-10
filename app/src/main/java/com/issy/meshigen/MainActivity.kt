package com.issy.meshigen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.issy.meshigen.navigation.MeshigenAppShell
import com.issy.meshigen.ui.theme.MeshigenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeshigenTheme {
                MeshigenAppShell()
            }
        }
    }
}
