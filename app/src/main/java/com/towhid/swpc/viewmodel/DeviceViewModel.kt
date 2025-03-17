package com.towhid.swpc.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.towhid.swpc.data.api.RetrofitClient
import com.towhid.swpc.data.models.DeviceData
import com.towhid.swpc.data.models.DeviceResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeviceViewModel : ViewModel() {
    var devices = mutableStateOf<List<DeviceData>?>(null)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    var isLoading = mutableStateOf(false)
        private set

    fun fetchDevices(userId: Int) {
        viewModelScope.launch {
            isLoading.value = true // Set loading state to true
            val apiService = RetrofitClient.apiService
            val call = apiService.getDevice(userId)

            call.enqueue(object : Callback<DeviceResponse> {
                override fun onResponse(
                    call: Call<DeviceResponse>,
                    response: Response<DeviceResponse>
                ) {
                    isLoading.value = false // Set loading state to false
                    if (response.isSuccessful) {
                        val deviceResponse = response.body()
                        if (deviceResponse?.success == true && deviceResponse.data != null) {
                            devices.value = deviceResponse.data
                            errorMessage.value = null
                        } else {
                            errorMessage.value = deviceResponse?.message ?: "API error"
                            devices.value = null
                        }
                    } else {
                        errorMessage.value = "HTTP error: ${response.code()}"
                        devices.value = null
                    }
                }

                override fun onFailure(call: Call<DeviceResponse>, t: Throwable) {
                    isLoading.value = false // Set loading state to false
                    errorMessage.value = "Network error: ${t.message}"
                    devices.value = null
                }
            })
        }
    }
}