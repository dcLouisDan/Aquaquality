package com.example.aquaquality.data


data class SettingsInfo(
    val minTemp: Float? = 26f,
    val maxTemp: Float? = 32f,
    val minPh: Float? = 6.5f,
    val maxPh: Float? = 8.5f,
    val minTurb: Int? = 0,
    val maxTurb: Int? = 180,
)

fun checkParameterStatus(
    settingsInfo: SettingsInfo,
    fishpondInfo: FishpondInfo,
    onLowTemp: (() -> Unit)? = null,
    onHighTemp: (() -> Unit)? = null,
    onLowPh: (() -> Unit)? = null,
    onHighPh: (() -> Unit)? = null,
    onLowTurb: (() -> Unit)? = null,
    onHighTurb: (() -> Unit)? = null,
) {
    if (fishpondInfo.tempValue!! < settingsInfo.minTemp!!) {
        if (onLowTemp != null) {
            onLowTemp()
        }
    }
    if (fishpondInfo.tempValue > settingsInfo.maxTemp!!) {
        if (onHighTemp != null) {
            onHighTemp()
        }
    }
    if (fishpondInfo.phValue!! < settingsInfo.minPh!!){
        if (onLowPh != null) {
            onLowPh()
        }
    }
    if (fishpondInfo.phValue > settingsInfo.maxPh!!){
        if (onHighPh != null) {
            onHighPh()
        }
    }
    if (fishpondInfo.turbidityValue!! < settingsInfo.minTurb!!){
        if (onLowTurb != null) {
            onLowTurb()
        }
    }
    if (fishpondInfo.turbidityValue > settingsInfo.maxTurb!!){
        if (onHighTurb != null) {
            onHighTurb()
        }
    }
}