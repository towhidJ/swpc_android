package com.towhid.swpc.ui.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.towhid.swpc.data.models.DeviceData
import com.towhid.swpc.routes.Routes
import com.towhid.swpc.viewmodel.DeviceViewModel
import com.towhid.swpc.viewmodel.LoginViewModel

@Composable
fun DeviceListScreen(token: String, userId: Int, navController: NavHostController) {
    val viewModel: DeviceViewModel = viewModel()
    val authViewModel: LoginViewModel = viewModel()
    val context = LocalContext.current
    // Fetch devices when the screen is launched or userId changes
    LaunchedEffect(userId) {
        viewModel.fetchDevices(token, userId)
    }

    val devices = viewModel.devices.value
    val errorMessage = viewModel.errorMessage.value

    Column(modifier = Modifier.fillMaxSize()) {
        // Logout button at the top-right corner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    authViewModel.logout(
                        token = token,
                        onSuccess = {
                            // Navigate to the login screen after successful logout
                            navController.navigate(Routes.MainRoute.Login.route) {
                                popUpTo(Routes.MainRoute.DeviceList.route) {
                                    inclusive = true
                                }
                            }
                        },
                        onError = { errorMessage ->
                            // Show an error message (e.g., using a Snackbar or Toast)
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            ) {
                Text("Logout")
            }
        }

        // Display error message, loading state, or device list
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) // Add padding for better spacing
    ) {
        Text(text = "Device ID: ${device.device_name}", style = MaterialTheme.typography.body1)
        Text(
            text = "Device Name: ${device.user_device_name ?: "N/A"}",
            style = MaterialTheme.typography.body1
        )
        Text(text = "User ID: ${device.user_id}", style = MaterialTheme.typography.body1)
        Text(
            text = "Caretaker ID: ${device.caretaker_id ?: "N/A"}",
            style = MaterialTheme.typography.body1
        )
    }
}