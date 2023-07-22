package com.example.aquaquality.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.aquaquality.data.FishpondInfo
import com.example.aquaquality.data.FishpondListUiState
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

@RequiresApi(Build.VERSION_CODES.O)
class FishpondListViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(FishpondListUiState())
    val uiState: StateFlow<FishpondListUiState> = _uiState.asStateFlow()
    private val database = Firebase.database
    private var fishpondsReference: DatabaseReference

    private var list = mutableStateOf(emptyList<FishpondInfo>())

    init {
        database.useEmulator("10.0.2.2", 9000)
        fishpondsReference = database.getReference("fishponds")

        fishpondsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (info in snapshot.children) {
                    _uiState.value = FishpondListUiState(
                        fishpondList = uiState.value.fishpondList.plus(info.getValue<FishpondInfo>()!!)
                    )
                    Log.i("Firebase", "AVE value: ${info.getValue<FishpondInfo>()}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        initializeUiState(list.value)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeUiState(result: List<FishpondInfo>) {

    }

    fun updateUiState(fishpondList: List<FishpondInfo>) {
        _uiState.value =
            FishpondListUiState(
                fishpondList = fishpondList,
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
                currentSelectedFishpondInfo = it.fishpondList.get(0),
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
}