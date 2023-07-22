package com.example.aquaquality.data

import java.time.LocalDate
import java.time.LocalTime

data class FishpondInfo(
    val name: String? = "",
    val connectedDeviceId: Int? = null,
    val tempValue: Float? = 0.0F,
    val phValue: Float? = 0.0F,
    val turbidityValue: Int? = 0,
)

data class HistoryLog(
    val date: LocalDate,
    val time: LocalTime,
    val tempValue: Float = 0.0F,
    val phValue: Float = 0.0F,
    val turbidityValue: Int = 0
)
