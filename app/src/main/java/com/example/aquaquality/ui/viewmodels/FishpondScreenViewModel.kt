package com.example.aquaquality.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquaquality.data.FishpondInfo
import com.example.aquaquality.data.FishpondScreenUiState
import com.example.aquaquality.data.local.LocalFishpondsDataProvider
import com.example.aquaquality.utilities.DateHelper
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class FishpondScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FishpondScreenUiState())
    val uiState: StateFlow<FishpondScreenUiState> = _uiState.asStateFlow()

    internal var tempChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer()
    internal var phChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer()
    internal var turbidityChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer()


    init {
        Log.i("ChartValues", "Initialized")
        val historyLogs = LocalFishpondsDataProvider.historyList

        val month = historyLogs[0].month
        val day = historyLogs[0].day
        val year = historyLogs[0].year

        _uiState.update { currentState ->
            currentState.copy(
                year = year,
                month = month,
                day = day
            )
        }

        for (log in historyLogs) {
            _uiState.update { currentState ->
                currentState.copy(
                    timeList = uiState.value.timeList.plus("${log.hour}:${log.minute}"),
                    tempValueList = uiState.value.tempValueList.plus(log.tempValue),
                    phValueList = uiState.value.phValueList.plus(log.phValue),
                    turbidityValueList = uiState.value.turbidityValueList.plus(log.turbidityValue),
                )
            }
        }



        setChartValues()

        Log.i("ChartValues", "Chart timelist: ${uiState.value.timeList}")
        Log.i("ChartValues", "Chart phList: ${uiState.value.phValueList}")
        Log.i("ChartValues", "Chart turblist: ${uiState.value.turbidityValueList}")
    }

    fun setChartValues() {
        createTempFloatEntryList(valueList = uiState.value.tempValueList)
        createPhFloatEntryList(valueList = uiState.value.phValueList)
        createTurbFloatEntryList(valueList = uiState.value.turbidityValueList)

        viewModelScope.launch {
            tempChartEntryModelProducer = ChartEntryModelProducer(uiState.value.tempEntryList)
            phChartEntryModelProducer = ChartEntryModelProducer(uiState.value.phEntryList)
            turbidityChartEntryModelProducer = ChartEntryModelProducer(uiState.value.turbidityEntryList)
        }
    }

    fun setSelectedDate(year: Int, month: Int, day: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                year = year,
                month = month,
                day = day
            )
        }
    }

    fun setFishpondInfo(fishpondKey: String, fishpondInfo: FishpondInfo) {
        _uiState.update { currentState ->
            currentState.copy(
                fishpondKey = fishpondKey,
                fishpondInfo = fishpondInfo
            )
        }
    }

    fun createTempFloatEntryList(valueList: List<Number>){
        for ((index, value) in valueList.withIndex()) {
            _uiState.update { currentState ->
                currentState.copy(
                    tempEntryList = uiState.value.tempEntryList.plus(FloatEntry(x = index.toFloat(), y = value.toFloat()))
                )
            }
        }
        Log.i("ChartValues", "Chart EntryList: ${uiState.value.tempEntryList}")
    }
    fun createPhFloatEntryList(valueList: List<Number>){
        for ((index, value) in valueList.withIndex()) {
            _uiState.update { currentState ->
                currentState.copy(
                    phEntryList = uiState.value.phEntryList.plus(FloatEntry(x = index.toFloat(), y = value.toFloat()))
                )
            }
        }
        Log.i("ChartValues", "Chart EntryList: ${uiState.value.tempEntryList}")
    }
    fun createTurbFloatEntryList(valueList: List<Number>){
        for ((index, value) in valueList.withIndex()) {
            _uiState.update { currentState ->
                currentState.copy(
                    turbidityEntryList = uiState.value.turbidityEntryList.plus(FloatEntry(x = index.toFloat(), y = value.toFloat()))
                )
            }
        }
        Log.i("ChartValues", "Chart EntryList: ${uiState.value.tempEntryList}")
    }
}