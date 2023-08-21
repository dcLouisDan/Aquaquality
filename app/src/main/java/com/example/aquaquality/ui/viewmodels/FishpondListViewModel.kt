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
        fetchData()
    }

    fun refreshData() {
        fetchData()
    }
    private fun fetchData() {
        val userId = getSignedInUser()?.userId
        fishpondsReference = database.getReference("$userId/fishponds")
        settingsRef = database.getReference("$userId/settings")

        initializeSettings { settingsInfo ->
            fishpondsReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _uiState.update {
                        it.copy(
                            fishpondList = emptyList(),
                            fishpondKeyList = emptyList()
                        )
                    }
                    for (info in snapshot.children) {
                        _uiState.update {
                            it.copy(
                                fishpondList = uiState.value.fishpondList.plus(info.getValue<FishpondInfo>()!!),
                                fishpondKeyList = uiState.value.fishpondKeyList.plus(info.key!!)
                            )
                        }

                        var fishpondInfo = uiState.value.fishpondList.last()
                        val fishpondIndex = uiState.value.fishpondList.lastIndex
                        val fishpondList = uiState.value.fishpondList.toMutableList()

                        checkParameterStatus(
                            settingsInfo = settingsInfo,
                            fishpondInfo = fishpondInfo,
                            onLowTemp = {
                                fishpondInfo =
                                    fishpondInfo.copy(tempStatus = IndicatorStatus.UNDER_RANGE.name)
                                fishpondList[fishpondIndex] = fishpondInfo
                            },
                            onHighTemp = {
                                fishpondInfo =
                                    fishpondInfo.copy(tempStatus = IndicatorStatus.OVER_RANGE.name)
                                fishpondList[fishpondIndex] = fishpondInfo
                            },
                            onLowPh = {
                                fishpondInfo =
                                    fishpondInfo.copy(phStatus = IndicatorStatus.UNDER_RANGE.name)
                                fishpondList[fishpondIndex] = fishpondInfo
                            },
                            onHighPh = {
                                fishpondInfo =
                                    fishpondInfo.copy(phStatus = IndicatorStatus.OVER_RANGE.name)
                                fishpondList[fishpondIndex] = fishpondInfo
                            },
                            onLowTurb = {
                                fishpondInfo =
                                    fishpondInfo.copy(turbStatus = IndicatorStatus.UNDER_RANGE.name)
                                fishpondList[fishpondIndex] = fishpondInfo
                            },
                            onHighTurb = {
                                fishpondInfo =
                                    fishpondInfo.copy(turbStatus = IndicatorStatus.OVER_RANGE.name)
                                fishpondList[fishpondIndex] = fishpondInfo
                            }
                        )
                        if (fishpondInfo.connectedDeviceId != null) {
                            database.getReference("devices/${fishpondInfo.connectedDeviceId}")
                                .get().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val deviceInfo = task.result.getValue<DeviceInfo>()!!
                                        val epochSeconds = System.currentTimeMillis() / 1000
                                        val isOffline = (epochSeconds - deviceInfo.timestamp!!) > 5
                                        fishpondInfo =
                                            fishpondInfo.copy(isOffline = isOffline)
                                        fishpondList[fishpondIndex] = fishpondInfo
                                        _uiState.update {
                                            it.copy(
                                                fishpondList = fishpondList
                                            )
                                        }

                                        Log.i(
                                            "Firebase",
                                            "AVE value: ${uiState.value.fishpondList}"
                                        )
                                        Log.i(
                                            "Firebase",
                                            "Key List: ${uiState.value.fishpondKeyList}"
                                        )
                                    }
                                }
                        } else {
                            _uiState.update {
                                it.copy(
                                    fishpondList = fishpondList
                                )
                            }
                            Log.i("Firebase", "AVE value: ${uiState.value.fishpondList}")
                            Log.i("Firebase", "Key List: ${uiState.value.fishpondKeyList}")
                        }

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "$error")
                }

            })
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
                currentSelectedFishpondInfo = it.fishpondList[0],
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
        val fishpondInfo = FishpondInfo(name = name)

        newChildRef.setValue(fishpondInfo).addOnCompleteListener { task ->
            _uiState.update {
                it.copy(
                    isAdditionSuccess = task.isSuccessful
                )
            }
        }
    }

    fun updateFishpondName(name: String, fishpondInfo: FishpondInfo) {
        val fishpondInfoIndex = uiState.value.fishpondList.indexOf(fishpondInfo)
        val fishpondKey: String = uiState.value.fishpondKeyList[fishpondInfoIndex]
        val newInfo = fishpondInfo.copy(name = name)
        val dataRef = fishpondsReference.child(fishpondKey)

        dataRef.setValue(newInfo)
        resetFishpondInfoToModify()
    }

    fun deleteFishpondInfo(fishpondInfo: FishpondInfo) {
        val fishpondInfoIndex = uiState.value.fishpondList.indexOf(fishpondInfo)
        val fishpondKey: String = uiState.value.fishpondKeyList[fishpondInfoIndex]
        val dataRef = fishpondsReference.child(fishpondKey)

        dataRef.removeValue()
        resetFishpondInfoToModify()
        disconnectDeviceFromFishpond(fishpondInfo)
    }

    private fun disconnectDeviceFromFishpond(fishpond: FishpondInfo) {
        val deviceId = fishpond.connectedDeviceId
        val fishpondReference = database.getReference("devices").child("$deviceId")

        fishpondReference.child("fishpondId").setValue(null)
    }

    fun getFishpondKey(fishpond: FishpondInfo): String {
        val fishpondInfoIndex = uiState.value.fishpondList.indexOf(fishpond)
        return uiState.value.fishpondKeyList[fishpondInfoIndex]
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