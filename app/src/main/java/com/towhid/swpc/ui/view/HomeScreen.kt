package com.towhid.swpc.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController


@Composable
fun HomeScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Device ID:")
    }
}