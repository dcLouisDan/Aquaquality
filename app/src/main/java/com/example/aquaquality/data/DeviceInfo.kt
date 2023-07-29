package com.example.aquaquality.data

import com.google.firebase.database.PropertyName

data class DeviceInfo(
    val name: String? = "",
    @PropertyName("available") val isAvailable: Boolean? = false,
    @PropertyName("taken") val isTaken: Boolean? = false,
    val tempValue: Float? = 0.0F,
    val phValue: Float? = 0.0F,
    val turbidityValue: Int? = 0
)
