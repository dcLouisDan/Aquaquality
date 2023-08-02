package com.example.aquaquality.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.aquaquality.data.SettingsInfo
import com.example.aquaquality.data.SettingsUiState
import com.example.aquaquality.presentation.sign_in.UserData
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

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    private val auth = Firebase.auth
    private val database =
        Firebase.database("https://aquaquality-fe2e7-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val settingsRef: DatabaseReference
    private val userId: String

    init {
        Log.i("Settings", "Initialized")
        userId = getSignedInUser()?.userId!!
        settingsRef = database.getReference("$userId/settings")
        settingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == null) {
                    settingsRef.setValue(SettingsInfo())
                } else {
                    val info = snapshot.getValue<SettingsInfo>()
                    _uiState.update { currentState ->
                        currentState.copy(
                            minTemp = info?.minTemp.toString(),
                            maxTemp = info?.maxTemp.toString(),
                            minPh = info?.minPh.toString(),
                            maxPh = info?.maxPh.toString(),
                            minTurb = info?.minTurb.toString(),
                            maxTurb = info?.maxTurb.toString()
                        )
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Settings", "Settings database error")
            }
        })
    }

    fun resetErrorMessage() {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = null,
                isSaveSuccess = false
            )
        }
    }
    fun resetSaveSuccessState() {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = null,
                isSaveSuccess = false
            )
        }
    }

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

    fun updateSettings() {
        if (
            uiState.value.minTemp == "" &&
            uiState.value.maxTemp == "" &&
            uiState.value.minPh == "" &&
            uiState.value.maxPh == "" &&
            uiState.value.minTurb == "" &&
            uiState.value.maxTurb == ""
        ) {
            _uiState.update { currentState ->
                currentState.copy(
                    errorMessage = "Invalid inputs."
                )
            }
            return
        }

        val newSettings = SettingsInfo(
            uiState.value.minTemp.toFloat(),
            uiState.value.maxTemp.toFloat(),
            uiState.value.minPh.toFloat(),
            uiState.value.maxPh.toFloat(),
            uiState.value.minTurb.toInt(),
            uiState.value.maxTurb.toInt(),
        )

        settingsRef.setValue(newSettings).addOnCompleteListener { task ->
            _uiState.update {
                if (task.isSuccessful) {
                    it.copy(
                        isSaveSuccess = task.isSuccessful
                    )
                } else {
                    it.copy(
                        errorMessage = "Something went wrong"
                    )
                }
            }
        }
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