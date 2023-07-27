package com.example.aquaquality.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.aquaquality.data.FishpondInfo
import com.example.aquaquality.data.FishpondScreenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FishpondScreenViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(FishpondScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun setFishpondInfo(fishpondKey: String ,fishpondInfo: FishpondInfo) {
        _uiState.update { currentState ->
            currentState.copy(
                fishpondKey = fishpondKey,
                fishpondInfo = fishpondInfo
            )
        }
    }
}