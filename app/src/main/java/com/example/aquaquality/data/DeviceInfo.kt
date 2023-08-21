package com.example.aquaquality.data

import com.google.firebase.database.PropertyName

data class DeviceInfo(
    val name: String? = "",
    @PropertyName("available") val isAvailable: Boolean? = false,
    val fishpondId: String? = null,
    val timestamp: Long? = null,
)
