package com.towhid.swpc.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.towhid.swpc.data.models.DeviceData
import com.towhid.swpc.viewmodel.DeviceViewModel
import com.towhid.swpc.viewmodel.LoginViewModel

@Composable
fun DeviceListScreen(userId: Int,navController: NavHostController) {
    val viewModel: DeviceViewModel = viewModel()
    val authViewModel: LoginViewModel = viewModel()

    LaunchedEffect(userId) {
        viewModel.fetchDevices(userId)
    }

    val devices = viewModel.devices.value
    val errorMessage = viewModel.errorMessage.value

    Column(modifier = Modifier.fillMaxSize()) {
//        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
//            Button(onClick = {
//                authViewModel.logout()
//                navController.navigate("login") {
//                    popUpTo("deviceList/{userId}") { inclusive = true }
//                }
//            }) {
//                Text("Logout")
//            }
//        }
        if (errorMessage != null) {
            Text("Error: $errorMessage")
        } else if (devices == null) {
            Text("Loading...")
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(devices) { device ->
                    DeviceItem(device)
                }
            }
        }
    }
}

@Composable
fun DeviceItem(device: DeviceData) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Device ID: ${device.deviceId}")
        Text("Device Name: ${device.deviceName ?: "N/A"}")
        Text("User ID: ${device.userId}")
        Text("Caretaker ID: ${device.caretakerId ?: "N/A"}")
    }
}