package com.example.aquaquality.data

data class ThresholdSettings(
    val userid: Int,
    val minTemp: Float,
    val maxTemp: Float,
    val minPh: Float,
    val maxPh: Float,
    val minTurb: Int,
    val maxTurb: Int
)
