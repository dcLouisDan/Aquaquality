package com.example.aquaquality.data

data class FishpondUiState(
    val id: Int = -1,
    val name: String = "",
    val connectedDeviceId: Int? = null,
    val tempValue: Float = 0.0F,
    val phValue: Float = 0.0F,
    val turbidityValue: Int = 0,
    val historyLog: List<HistoryLog> = emptyList()
)
