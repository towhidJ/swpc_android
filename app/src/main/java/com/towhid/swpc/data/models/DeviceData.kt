package com.towhid.swpc.data.models

data class DeviceData(
    val caretaker_id: Int,
    val device_name: String,
    val flow_rate: Int,
    val height: Int,
    val id: Int,
    val length: Int,
    val motor_hp: Double,
    val motor_type: String,
    val phase: String,
    val sourch_sensor_type: String,
    val tank_height: Int,
    val tank_overflow_height: Int,
    val tank_sensor_mode: String,
    val user_device_name: String,
    val user_id: Int,
    val voltage: Int,
    val volume: Int,
    val water_source: String,
    val width: Int
)