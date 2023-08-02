package com.example.aquaquality.data

data class SettingsInfo(
    val minTemp: Float? = 26f,
    val maxTemp: Float? = 32f,
    val minPh: Float? = 6.5f,
    val maxPh: Float? = 8.5f,
    val minTurb: Int? = 0,
    val maxTurb: Int? = 180,
)
