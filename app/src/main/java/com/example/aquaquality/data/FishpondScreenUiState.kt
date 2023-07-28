package com.example.aquaquality.data


import com.example.aquaquality.utilities.DateHelper
import com.patrykandpatrick.vico.core.entry.FloatEntry


data class FishpondScreenUiState(
    val fishpondKey: String? = "",
    val fishpondInfo: FishpondInfo? = null,
    val year: Int = DateHelper.mYear,
    val month: Int = DateHelper.mMonth,
    val day: Int = DateHelper.mDay,
    val timeList: List<String> = emptyList(),
    val tempValueList: List<Float> = emptyList(),
    val tempEntryList: List<FloatEntry> = emptyList(),
    val phValueList: List<Float> = emptyList(),
    val phEntryList: List<FloatEntry> = emptyList(),
    val turbidityValueList: List<Int> = emptyList(),
    val turbidityEntryList: List<FloatEntry> = emptyList(),
    val deviceList: List<DeviceInfo> = emptyList(),
    val deviceKeyList: List<String> = emptyList()
)