package com.towhid.swpc.data.models

data class DeviceResponse(
    val `data`: List<DeviceData>,
    val message: String,
    val success: Boolean
)