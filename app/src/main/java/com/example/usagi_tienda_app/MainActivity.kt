package com.example.usagi_tienda_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

// actividad principal que arranca la app configura compose y muestra usagiapp
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // llama al composable principal que define la navegacion y pantallas
        setContent {
            UsagiApp()
        }
    }
}