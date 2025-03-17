package com.towhid.swpc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Surface

import androidx.compose.ui.Modifier

import androidx.navigation.compose.rememberNavController


import com.towhid.swpc.ui.theme.SwpcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            SwpcTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val navController = rememberNavController()
                    MainNavigation() // Pass start destination to MainScreen
                }
            }
        }
    }
}

//@Composable
//fun MainScreen(startDestination : String, C) {
//    val loginResult by viewModel.loginResult.collectAsState()
//
//    if (startDestination == "login" || loginResult == null) {
//        LoginScreen(viewModel)
//    } else {
//        DeviceListScreen(3)
//    }
//}