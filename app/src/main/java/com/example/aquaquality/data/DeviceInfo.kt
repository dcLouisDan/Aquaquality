package com.example.aquaquality.data

data class DeviceInfo(
    val id: Int,
    val name: String,
    val isAvailable: Boolean,
    val tempValue: Float = 0.0F,
    val phValue: Float = 0.0F,
    val turbidityValue: Int = 0
)
