package com.example.aquaquality.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.aquaquality.data.HistoryLog
import java.time.LocalTime
import kotlin.random.Random

object LocalFishpondsDataProvider {
    @RequiresApi(Build.VERSION_CODES.O)
    fun createHistoryLogList(): List<HistoryLog> {
        val list = mutableListOf<HistoryLog>()

        for (i in 0..23) {
            val time = LocalTime.of(i, 0)
            val tempValue = 20f + (Random.nextFloat() * 10)
            val phValue = 4f + (Random.nextFloat() * 10)
            val turbidityValue = 40 + Random.nextInt(10)

            list.add(
                HistoryLog(
//                date = LocalDate.of(2023, 7, 21),
//                time = time.to,
                    year = 2023,
                    month = 7,
                    day = 21,
                    hour = time.hour,
                    minute = time.minute,
                    tempValue = tempValue,
                    phValue = phValue,
                    turbidityValue = turbidityValue
                )
            )
        }

        return list
    }

    val historyList = listOf(HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 0,
        minute = 0,
        tempValue = 23.49477f,
        phValue = 7.3927627f,
        turbidityValue = 45
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 1,
        minute = 0,
        tempValue = 23.170918f,
        phValue = 13.985258f,
        turbidityValue = 47
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 2,
        minute = 0,
        tempValue = 26.900743f,
        phValue = 9.805602f,
        turbidityValue = 49
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 3,
        minute = 0,
        tempValue = 20.985f,
        phValue = 4.209289f,
        turbidityValue = 47
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 4,
        minute = 0,
        tempValue = 27.968754f,
        phValue = 12.57627f,
        turbidityValue = 42
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 5,
        minute = 0,
        tempValue = 21.571478f,
        phValue = 8.3622f,
        turbidityValue = 46
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 6,
        minute = 0,
        tempValue = 20.705511f,
        phValue = 6.9101477f,
        turbidityValue = 48
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 7,
        minute = 0,
        tempValue = 27.727032f,
        phValue = 7.8570776f,
        turbidityValue = 48
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 8,
        minute = 0,
        tempValue = 29.41501f,
        phValue = 4.3237815f,
        turbidityValue = 43
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 9,
        minute = 0,
        tempValue = 25.4541f,
        phValue = 11.224173f,
        turbidityValue = 47
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 10,
        minute = 0,
        tempValue = 29.247162f,
        phValue = 8.393436f,
        turbidityValue = 47
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 11,
        minute = 0,
        tempValue = 21.914824f,
        phValue = 11.58907f,
        turbidityValue = 48
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 12,
        minute = 0,
        tempValue = 29.448002f,
        phValue = 4.624611f,
        turbidityValue = 42
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 13,
        minute = 0,
        tempValue = 26.443644f,
        phValue = 10.422734f,
        turbidityValue = 46
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 14,
        minute = 0,
        tempValue = 28.547832f,
        phValue = 9.514466f,
        turbidityValue = 47
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 15,
        minute = 0,
        tempValue = 24.29486f,
        phValue = 11.748126f,
        turbidityValue = 47
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 16,
        minute = 0,
        tempValue = 27.641718f,
        phValue = 5.013259f,
        turbidityValue = 44
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 17,
        minute = 0,
        tempValue = 23.95653f,
        phValue = 6.947404f,
        turbidityValue = 41
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 18,
        minute = 0,
        tempValue = 28.253124f,
        phValue = 5.505015f,
        turbidityValue = 49
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 19,
        minute = 0,
        tempValue = 23.780186f,
        phValue = 5.658951f,
        turbidityValue = 47
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 20,
        minute = 0,
        tempValue = 21.659752f,
        phValue = 11.5386915f,
        turbidityValue = 49
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 21,
        minute = 0,
        tempValue = 25.381222f,
        phValue = 5.639745f,
        turbidityValue = 48
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 22,
        minute = 0,
        tempValue = 20.606745f,
        phValue = 4.225734f,
        turbidityValue = 41
    ), HistoryLog(
        year = 2023,
        month = 7,
        day = 21,
        hour = 23,
        minute = 0,
        tempValue = 29.789742f,
        phValue = 5.358577f,
        turbidityValue = 44
    ))
}
