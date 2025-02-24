package com.example.actapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.act1_pdm.Navegation.AppNavegation
import com.example.actapp.ViewModel.MyViewModel
import com.example.actapp.ui.theme.ActAppAPITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ActAppAPITheme {
                val vielModel = MyViewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavegation(Modifier.padding(innerPadding), vielModel)
                }
            }
        }
    }
}
