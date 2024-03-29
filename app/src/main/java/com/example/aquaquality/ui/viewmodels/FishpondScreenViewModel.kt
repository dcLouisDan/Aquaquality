package com.example.aquaquality.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquaquality.data.DeviceInfo
import com.example.aquaquality.data.FishpondInfo
import com.example.aquaquality.data.FishpondScreenUiState
import com.example.aquaquality.data.HistoryLog
import com.example.aquaquality.data.SettingsInfo
import com.example.aquaquality.data.checkParameterStatus
import com.example.aquaquality.data.checkSingleParameter
import com.example.aquaquality.presentation.sign_in.UserData
import com.example.aquaquality.ui.components.IndicatorStatus
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
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@RequiresApi(Build.VERSION_CODES.O)
class FishpondScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FishpondScreenUiState())
    val uiState: StateFlow<FishpondScreenUiState> = _uiState.asStateFlow()
    private val database =
        Firebase.database("https://aquaquality-fe2e7-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val auth = Firebase.auth
    private val userId = getSignedInUser()?.userId

    private lateinit var devicesReference: DatabaseReference
    private lateinit var currentDeviceReference: DatabaseReference
    private lateinit var fishpondReference: DatabaseReference
    private lateinit var historyReference: DatabaseReference
    private var settingsRef: DatabaseReference
    private lateinit var fishpondEventListener: ValueEventListener


    internal var tempChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer()
    internal var phChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer()
    internal var turbidityChartEntryModelProducer: ChartEntryModelProducer =
        ChartEntryModelProducer()


    init {
        Log.i("FishpondScreen", "Created")
        settingsRef = database.getReference("$userId/settings")

        if (uiState.value.fishpondInfo?.connectedDeviceId == null) {
            getDeviceList()
        }

        Log.i("ChartValues", "Chart time list: ${uiState.value.timeList}")
        Log.i("ChartValues", "Chart phList: ${uiState.value.phValueList}")
        Log.i("ChartValues", "Chart turbidity list: ${uiState.value.turbidityValueList}")

    }

    fun connectDeviceToFishpond(deviceInfo: DeviceInfo) {
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
                currentDeviceReference.child("fishpondId")
                    .setValue("$userId/fishponds/$fishpondKey")
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

    fun getDeviceList() {
        devicesReference = database.getReference("devices")

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
                        val deviceInfo = info.getValue<DeviceInfo>()!!
                        var connectedFishpondRef: DatabaseReference? = null
                        if (deviceInfo.fishpondId != null) {
                            connectedFishpondRef =
                                database.getReference(deviceInfo.fishpondId)
                        }
                        val epochSeconds = System.currentTimeMillis() / 1000
                        val isOffline = (epochSeconds - deviceInfo.timestamp!!) > 5

                        if (isOffline) {
                            devicesReference.child("/${deviceInfo.name}").child("available")
                                .setValue(false)
                            connectedFishpondRef?.child("offline")?.setValue(true)
                        } else {
                            devicesReference.child("/${deviceInfo.name}").child("available")
                                .setValue(true)

                            connectedFishpondRef?.child("offline")?.setValue(false)
                        }

                        _uiState.update { currentState ->
                            currentState.copy(
                                deviceList = uiState.value.deviceList.plus(info.getValue<DeviceInfo>()!!),
                                deviceKeyList = uiState.value.deviceKeyList.plus(info.key.toString())
                            )
                        }

                    }
                    Log.i("Device Info", "Device Info: ${uiState.value.deviceList}")
                    Log.i("Device Key List", "Device Info: ${uiState.value.deviceKeyList}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Device Info", "Device info retrieval failed")

            }

        })
    }

    private fun getDeviceKey(deviceInfo: DeviceInfo): String {
        var deviceInfoIndex = -1

        for ((i, device) in uiState.value.deviceList.withIndex()) {
            if (device.name.equals(deviceInfo.name)) {
                deviceInfoIndex = i
            }
        }
        return uiState.value.deviceKeyList[deviceInfoIndex]
    }

    private fun updateFishpondInfo(settingsInfo: SettingsInfo, fishpondKey: String) {
        fishpondReference = database.getReference("$userId/fishponds/$fishpondKey")

        fishpondEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _uiState.update {
                    it.copy(
                        fishpondInfo = snapshot.getValue<FishpondInfo>()!!
                    )
                }
                Log.i("Child", "${snapshot.key}: ${snapshot.value}")


                var fishpondInfo = uiState.value.fishpondInfo!!
                if (fishpondInfo.connectedDeviceId != null) {
                    checkParameterStatus(
                        settingsInfo = settingsInfo,
                        fishpondInfo = fishpondInfo,
                        onLowTemp = {
                            fishpondInfo =
                                fishpondInfo.copy(tempStatus = IndicatorStatus.UNDER_RANGE.name)
                        },
                        onHighTemp = {
                            fishpondInfo =
                                fishpondInfo.copy(tempStatus = IndicatorStatus.OVER_RANGE.name)
                        },
                        onLowPh = {
                            fishpondInfo =
                                fishpondInfo.copy(phStatus = IndicatorStatus.UNDER_RANGE.name)
                        },
                        onHighPh = {
                            fishpondInfo =
                                fishpondInfo.copy(phStatus = IndicatorStatus.OVER_RANGE.name)

                        },
                        onLowTurb = {
                            fishpondInfo =
                                fishpondInfo.copy(turbStatus = IndicatorStatus.UNDER_RANGE.name)

                        },
                        onHighTurb = {
                            fishpondInfo =
                                fishpondInfo.copy(turbStatus = IndicatorStatus.OVER_RANGE.name)
                        })

                    _uiState.update {
                        it.copy(
                            fishpondInfo = fishpondInfo
                        )
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Fishpond Info", "Fishpond info retrieval failed")
            }
        }
        fishpondReference.removeEventListener(fishpondEventListener)
        fishpondReference.addValueEventListener(fishpondEventListener)
    }

    private fun initializeSettings(callback: (SettingsInfo) -> Unit) {
        settingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val settings = if (snapshot.value != null) {
                    snapshot.getValue<SettingsInfo>()!!
                } else {
                    settingsRef.setValue(SettingsInfo())
                    SettingsInfo()
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        minTemp = settings.minTemp.toString(),
                        maxTemp = settings.maxTemp.toString(),
                        minPh = settings.minPh.toString(),
                        maxPh = settings.maxPh.toString(),
                        minTurb = settings.minTurb.toString(),
                        maxTurb = settings.maxTurb.toString()
                    )
                }
                Log.i("Settings", "Updated Settings: $settings")
                callback(settings)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Settings", "Something went wrong")
            }
        })
    }

    private fun generateDayTempAnomalyTimeReport() {
        _uiState.update { currentState ->
            currentState.copy(
                dayTempMaxAnomalyTimeList = emptyList()
            )
        }
        initializeSettings { settingsInfo ->
            val timeList = uiState.value.timeList
            val tempValueList = uiState.value.tempValueList

            tempValueList.forEach { value ->
                checkSingleParameter(
                    value = value,
                    max = settingsInfo.maxTemp!!,
                    min = settingsInfo.minTemp!!,
                    onMax = {
                        val index = tempValueList.indexOf(value)
                        if (index != -1) {
                            val time = timeList[index]
                            _uiState.update { currentState ->
                                currentState.copy(
                                    dayTempMaxAnomalyTimeList = uiState.value.dayTempMaxAnomalyTimeList.plus(
                                        Pair(time, value)
                                    )
                                )
                            }
                        }
                    },
                    onMin = {
                        val index = tempValueList.indexOf(value)
                        if (index != -1) {
                            val time = timeList[index]
                            _uiState.update { currentState ->
                                currentState.copy(
                                    dayTempMinAnomalyTimeList = uiState.value.dayTempMinAnomalyTimeList.plus(
                                        Pair(time, value)
                                    )
                                )
                            }
                        }
                    }
                )
            }
        }
    }

    private fun generateDayPhAnomalyTimeReport() {
        _uiState.update { currentState ->
            currentState.copy(
                dayPhMaxAnomalyTimeList = emptyList()
            )
        }
        initializeSettings { settingsInfo ->
            val timeList = uiState.value.timeList
            val phValueList = uiState.value.phValueList

            phValueList.forEach { value ->
                checkSingleParameter(
                    value = value,
                    max = settingsInfo.maxPh!!,
                    min = settingsInfo.minPh!!,
                    onMax = {
                        val index = phValueList.indexOf(value)
                        if (index != -1) {
                            val time = timeList[index]
                            _uiState.update { currentState ->
                                currentState.copy(
                                    dayPhMaxAnomalyTimeList = uiState.value.dayPhMaxAnomalyTimeList.plus(
                                        Pair(time, value)
                                    )
                                )
                            }
                        }
                    },
                    onMin = {
                        val index = phValueList.indexOf(value)
                        if (index != -1) {
                            val time = timeList[index]
                            _uiState.update { currentState ->
                                currentState.copy(
                                    dayPhMinAnomalyTimeList = uiState.value.dayPhMinAnomalyTimeList.plus(
                                        Pair(time, value)
                                    )
                                )
                            }
                        }
                    }
                )
            }
        }
    }

    private fun generateDayTurbAnomalyTimeReport() {
        _uiState.update { currentState ->
            currentState.copy(
                dayTurbMaxAnomalyTimeList = emptyList()
            )
        }
        initializeSettings { settingsInfo ->
            val timeList = uiState.value.timeList
            val turbValueList = uiState.value.turbidityValueList

            turbValueList.forEach { value ->
                checkSingleParameter(
                    value = value,
                    max = settingsInfo.maxTurb!!,
                    min = settingsInfo.minTurb!!,
                    onMax = {
                        val index = turbValueList.indexOf(value)
                        if (index != -1) {
                            val time = timeList[index]
                            _uiState.update { currentState ->
                                currentState.copy(
                                    dayTurbMaxAnomalyTimeList = uiState.value.dayTurbMaxAnomalyTimeList.plus(
                                        Pair(time, value)
                                    )
                                )
                            }
                        }
                    },
                    onMin = {
                        val index = turbValueList.indexOf(value)
                        if (index != -1) {
                            val time = timeList[index]
                            _uiState.update { currentState ->
                                currentState.copy(
                                    dayTurbMinAnomalyTimeList = uiState.value.dayTurbMinAnomalyTimeList.plus(
                                        Pair(time, value)
                                    )
                                )
                            }
                        }
                    }
                )
            }
        }
    }


    suspend fun fetchHistoryList(dateKey: String): List<HistoryLog> {
        val fishpondKey = uiState.value.fishpondKey
        val historyList: MutableList<HistoryLog> = mutableListOf()
        historyReference = database.getReference("history/$userId/fishponds/$fishpondKey/$dateKey")

        return suspendCoroutine { continuation ->
            historyReference.get().addOnSuccessListener {
                Log.i("History Log Data", "$dateKey: ${it.value}")
                Log.i(
                    "History Log Data",
                    "History Path: history/$userId/fishponds/$fishpondKey/$dateKey"
                )
                for (dataLog in it.children) {
                    val historyLog = dataLog.getValue<HistoryLog>()

                    if (historyLog != null) {
                        historyList.add(historyLog)
                    }
                }

                Log.i("History Log", "$dateKey: $historyList")
                continuation.resume(historyList)
            }.addOnFailureListener {
                Log.e("History Log", "$dateKey: Data retrieval failed")
                continuation.resume(emptyList())
            }
        }

    }

    private fun setChartValues() {
        viewModelScope.launch {

            _uiState.update { currentState ->
                currentState.copy(
                    timeList = emptyList(),
                    tempValueList = emptyList(),
                    phValueList = emptyList(),
                    turbidityValueList = emptyList(),
                )
            }

            val historyList = fetchHistoryList(uiState.value.dateKey)

            for (log in historyList) {
                Log.i("History Log List Item", "Item: $log")
                val timeString: String = if (log.hour == 0) {
                    "12 am"
                } else if (log.hour!! > 0 && log.hour < 12) {
                    "${log.hour} am"
                } else if (log.hour == 12) {
                    "${log.hour} nn"
                } else {
                    "${log.hour - 12} pm"
                }
                _uiState.update { currentState ->
                    currentState.copy(
                        timeList = uiState.value.timeList.plus(timeString),
                        tempValueList = uiState.value.tempValueList.plus(log.tempValue!!),
                        phValueList = uiState.value.phValueList.plus(log.phValue!!),
                        turbidityValueList = uiState.value.turbidityValueList.plus(log.turbidityValue!!),
                    )
                }
            }
            Log.i(
                "History Log UI State",
                "${uiState.value.dateKey}: ${uiState.value.tempValueList}"
            )


            createTempFloatEntryList(valueList = uiState.value.tempValueList)
            createPhFloatEntryList(valueList = uiState.value.phValueList)
            createTurbFloatEntryList(valueList = uiState.value.turbidityValueList)
            generateDayTempAnomalyTimeReport()
            generateDayPhAnomalyTimeReport()
            generateDayTurbAnomalyTimeReport()

            tempChartEntryModelProducer = ChartEntryModelProducer(uiState.value.tempEntryList)
            phChartEntryModelProducer = ChartEntryModelProducer(uiState.value.phEntryList)
            turbidityChartEntryModelProducer =
                ChartEntryModelProducer(uiState.value.turbidityEntryList)
        }
    }

    fun setSelectedDate(year: Int, month: Int, day: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                dateKey = "$year${month + 1}$day",
                year = year,
                month = month,
                day = day
            )
        }

        setChartValues()
    }

    fun setFishpondInfo(fishpondInfo: FishpondInfo, key: String) {
        _uiState.update { currentState ->
            currentState.copy(
                fishpondInfo = fishpondInfo,
                fishpondKey = key
            )
        }
        Log.i("Fishpond info", "Selected Fishpond: ${uiState.value.fishpondInfo}")
        setChartValues()

        if (fishpondInfo.id != null) {
            initializeSettings { settingsInfo ->
                updateFishpondInfo(settingsInfo, fishpondInfo.id)
            }
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
        _uiState.update { currentState ->
            currentState.copy(
                tempEntryList = emptyList()
            )
        }
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
        _uiState.update { currentState ->
            currentState.copy(
                phEntryList = emptyList()
            )
        }
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
        _uiState.update { currentState ->
            currentState.copy(
                turbidityEntryList = emptyList()
            )
        }
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

    fun resetState() {
        fishpondReference.removeEventListener(fishpondEventListener)
    }
}