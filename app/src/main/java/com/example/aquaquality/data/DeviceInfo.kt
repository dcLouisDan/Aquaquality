package com.example.aquaquality.data

import com.google.firebase.database.PropertyName

data class DeviceInfo(
    val name: String? = "",
    @PropertyName("available") val isAvailable: Boolean? = false,
    val fishpondId: String? = null,
    val tempValue: Float? = 0.0F,
    val phValue: Float? = 0.0F,
    val turbidityValue: Int? = 0
)
