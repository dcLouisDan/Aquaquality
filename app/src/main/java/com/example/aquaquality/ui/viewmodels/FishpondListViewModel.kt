package com.example.aquaquality.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.aquaquality.data.FishpondInfo
import com.example.aquaquality.data.FishpondListUiState
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

@RequiresApi(Build.VERSION_CODES.O)
class FishpondListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FishpondListUiState())
    val uiState: StateFlow<FishpondListUiState> = _uiState.asStateFlow()
    private val database = Firebase.database
    private var fishpondsReference: DatabaseReference

    private val auth = Firebase.auth


    init {
        database.useEmulator("10.0.2.2", 9000)
        val userId = getSignedInUser()?.userId
        fishpondsReference = database.getReference("$userId/fishponds")

        fishpondsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _uiState.value = FishpondListUiState(
                    fishpondList = emptyList(),
                    fishpondKeyList = emptyList()
                )
                for (info in snapshot.children) {
                    _uiState.value = FishpondListUiState(
                        fishpondList = uiState.value.fishpondList.plus(info.getValue<FishpondInfo>()!!),
                        fishpondKeyList = uiState.value.fishpondKeyList.plus(info.key!!)
                    )
                }
                    Log.i("Firebase", "AVE value: ${uiState.value.fishpondList}")
                    Log.i("Firebase", "Key List: ${uiState.value.fishpondKeyList}")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
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

    fun resetFishpondInfoToModify() {
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
    }

    fun deleteFishpondInfo(fishpondInfo: FishpondInfo) {
        val fishpondInfoIndex = uiState.value.fishpondList.indexOf(fishpondInfo)
        val fishpondKey: String = uiState.value.fishpondKeyList[fishpondInfoIndex]
        val dataRef = fishpondsReference.child(fishpondKey)

        dataRef.removeValue()
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            email = email,
            profilePictureUrl = photoUrl.toString()
        )
    }
}