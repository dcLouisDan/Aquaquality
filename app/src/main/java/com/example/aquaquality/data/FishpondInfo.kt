package com.example.aquaquality.data

import com.example.aquaquality.ui.components.IndicatorStatus
import java.time.LocalDate
import java.time.LocalTime

data class FishpondInfo(
    val name: String? = "",
    val connectedDeviceId: Int? = null,
    val tempValue: Float? = 0.0F,
    val tempStatus: String? = IndicatorStatus.NORMAL.name,
    val phValue: Float? = 0.0F,
    val phStatus: String? = IndicatorStatus.NORMAL.name,
    val turbidityValue: Int? = 0,
    val turbStatus: String? = IndicatorStatus.NORMAL.name,
    )

data class HistoryLog(
    val date: LocalDate,
    val time: LocalTime,
    val tempValue: Float = 0.0F,
    val phValue: Float = 0.0F,
    val turbidityValue: Int = 0
)
