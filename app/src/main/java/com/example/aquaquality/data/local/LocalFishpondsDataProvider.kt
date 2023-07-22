package com.example.aquaquality.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.aquaquality.data.FishpondInfo
import com.example.aquaquality.data.HistoryLog
import java.time.LocalDate
import java.time.LocalTime
import kotlin.random.Random

object LocalFishpondsDataProvider {



    @RequiresApi(Build.VERSION_CODES.O)
    fun createHistoryLogList(): List<HistoryLog> {
        val list = mutableListOf<HistoryLog>()

        for (i in 0..23) {
            val time = LocalTime.of(i, 0)
            val tempValue = 20f + (Random.nextFloat()*10)
            val phValue = 4f + (Random.nextFloat()*10)
            val turbidityValue = 40 + Random.nextInt(10)

            list.add(HistoryLog(
                date = LocalDate.of(2023, 7, 21),
                time = time,
                tempValue = tempValue,
                phValue = phValue,
                turbidityValue = turbidityValue
            ))
        }

        return list
    }
}

