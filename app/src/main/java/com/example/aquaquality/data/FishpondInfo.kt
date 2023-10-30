package com.example.aquaquality.data

import com.example.aquaquality.ui.components.IndicatorStatus
import com.google.firebase.database.PropertyName

data class FishpondInfo(
    @PropertyName("id") val id: String? = "",
    @PropertyName("name") val name: String? = "",
    @PropertyName("desc") val desc: String? = "",
    @PropertyName("connectedDeviceId") val connectedDeviceId: String? = null,
    @PropertyName("tempValue") val tempValue: Float? = 0.0F,
    @PropertyName("tempStatus") val tempStatus: String? = IndicatorStatus.NORMAL.name,
    @PropertyName("phValue") val phValue: Float? = 0.0F,
    @PropertyName("phStatus") val phStatus: String? = IndicatorStatus.NORMAL.name,
    @PropertyName("turbidityValue") val turbidityValue: Int? = 0,
    @PropertyName("turbStatus") val turbStatus: String? = IndicatorStatus.NORMAL.name,
    @PropertyName("offline")val isOffline: Boolean? = true
    )

data class HistoryLog(
    val year: Int? = null,
    val month: Int? = null,
    val day: Int? = null,
    val hour: Int? = null,
    val minute: Int? = null,
    val tempValue: Float? = 0.0F,
    val phValue: Float? = 0.0F,
    val turbidityValue: Int? = 0
)
