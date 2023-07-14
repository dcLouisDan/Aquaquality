package com.example.aquaquality.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.aquaquality.data.FishpondInfo
import com.example.aquaquality.data.FishpondListUiState
import com.example.aquaquality.data.local.LocalFishpondsDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FishpondListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FishpondListUiState())
    val uiState: StateFlow<FishpondListUiState> = _uiState.asStateFlow()

    init {
        initializeUiState()
    }

    private fun initializeUiState() {
        val fishpondList = LocalFishpondsDataProvider.fishpondInfoList
        _uiState.value =
            FishpondListUiState(
                fishpondList = fishpondList,
                currentSelectedFishpondInfo = fishpondList.get(0) ?: LocalFishpondsDataProvider.defaultFishpond
            )
    }

    fun updateDetailsScreenStates(fishpond: FishpondInfo) {
        _uiState.update {
            it.copy(
                currentSelectedFishpondInfo = fishpond,
                isShowingHomepage = false
            )
        }
    }

    fun resetHomeScreenStates() {
        _uiState.update {
            it.copy(
                currentSelectedFishpondInfo = it.fishpondList.get(0) ?: LocalFishpondsDataProvider.defaultFishpond,
                isShowingHomepage = true
            )
        }
    }
}