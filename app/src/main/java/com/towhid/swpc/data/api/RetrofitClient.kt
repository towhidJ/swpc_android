package com.towhid.swpc.data.api

import com.towhid.swpc.util.DataStoreManager
import com.towhid.swpc.viewmodel.AuthViewModel
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthInterceptor(private val dataStore: DataStoreManager, private val viewModel: AuthViewModel) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { dataStore.getAccessToken() }
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        val response = chain.proceed(newRequest)

        if (response.code == 401) { // Unauthorized
            viewModel.refreshToken()
        }

        return response
    }
}


object RetrofitClient {
    private const val BASE_URL = "http://192.168.0.100:5006/" // Replace with your base URL

    fun createApi(dataStore: DataStoreManager, viewModel: AuthViewModel): AuthApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(dataStore, viewModel))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(AuthApi::class.java)
    }
}

