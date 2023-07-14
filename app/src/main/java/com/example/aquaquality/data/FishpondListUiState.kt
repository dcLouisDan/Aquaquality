package com.example.aquaquality.data

import com.example.aquaquality.data.local.LocalFishpondsDataProvider

data class FishpondListUiState(
    val fishpondList: List<FishpondInfo> = emptyList(),
    val currentSelectedFishpondInfo: FishpondInfo = LocalFishpondsDataProvider.defaultFishpond,
    val isShowingHomepage: Boolean = true
)
