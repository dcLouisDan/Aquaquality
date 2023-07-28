package com.example.aquaquality.utilities

import java.util.Calendar

object DateHelper {
    // Initializing a Calendar
    private val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    val mYear = mCalendar.get(Calendar.YEAR)
    val mMonth = mCalendar.get(Calendar.MONTH)
    val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    val months = listOf(
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
    val dateToday = "${months[mMonth]} $mDay, $mYear"
}