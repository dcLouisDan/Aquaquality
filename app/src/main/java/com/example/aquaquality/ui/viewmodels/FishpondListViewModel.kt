package com.example.aquaquality.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.aquaquality.data.FishpondInfo
import com.example.aquaquality.data.FishpondListUiState
import com.example.aquaquality.data.local.LocalFishpondsDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@RequiresApi(Build.VERSION_CODES.O)
class FishpondListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FishpondListUiState())
    val uiState: StateFlow<FishpondListUiState> = _uiState.asStateFlow()

    init {
        initializeUiState()
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    fun setNewFishpondNameInput(name: String) {
        _uiState.update {
            it.copy(
                newFishpondName = name
            )
        }
    }

    fun setEditFishpondNameInput(name: String) {
        _uiState.update {
            it.copy(
                editFishpondName = name
            )
        }
    }

    fun setFishpondInfoToModify(fishpond: FishpondInfo) {
        _uiState.update {
            it.copy(
                fishpondInfoToModify = fishpond
            )
        }
    }
    fun resetFishpondInfoToModify() {
        _uiState.update {
            it.copy(
                fishpondInfoToModify = null
            )
        }
    }
}