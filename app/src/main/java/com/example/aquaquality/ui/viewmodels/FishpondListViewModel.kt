package com.example.aquaquality.ui.viewmodels


import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.aquaquality.data.DeviceInfo
import com.example.aquaquality.data.FishpondInfo
import com.example.aquaquality.data.FishpondListUiState
import com.example.aquaquality.data.SettingsInfo
import com.example.aquaquality.data.checkParameterStatus
import com.example.aquaquality.presentation.sign_in.UserData
import com.example.aquaquality.ui.components.IndicatorStatus
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class FishpondListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FishpondListUiState())
    val uiState: StateFlow<FishpondListUiState> = _uiState.asStateFlow()
    private val database =
        Firebase.database("https://aquaquality-fe2e7-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private lateinit var settingsRef: DatabaseReference
    private lateinit var fishpondsReference: DatabaseReference


    private val auth = Firebase.auth


    init {
//        fetchData()
        fetchDataByChild()
    }

    fun refreshData() {
//        fetchData()
        fetchDataByChild()
    }

//    private fun fetchData() {
//        val userId = getSignedInUser()?.userId
//        fishpondsReference = database.getReference("$userId/fishponds")
//        settingsRef = database.getReference("$userId/settings")
//
//        initializeSettings { settingsInfo ->
//            fishpondsReference.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    _uiState.update {
//                        it.copy(
//                            fishpondList = emptyList(),
//                            fishpondKeyList = emptyList()
//                        )
//                    }
//                    val fishpondList = mutableListOf<FishpondInfo>()
//                    for (info in snapshot.children) {
//                        var fishpondInfo = info.getValue<FishpondInfo>()!!
//                        fishpondList.add(fishpondInfo)
//                        val fishpondIndex = fishpondList.indexOf(fishpondInfo)
//
//                        if (fishpondInfo.connectedDeviceId != null) {
//                            checkParameterStatus(
//                                settingsInfo = settingsInfo,
//                                fishpondInfo = fishpondInfo,
//                                onLowTemp = {
//                                    fishpondInfo =
//                                        fishpondInfo.copy(tempStatus = IndicatorStatus.UNDER_RANGE.name)
//                                    fishpondList[fishpondIndex] = fishpondInfo
//
//                                    if (!uiState.value.sentAlerts.contains(1)) {
//                                        toggleLowTempAlert(true)
//                                        _uiState.update {
//                                            it.copy(
//                                                sentAlerts = uiState.value.sentAlerts.plus(1)
//                                            )
//                                        }
//                                    }
//                                },
//                                onHighTemp = {
//                                    fishpondInfo =
//                                        fishpondInfo.copy(tempStatus = IndicatorStatus.OVER_RANGE.name)
//                                    fishpondList[fishpondIndex] = fishpondInfo
//
//                                    if (!uiState.value.sentAlerts.contains(2)) {
//                                        toggleHighTempAlert(true)
//                                        _uiState.update {
//                                            it.copy(
//                                                sentAlerts = uiState.value.sentAlerts.plus(2)
//                                            )
//                                        }
//                                    }
//                                },
//                                onSafeTemp = {
//                                    if (uiState.value.sentAlerts.contains(1)) {
//                                        toggleLowTempAlert(false)
//                                        _uiState.update {
//                                            it.copy(
//                                                sentAlerts = uiState.value.sentAlerts.minus(1)
//                                            )
//                                        }
//                                    }
//                                    if (uiState.value.sentAlerts.contains(2)) {
//                                        toggleHighTempAlert(false)
//                                        _uiState.update {
//                                            it.copy(
//                                                sentAlerts = uiState.value.sentAlerts.minus(2)
//                                            )
//                                        }
//                                    }
//                                },
//                                onLowPh = {
//                                    fishpondInfo =
//                                        fishpondInfo.copy(phStatus = IndicatorStatus.UNDER_RANGE.name)
//                                    fishpondList[fishpondIndex] = fishpondInfo
//
//                                    if (!uiState.value.sentAlerts.contains(3)) {
//                                        toggleLowPhAlert(true)
//                                        _uiState.update {
//                                            it.copy(
//                                                sentAlerts = uiState.value.sentAlerts.plus(3)
//                                            )
//                                        }
//                                    }
//                                },
//                                onHighPh = {
//                                    fishpondInfo =
//                                        fishpondInfo.copy(phStatus = IndicatorStatus.OVER_RANGE.name)
//                                    fishpondList[fishpondIndex] = fishpondInfo
//
//                                    if (!uiState.value.sentAlerts.contains(4)) {
//                                        toggleHighPhAlert(true)
//                                        _uiState.update {
//                                            it.copy(
//                                                sentAlerts = uiState.value.sentAlerts.plus(4)
//                                            )
//                                        }
//                                    }
//                                },
//                                onSafePh = {
//                                    if (uiState.value.sentAlerts.contains(3)) {
//                                        toggleLowPhAlert(false)
//                                        _uiState.update {
//                                            it.copy(
//                                                sentAlerts = uiState.value.sentAlerts.minus(3)
//                                            )
//                                        }
//                                    }
//                                    if (uiState.value.sentAlerts.contains(4)) {
//                                        toggleHighPhAlert(false)
//                                        _uiState.update {
//                                            it.copy(
//                                                sentAlerts = uiState.value.sentAlerts.minus(4)
//                                            )
//                                        }
//                                    }
//                                },
//                                onLowTurb = {
//                                    fishpondInfo =
//                                        fishpondInfo.copy(turbStatus = IndicatorStatus.UNDER_RANGE.name)
//                                    fishpondList[fishpondIndex] = fishpondInfo
//
//                                    if (!uiState.value.sentAlerts.contains(5)) {
//                                        toggleLowTurbAlert(true)
//                                        _uiState.update {
//                                            it.copy(
//                                                sentAlerts = uiState.value.sentAlerts.plus(5)
//                                            )
//                                        }
//                                    }
//                                },
//                                onHighTurb = {
//                                    fishpondInfo =
//                                        fishpondInfo.copy(turbStatus = IndicatorStatus.OVER_RANGE.name)
//                                    fishpondList[fishpondIndex] = fishpondInfo
//                                    if (!uiState.value.sentAlerts.contains(6)) {
//                                        toggleHighTurbAlert(true)
//                                        _uiState.update {
//                                            it.copy(
//                                                sentAlerts = uiState.value.sentAlerts.plus(6)
//                                            )
//                                        }
//                                    }
//                                },
//                                onSafeTurb = {
//                                    if (uiState.value.sentAlerts.contains(5)) {
//                                        toggleLowTurbAlert(false)
//                                        _uiState.update {
//                                            it.copy(
//                                                sentAlerts = uiState.value.sentAlerts.minus(5)
//                                            )
//                                        }
//                                    }
//                                    if (uiState.value.sentAlerts.contains(6)) {
//                                        toggleHighTurbAlert(false)
//                                        _uiState.update {
//                                            it.copy(
//                                                sentAlerts = uiState.value.sentAlerts.minus(6)
//                                            )
//                                        }
//                                    }
//                                }
//                            )
//                        }
//                        if (fishpondInfo.connectedDeviceId != null) {
//                            database.getReference("devices/${fishpondInfo.connectedDeviceId}")
//                                .get().addOnCompleteListener { task ->
//                                    if (task.isSuccessful) {
//                                        val deviceInfo = task.result.getValue<DeviceInfo>()!!
//                                        val epochSeconds = System.currentTimeMillis() / 1000
//                                        val isOffline = (epochSeconds - deviceInfo.timestamp!!) > 5
//                                        fishpondInfo =
//                                            fishpondInfo.copy(isOffline = isOffline)
//                                        fishpondList[fishpondIndex] = fishpondInfo
//                                        _uiState.update {
//                                            it.copy(
//                                                fishpondList = fishpondList,
//                                                fishpondKeyList = uiState.value.fishpondKeyList.plus(
//                                                    info.key!!
//                                                )
//                                            )
//                                        }
//
//                                        Log.i(
//                                            "Firebase",
//                                            "AVE value: ${uiState.value.fishpondList}"
//                                        )
//                                        Log.i(
//                                            "Firebase",
//                                            "Key List: ${uiState.value.fishpondKeyList}"
//                                        )
//                                    }
//                                }
//                        } else {
//                            _uiState.update {
//                                it.copy(
//                                    fishpondList = fishpondList,
//                                    fishpondKeyList = uiState.value.fishpondKeyList.plus(info.key!!)
//                                )
//                            }
//                            Log.i("Firebase", "AVE value: ${uiState.value.fishpondList}")
//                            Log.i("Firebase", "Key List: ${uiState.value.fishpondKeyList}")
//                        }
//
//                    }
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.e("Firebase", "$error")
//                }
//
//            })
//        }
//    }

    private fun fetchDataByChild() {
        val userId = getSignedInUser()?.userId
        fishpondsReference = database.getReference("$userId/fishponds")
        settingsRef = database.getReference("$userId/settings")

        initializeSettings { settingsInfo ->
            fishpondsReference.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val fishpondData = snapshot.getValue<FishpondInfo>()!!

                    val fishpondInfo = checkFishpondInfo(fishpondData, settingsInfo)

                    _uiState.update {
                        it.copy(
                            fishpondMap = uiState.value.fishpondMap.plus(
                                Pair(
                                    fishpondInfo.id!!,
                                    fishpondInfo
                                )
                            )
                        )
                    }

                    Log.i("Fishpond Map", "child added: ${uiState.value.fishpondMap}")

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val fishpondData = snapshot.getValue<FishpondInfo>()!!
                    val fishpondInfo = checkFishpondInfo(fishpondData, settingsInfo)

                    checkFishpondInfoForAlerts(fishpondData, settingsInfo)

                    _uiState.update {
                        it.copy(
                            fishpondMap = uiState.value.fishpondMap.plus(
                                Pair(
                                    fishpondInfo.id!!,
                                    fishpondInfo
                                )
                            )
                        )
                    }
                    Log.i("Fishpond Map", "child changed: ${uiState.value.fishpondMap}")

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val fishpondData = snapshot.getValue<FishpondInfo>()!!

                    val fishpondInfo = checkFishpondInfo(fishpondData, settingsInfo)
                    _uiState.update {
                        it.copy(
                            fishpondMap = uiState.value.fishpondMap.minus(fishpondInfo.id!!)
                        )
                    }
                    Log.i("Fishpond Map", "child changed: ${uiState.value.fishpondMap}")

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Fishpond Map", "Something went wrong")

                }

            })
        }
    }

    private fun checkFishpondInfo(
        fishpondInfo: FishpondInfo,
        settingsInfo: SettingsInfo
    ): FishpondInfo {
        var fishpondInfo1 = fishpondInfo
        if (fishpondInfo1.connectedDeviceId != null) {
            checkParameterStatus(
                settingsInfo = settingsInfo,
                fishpondInfo = fishpondInfo1,
                onLowTemp = {
                    fishpondInfo1 =
                        fishpondInfo1.copy(tempStatus = IndicatorStatus.UNDER_RANGE.name)
                },
                onHighTemp = {
                    fishpondInfo1 =
                        fishpondInfo1.copy(tempStatus = IndicatorStatus.OVER_RANGE.name)
                },
                onLowPh = {
                    fishpondInfo1 =
                        fishpondInfo1.copy(phStatus = IndicatorStatus.UNDER_RANGE.name)
                },
                onHighPh = {
                    fishpondInfo1 =
                        fishpondInfo1.copy(phStatus = IndicatorStatus.OVER_RANGE.name)
                },
                onLowTurb = {
                    fishpondInfo1 =
                        fishpondInfo1.copy(turbStatus = IndicatorStatus.UNDER_RANGE.name)
                },
                onHighTurb = {
                    fishpondInfo1 =
                        fishpondInfo1.copy(turbStatus = IndicatorStatus.OVER_RANGE.name)
                },
            )
        }
        if (fishpondInfo.connectedDeviceId != null) {
            database.getReference("devices/${fishpondInfo1.connectedDeviceId}")
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val deviceInfo = task.result.getValue<DeviceInfo>()!!
                        val epochSeconds = System.currentTimeMillis() / 1000
                        val isOffline = (epochSeconds - deviceInfo.timestamp!!) > 5
                        fishpondInfo1 =
                            fishpondInfo1.copy(isOffline = isOffline)
                    }
                }
        }
        return fishpondInfo1
    }

    private fun checkFishpondInfoForAlerts(
        fishpondInfo: FishpondInfo,
        settingsInfo: SettingsInfo
    ) {

        val currentTime = System.currentTimeMillis()
        val intervalTime: Long = 60 * 1000
        if (fishpondInfo.connectedDeviceId != null) {
            checkParameterStatus(
                settingsInfo = settingsInfo,
                fishpondInfo = fishpondInfo,
                onLowTemp = {
                    val lastNotificationTime = uiState.value.notificationTimestamps[1] ?: 0
                    val timeElapsed = currentTime - lastNotificationTime

                    if (!uiState.value.sentAlerts.contains(1) && timeElapsed >= intervalTime) {
                        Log.i("Notification", "Showing alert: 1")
                        toggleLowTempAlert(true)
                        _uiState.update {
                            it.copy(
                                sentAlerts = uiState.value.sentAlerts.plus(1),
                                notificationTimestamps = uiState.value.notificationTimestamps.plus(
                                    Pair(1, currentTime)
                                )
                            )
                        }
                    } else {
                        Log.i("Notification", "Skipping notification: 1. Not enough time elapsed.")
                    }
                },
                onHighTemp = {
                    val lastNotificationTime = uiState.value.notificationTimestamps[2] ?: 0
                    val timeElapsed = currentTime - lastNotificationTime

                    if (!uiState.value.sentAlerts.contains(2) && timeElapsed >= intervalTime) {
                        Log.i("Notification", "Showing alert: 2")

                        toggleHighTempAlert(true)
                        _uiState.update {
                            it.copy(
                                sentAlerts = uiState.value.sentAlerts.plus(2),
                                notificationTimestamps = uiState.value.notificationTimestamps.plus(Pair(2, currentTime))
                            )
                        }
                    } else {
                        Log.i("Notification", "Skipping notification: 2. Not enough time elapsed.")
                    }
                },
                onSafeTemp = {
                    if (uiState.value.sentAlerts.contains(1)) {
                        toggleLowTempAlert(false)
                        _uiState.update {
                            it.copy(
                                sentAlerts = uiState.value.sentAlerts.minus(1)
                            )
                        }
                    }
                    if (uiState.value.sentAlerts.contains(2)) {
                        toggleHighTempAlert(false)
                        _uiState.update {
                            it.copy(
                                sentAlerts = uiState.value.sentAlerts.minus(2)
                            )
                        }
                    }
                },
                onLowPh = {
                    val lastNotificationTime = uiState.value.notificationTimestamps[3] ?: 0
                    val timeElapsed = currentTime - lastNotificationTime

                    if (!uiState.value.sentAlerts.contains(3) && timeElapsed >= intervalTime) {
                        Log.i("Notification", "Showing alert: 3")

                        toggleLowPhAlert(true)
                        _uiState.update {
                            it.copy(
                                sentAlerts = uiState.value.sentAlerts.plus(3),
                                notificationTimestamps = uiState.value.notificationTimestamps.plus(Pair(3, currentTime))
                            )
                        }
                    } else {
                        Log.i("Notification", "Skipping notification: 3. Not enough time elapsed.")
                    }
                },
                onHighPh = {
                    val lastNotificationTime = uiState.value.notificationTimestamps[4] ?: 0
                    val timeElapsed = currentTime - lastNotificationTime

                    if (!uiState.value.sentAlerts.contains(4) && timeElapsed >= intervalTime) {
                        Log.i("Notification", "Showing alert: 4")

                        toggleHighPhAlert(true)
                        _uiState.update {
                            it.copy(
                                sentAlerts = uiState.value.sentAlerts.plus(4),
                                notificationTimestamps = uiState.value.notificationTimestamps.plus(Pair(4, currentTime))
                            )
                        }
                    } else {
                        Log.i("Notification", "Skipping notification: 4. Not enough time elapsed.")
                    }
                },
                onSafePh = {
                    if (uiState.value.sentAlerts.contains(3)) {
                        toggleLowPhAlert(false)
                        _uiState.update {
                            it.copy(
                                sentAlerts = uiState.value.sentAlerts.minus(3)
                            )
                        }
                    }
                    if (uiState.value.sentAlerts.contains(4)) {
                        toggleHighPhAlert(false)
                        _uiState.update {
                            it.copy(
                                sentAlerts = uiState.value.sentAlerts.minus(4)
                            )
                        }
                    }
                },
                onLowTurb = {
                    val lastNotificationTime = uiState.value.notificationTimestamps[5] ?: 0
                    val timeElapsed = currentTime - lastNotificationTime

                    if (!uiState.value.sentAlerts.contains(5) && timeElapsed >= intervalTime) {
                        Log.i("Notification", "Showing alert: 5")

                        toggleLowTurbAlert(true)
                        _uiState.update {
                            it.copy(
                                sentAlerts = uiState.value.sentAlerts.plus(5),
                                notificationTimestamps = uiState.value.notificationTimestamps.plus(Pair(5, currentTime))
                            )
                        }
                    } else {
                        Log.i("Notification", "Skipping notification: 5. Not enough time elapsed.")
                    }
                },
                onHighTurb = {
                    val lastNotificationTime = uiState.value.notificationTimestamps[6] ?: 0
                    val timeElapsed = currentTime - lastNotificationTime

                    if (!uiState.value.sentAlerts.contains(6) && timeElapsed >= intervalTime) {
                        toggleHighTurbAlert(true)
                        _uiState.update {
                            it.copy(
                                sentAlerts = uiState.value.sentAlerts.plus(6),
                                notificationTimestamps = uiState.value.notificationTimestamps.plus(Pair(6, currentTime))
                            )
                        }
                    } else {
                        Log.i("Notification", "Skipping notification: 6. Not enough time elapsed.")
                    }
                },
                onSafeTurb = {
                    if (uiState.value.sentAlerts.contains(5)) {
                        toggleLowTurbAlert(false)
                        _uiState.update {
                            it.copy(
                                sentAlerts = uiState.value.sentAlerts.minus(5)
                            )
                        }
                    }
                    if (uiState.value.sentAlerts.contains(6)) {
                        toggleHighTurbAlert(false)
                        _uiState.update {
                            it.copy(
                                sentAlerts = uiState.value.sentAlerts.minus(6)
                            )
                        }
                    }
                }
            )
        }
    }

    fun toggleLowTempAlert(isVisible: Boolean) {
        _uiState.update {
            it.copy(
                isLowTempAlertVisible = isVisible
            )
        }
    }

    fun toggleHighTempAlert(isVisible: Boolean) {
        _uiState.update {
            it.copy(
                isHighTempAlertVisible = isVisible
            )
        }
    }

    fun toggleLowPhAlert(isVisible: Boolean) {
        _uiState.update {
            it.copy(
                isLowPhAlertVisible = isVisible
            )
        }
    }

    fun toggleHighPhAlert(isVisible: Boolean) {
        _uiState.update {
            it.copy(
                isHighPhAlertVisible = isVisible
            )
        }
    }

    fun toggleLowTurbAlert(isVisible: Boolean) {
        _uiState.update {
            it.copy(
                isLowTurbAlertVisible = isVisible
            )
        }
    }

    fun toggleHighTurbAlert(isVisible: Boolean) {
        _uiState.update {
            it.copy(
                isHighTurbAlertVisible = isVisible
            )
        }
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
                Log.i("Settings", "Updated Settings: $settings")
                callback(settings)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Settings", "Something went wrong")
            }
        })
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
                currentSelectedFishpondInfo = null,
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

    fun setEditFishpondNameInput(name: String?) {
        _uiState.update {
            name?.let { it1 ->
                it.copy(
                    editFishpondName = it1
                )
            }!!
        }
    }

    fun setFishpondInfoToModify(fishpond: FishpondInfo) {
        _uiState.update {
            it.copy(
                fishpondInfoToModify = fishpond
            )
        }
    }

    private fun resetFishpondInfoToModify() {
        _uiState.update {
            it.copy(
                fishpondInfoToModify = null
            )
        }
    }

    fun addNewFishpond(name: String) {
        val newChildRef = fishpondsReference.push()
        val fishpondInfo = FishpondInfo(id = newChildRef.key, name = name)

        newChildRef.setValue(fishpondInfo).addOnCompleteListener { task ->
            _uiState.update {
                it.copy(
                    isAdditionSuccess = task.isSuccessful,
                    newFishpondName = ""
                )
            }
        }
    }

    fun updateFishpondName(name: String, fishpondInfo: FishpondInfo) {
        val dataRef = fishpondsReference.child(fishpondInfo.id!!)

        dataRef.child("name").setValue(name).addOnCompleteListener {
            if (it.isSuccessful) {
                _uiState.update { currentState ->
                    currentState.copy(
                        editFishpondName = ""
                    )
                }
            }
        }
        resetFishpondInfoToModify()
    }

    fun deleteFishpondInfo(fishpondInfo: FishpondInfo) {
        val dataRef = fishpondsReference.child(fishpondInfo.id!!)

        dataRef.removeValue()
        resetFishpondInfoToModify()
        disconnectDeviceFromFishpond(fishpondInfo)
    }

    private fun disconnectDeviceFromFishpond(fishpond: FishpondInfo) {
        val deviceId = fishpond.connectedDeviceId
        val fishpondReference = database.getReference("devices").child("$deviceId")

        fishpondReference.child("fishpondId").setValue(null)
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