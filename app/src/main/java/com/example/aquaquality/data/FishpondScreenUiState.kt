package com.example.aquaquality.data


import java.util.Calendar



// Initializing a Calendar
val mCalendar = Calendar.getInstance()

// Fetching current year, month and day
val mYear = mCalendar.get(Calendar.YEAR)
val mMonth = mCalendar.get(Calendar.MONTH)
val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

private val months = listOf(
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
)

// Declaring a string value to
// store date in string format
val mDate = "${months[mMonth]} $mDay, $mYear"

data class FishpondScreenUiState(
    val fishpondKey: String? = "",
    val fishpondInfo: FishpondInfo? = null,
    val selectedDate: String? = mDate,
    val timeList: List<String> = emptyList(),
    val tempValueList: List<Float> = emptyList(),
    val phValueList: List<Float> = emptyList(),
    val turbValueList: List<Int> = emptyList(),
    val deviceList: List<DeviceInfo> = emptyList(),
    val deviceKeyList: List<String> = emptyList()
)