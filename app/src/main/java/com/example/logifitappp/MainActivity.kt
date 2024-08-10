package com.example.logifitappp

import MainScreen
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.navigation.MainNavigation
import com.example.logifitappp.ui.theme.LogifitApppTheme

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LogifitApppTheme {

                navController = rememberNavController()
                MainNavigation(navigation = navController)
            }
        }
    }
}