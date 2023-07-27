package com.example.aquaquality.data

data class DeviceInfo(
    val id: String? = "",
    val name: String? = "",
    val isAvailable: Boolean? = false,
    val isTaken: Boolean? = false,
    val tempValue: Float? = 0.0F,
    val phValue: Float? = 0.0F,
    val turbidityValue: Int? = 0
)
