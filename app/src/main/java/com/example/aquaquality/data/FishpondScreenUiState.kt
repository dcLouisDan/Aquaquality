package com.example.aquaquality.data


import com.example.aquaquality.utilities.DateHelper
import com.patrykandpatrick.vico.core.entry.FloatEntry


data class FishpondScreenUiState(
    val fishpondKey: String? = "",
    val fishpondInfo: FishpondInfo? = FishpondInfo(),
    val deviceInfo: DeviceInfo = DeviceInfo(""),
    val dateKey: String = "${DateHelper.mYear}${DateHelper.mMonth+1}${DateHelper.mDay}",
    val year: Int = DateHelper.mYear,
    val month: Int = DateHelper.mMonth,
    val day: Int = DateHelper.mDay,
    val timeList: List<String> = emptyList(),
    val dayTempMaxAnomalyTimeList: List<Pair<String, Number>> = emptyList(),
    val dayTempMinAnomalyTimeList: List<Pair<String, Number>> = emptyList(),
    val dayPhMaxAnomalyTimeList: List<Pair<String, Number>> = emptyList(),
    val dayPhMinAnomalyTimeList: List<Pair<String, Number>> = emptyList(),
    val dayTurbMaxAnomalyTimeList: List<Pair<String, Number>> = emptyList(),
    val dayTurbMinAnomalyTimeList: List<Pair<String, Number>> = emptyList(),
    val tempValueList: List<Float> = emptyList(),
    val tempEntryList: List<FloatEntry> = emptyList(),
    val phValueList: List<Float> = emptyList(),
    val phEntryList: List<FloatEntry> = emptyList(),
    val turbidityValueList: List<Int> = emptyList(),
    val turbidityEntryList: List<FloatEntry> = emptyList(),
    val deviceList: List<DeviceInfo> = emptyList(),
    val deviceKeyList: List<String> = emptyList(),
    val isConnectionSuccess: Boolean = false,
    val isDisconnectionSuccess: Boolean = false,
    val minTemp: String = "",
    val maxTemp: String = "",
    val minPh: String = "",
    val maxPh: String = "",
    val minTurb: String = "",
    val maxTurb: String = "",
)
enum class ConnectingStatus {
    AVAILABLE, LOADING, CONNECTED
}

