package com.example.aquaquality.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquaquality.data.DeviceInfo
import com.example.aquaquality.data.FishpondInfo
import com.example.aquaquality.data.FishpondScreenUiState
import com.example.aquaquality.data.local.LocalFishpondsDataProvider
import com.example.aquaquality.presentation.sign_in.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class FishpondScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FishpondScreenUiState())
    val uiState: StateFlow<FishpondScreenUiState> = _uiState.asStateFlow()
    private val database = Firebase.database
    private val auth = Firebase.auth

    private lateinit var devicesReference: DatabaseReference
    private lateinit var currentDeviceReference: DatabaseReference
    private lateinit var fishpondReference: DatabaseReference


    internal var tempChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer()
    internal var phChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer()
    internal var turbidityChartEntryModelProducer: ChartEntryModelProducer =
        ChartEntryModelProducer()


    init {
        database.useEmulator("10.0.2.2", 9000)
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
        if (uiState.value.fishpondInfo?.connectedDeviceId == null) {
            getDeviceList()
        }


        Log.i("ChartValues", "Chart time list: ${uiState.value.timeList}")
        Log.i("ChartValues", "Chart phList: ${uiState.value.phValueList}")
        Log.i("ChartValues", "Chart turbidity list: ${uiState.value.turbidityValueList}")

    }

    fun connectDeviceToFishpond(deviceInfo: DeviceInfo) {
        val userId = getSignedInUser()?.userId
        val deviceId = getDeviceKey(deviceInfo)
        val fishpondKey = uiState.value.fishpondKey

        fishpondReference = database.getReference("$userId/fishponds").child("$fishpondKey")

        fishpondReference.child("connectedDeviceId").setValue(deviceId).addOnCompleteListener {
            if (it.isSuccessful) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isConnectionSuccess = true
                    )
                }

                currentDeviceReference = database.getReference("devices/$deviceId")
                currentDeviceReference.child("fishpondId").setValue(fishpondKey)
            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        isConnectionSuccess = false
                    )
                }
            }
        }
    }

    fun resetConnectionMessages() {
        _uiState.update { currentState ->
            currentState.copy(
                isConnectionSuccess = false,
                isDisconnectionSuccess = false
            )
        }
    }

    fun disconnectDeviceFromFishpond(deviceInfo: DeviceInfo) {
        val userId = getSignedInUser()?.userId
        val deviceId = getDeviceKey(deviceInfo)
        val fishpondKey = uiState.value.fishpondKey
        fishpondReference = database.getReference("$userId/fishponds").child("$fishpondKey")

        fishpondReference.child("connectedDeviceId").removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isDisconnectionSuccess = true
                    )
                }
                currentDeviceReference = database.getReference("devices/$deviceId")
                currentDeviceReference.child("fishpondId").setValue(null)
            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        isDisconnectionSuccess = false
                    )
                }
            }
        }
    }

    private fun getDeviceList() {
        devicesReference = database.getReference("devices")

//        devicesReference.child("AQ001").setValue(DeviceInfo(
//            "AQ001",
//            true,
//            null,
//            28f,
//            7f,
//            80
//        ))
//        devicesReference.child("AQ002").setValue(
//            DeviceInfo(
//                "AQ002",
//                true,
//                null,
//                24f,
//                7.5f,
//                45
//            )
//        )
//        devicesReference.child("AQ003").setValue(DeviceInfo(
//            "AQ003",
//            false,
//            null,
//            32f,
//            6.6f,
//            64
//        ))

        devicesReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _uiState.update { currentState ->
                    currentState.copy(
                        deviceList = emptyList(),
                        deviceKeyList = emptyList()
                    )
                }

                if (snapshot.exists()) {
                    for (info in snapshot.children) {
                        _uiState.update { currentState ->
                            currentState.copy(
                                deviceList = uiState.value.deviceList.plus(info.getValue<DeviceInfo>()!!),
                                deviceKeyList = uiState.value.deviceKeyList.plus(info.key.toString())
                            )
                        }
                    }
                    Log.i("Device Info", "Device Info: ${uiState.value.deviceList}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Device Info", "Device info retrieval failed")

            }

        })
    }

    private fun getDeviceKey(deviceInfo: DeviceInfo): String {
        val deviceInfoIndex = uiState.value.deviceList.indexOf(deviceInfo)
        return uiState.value.deviceKeyList[deviceInfoIndex]
    }

    private fun setChartValues() {
        createTempFloatEntryList(valueList = uiState.value.tempValueList)
        createPhFloatEntryList(valueList = uiState.value.phValueList)
        createTurbFloatEntryList(valueList = uiState.value.turbidityValueList)

        viewModelScope.launch {
            tempChartEntryModelProducer = ChartEntryModelProducer(uiState.value.tempEntryList)
            phChartEntryModelProducer = ChartEntryModelProducer(uiState.value.phEntryList)
            turbidityChartEntryModelProducer =
                ChartEntryModelProducer(uiState.value.turbidityEntryList)
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
        if (fishpondInfo.connectedDeviceId != null) {
            currentDeviceReference =
                database.getReference("devices").child("${fishpondInfo.connectedDeviceId}")
            currentDeviceReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        _uiState.update { currentState ->
                            currentState.copy(
                                deviceInfo = snapshot.getValue<DeviceInfo>()!!
                            )
                        }
                        Log.i(
                            "DeviceStatus",
                            "${uiState.value.fishpondInfo?.name} is connected to ${uiState.value.deviceInfo}"
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Device Info", "Device info retrieval failed")
                }

            })
        }
    }

    private fun createTempFloatEntryList(valueList: List<Number>) {
        for ((index, value) in valueList.withIndex()) {
            _uiState.update { currentState ->
                currentState.copy(
                    tempEntryList = uiState.value.tempEntryList.plus(
                        FloatEntry(
                            x = index.toFloat(),
                            y = value.toFloat()
                        )
                    )
                )
            }
        }
        Log.i("ChartValues", "Chart EntryList: ${uiState.value.tempEntryList}")
    }

    private fun createPhFloatEntryList(valueList: List<Number>) {
        for ((index, value) in valueList.withIndex()) {
            _uiState.update { currentState ->
                currentState.copy(
                    phEntryList = uiState.value.phEntryList.plus(
                        FloatEntry(
                            x = index.toFloat(),
                            y = value.toFloat()
                        )
                    )
                )
            }
        }
        Log.i("ChartValues", "Chart EntryList: ${uiState.value.tempEntryList}")
    }

    private fun createTurbFloatEntryList(valueList: List<Number>) {
        for ((index, value) in valueList.withIndex()) {
            _uiState.update { currentState ->
                currentState.copy(
                    turbidityEntryList = uiState.value.turbidityEntryList.plus(
                        FloatEntry(
                            x = index.toFloat(),
                            y = value.toFloat()
                        )
                    )
                )
            }
        }
        Log.i("ChartValues", "Chart EntryList: ${uiState.value.tempEntryList}")
    }

    private fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            email = email,
            profilePictureUrl = photoUrl.toString()
        )
    }
}