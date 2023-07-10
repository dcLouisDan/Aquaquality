package com.example.aquaquality.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.aquaquality.data.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun setMinTempInput(minTemp: String) {
        _uiState.update { currentState ->
            currentState.copy(
                minTemp = minTemp
            )
        }
    }

    fun setMaxTempInput(maxTemp: String) {
        _uiState.update { currentState ->
            currentState.copy(
                maxTemp = maxTemp
            )
        }
    }

    fun setMinPhInput(minPh: String) {
        _uiState.update { currentState ->
            currentState.copy(
                minPh = minPh
            )
        }
    }

    fun setMaxPhInput(maxPh: String) {
        _uiState.update { currentState ->
            currentState.copy(
                maxPh = maxPh
            )
        }
    }

    fun setMinTurbInput(minTurb: String) {
        _uiState.update { currentState ->
            currentState.copy(
                minTurb = minTurb
            )
        }
    }

    fun setMaxTurbInput(maxTurb: String) {
        _uiState.update { currentState ->
            currentState.copy(
                maxTurb = maxTurb
            )
        }
    }
}