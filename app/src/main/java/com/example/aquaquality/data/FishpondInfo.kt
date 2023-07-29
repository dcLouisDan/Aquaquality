package com.example.aquaquality.data

import com.example.aquaquality.ui.components.IndicatorStatus

data class FishpondInfo(
    val name: String? = "",
    val connectedDeviceId: String? = null,
    val tempValue: Float? = 0.0F,
    val tempStatus: String? = IndicatorStatus.NORMAL.name,
    val phValue: Float? = 0.0F,
    val phStatus: String? = IndicatorStatus.NORMAL.name,
    val turbidityValue: Int? = 0,
    val turbStatus: String? = IndicatorStatus.NORMAL.name,
    )

data class HistoryLog(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val tempValue: Float = 0.0F,
    val phValue: Float = 0.0F,
    val turbidityValue: Int = 0
)
