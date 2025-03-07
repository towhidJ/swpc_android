package com.towhid.swpc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.towhid.swpc.data.api.AuthApi
import com.towhid.swpc.data.api.AuthInterceptor
import com.towhid.swpc.data.api.RetrofitClient
import com.towhid.swpc.data.repository.AuthRepository
import com.towhid.swpc.ui.login.LoginScreen

import com.towhid.swpc.ui.theme.SwpcTheme
import com.towhid.swpc.util.DataStoreManager
import com.towhid.swpc.viewmodel.AuthViewModel
import com.towhid.swpc.viewmodel.AuthViewModelFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {


    private val dataStore: DataStoreManager by lazy { DataStoreManager(this) }
    private val api: AuthApi by lazy { RetrofitClient.createApi(dataStore, authViewModel) } // Initialize with ViewModel
    private val repository: AuthRepository by lazy { AuthRepository(api, dataStore) }
    private val authViewModel: AuthViewModel by viewModels { AuthViewModelFactory(repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        setContent {
            SwpcTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding->
                    LoginScreen(paddingValues =innerPadding , viewModel = authViewModel)
                }
            }
        }
    }
}



