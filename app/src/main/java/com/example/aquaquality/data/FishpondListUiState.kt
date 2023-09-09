package com.example.aquaquality.data

data class FishpondListUiState(
    val fishpondList: List<FishpondInfo> = emptyList(),
    val fishpondMap: Map<String, FishpondInfo> = emptyMap(),
    val fishpondKeyList: List<String> = emptyList(),
    val currentSelectedFishpondInfo: FishpondInfo? = null,
    val isShowingHomepage: Boolean = true,
    val isAdditionSuccess: Boolean = false,
    val fishpondInfoToModify: FishpondInfo? = null,
    val newFishpondName: String = "",
    val editFishpondName: String = "",
    val sentAlerts: Map<String,Set<Int>> = emptyMap(),
    val isLowTempAlertVisible: Boolean = false,
    val isHighTempAlertVisible: Boolean = false,
    val isLowPhAlertVisible: Boolean = false,
    val isHighPhAlertVisible: Boolean = false,
    val isLowTurbAlertVisible: Boolean = false,
    val isHighTurbAlertVisible: Boolean = false,
    val notificationTimestamps: Map<Int, Long> = emptyMap()
)
