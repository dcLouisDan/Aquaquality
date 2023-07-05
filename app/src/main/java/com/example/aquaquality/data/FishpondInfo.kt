package com.example.aquaquality.data

data class FishpondInfo(
    val id: Int,
    val name: String,
    val tempValue: Float = 0.0F,
    val phValue: Float = 0.0F,
    val turbidityValue: Int = 0
)
