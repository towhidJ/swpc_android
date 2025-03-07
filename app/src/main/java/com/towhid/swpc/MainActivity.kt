package com.towhid.swpc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel

import com.towhid.swpc.ui.login.LoginScreen
import com.towhid.swpc.ui.login.ProtectedDataScreen

import com.towhid.swpc.ui.theme.SwpcTheme
import com.towhid.swpc.util.TokenManager

import com.towhid.swpc.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwpcTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: AuthViewModel = viewModel()
                    var startDestination by remember { mutableStateOf("main") } // or "login"
                    LaunchedEffect(Unit) {
                        lifecycleScope.launch {
                            val accessToken = TokenManager.getAccessToken(this@MainActivity)
                            if (accessToken != null) {
                                try {
                                    viewModel.getProtectedData() // Try fetching data
                                } catch (e: Exception) {
                                    // Handle errors (e.g., token expired, network issues)
                                    viewModel.refreshAccessToken();
                                }
                            } else {
                                startDestination = "login"
                            }
                        }
                    }
                    MainScreen(startDestination) // Pass start destination to MainScreen
                }
            }
        }
    }
}

@Composable
fun MainScreen(startDestination : String, viewModel: AuthViewModel = viewModel()) {
    val loginResult by viewModel.loginResult.collectAsState()

    if (startDestination == "login" || loginResult == null) {
        LoginScreen(viewModel)
    } else {
        ProtectedDataScreen(viewModel)
    }
}