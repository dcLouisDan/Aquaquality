package com.example.aquaquality.data.local

import com.example.aquaquality.data.FishpondInfo

object LocalFishpondsDataProvider {
    val defaultFishpond = FishpondInfo(-1, "")

    val fishpondInfoList: List<FishpondInfo> = listOf(
        FishpondInfo(
            id = 1,
            name = "Fishpond 1",
            tempValue = 30F,
            phValue = 6.7F,
            turbidityValue = 160
        ),
        FishpondInfo(
            id = 2,
            name = "Fishpond 2",
            tempValue = 27.5F,
            phValue = 7.0F,
            turbidityValue = 154
        )
    )

}