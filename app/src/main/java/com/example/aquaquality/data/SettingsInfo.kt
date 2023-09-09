package com.example.aquaquality.data

import android.util.Log


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
    onSafeTemp: (() -> Unit)? = null,
    onLowPh: (() -> Unit)? = null,
    onHighPh: (() -> Unit)? = null,
    onSafePh: (() -> Unit)? = null,
    onLowTurb: (() -> Unit)? = null,
    onHighTurb: (() -> Unit)? = null,
    onSafeTurb: (() -> Unit)? = null,
) {
    if (fishpondInfo.tempValue!! < settingsInfo.minTemp!!) {
        if (onLowTemp != null) {
            onLowTemp()
            Log.i("Parameter Status", "Low Temp Achieved")
        }
    } else if (fishpondInfo.tempValue > settingsInfo.maxTemp!!) {
        if (onHighTemp != null) {
            onHighTemp()
            Log.i("Parameter Status", "High Temp Achieved")

        }
    } else {
        if (onSafeTemp != null){
            onSafeTemp()
            Log.i("Parameter Status", "Safe Temp Achieved")
        }
    }


    if (fishpondInfo.phValue!! < settingsInfo.minPh!!){
        if (onLowPh != null) {
            onLowPh()
        }
    } else if (fishpondInfo.phValue > settingsInfo.maxPh!!){
        if (onHighPh != null) {
            onHighPh()
        }
    } else {
        if (onSafePh != null){
            onSafePh()
        }
    }


    if (fishpondInfo.turbidityValue!! < settingsInfo.minTurb!!){
        if (onLowTurb != null) {
            onLowTurb()
        }
    } else if (fishpondInfo.turbidityValue > settingsInfo.maxTurb!!){
        if (onHighTurb != null) {
            onHighTurb()
        }
    } else {
        if (onSafeTurb != null){
            onSafeTurb()
        }
    }
}