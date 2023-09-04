package com.example.aquaquality.data

data class FishpondListUiState(
    val fishpondList: List<FishpondInfo> = emptyList(),
    val fishpondKeyList: List<String> = emptyList(),
    val currentSelectedFishpondInfo: FishpondInfo = FishpondInfo(""),
    val isShowingHomepage: Boolean = true,
    val isAdditionSuccess: Boolean = false,
    val fishpondInfoToModify: FishpondInfo? = null,
    val newFishpondName: String = "",
    val editFishpondName: String = "",
    val sentAlerts: Set<Int> = emptySet(),
    val isLowTempAlertVisible: Boolean = false,
    val isHighTempAlertVisible: Boolean = false,
    val isLowPhAlertVisible: Boolean = false,
    val isHighPhAlertVisible: Boolean = false,
    val isLowTurbAlertVisible: Boolean = false,
    val isHighTurbAlertVisible: Boolean = false,
)
