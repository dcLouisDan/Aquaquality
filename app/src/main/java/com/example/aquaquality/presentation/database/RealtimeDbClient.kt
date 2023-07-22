package com.example.aquaquality.presentation.database

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aquaquality.data.FishpondInfo
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.database.ktx.values
import com.google.firebase.ktx.Firebase


class RealtimeDbClient(
    private val emulatorAddress: String? = null,
    private val emulatorPort: Int? = null
) {
    val databaseConnect = Firebase.database("https://aquaquality-fe2e7-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val database: DatabaseReference

    init {
//        if (emulatorAddress != null && emulatorPort != null) {
//            databaseConnect.useEmulator(emulatorAddress, emulatorPort)
//        }

        database = databaseConnect.reference

    }

    fun addNewFishPond(name: String): Boolean {
        val newChildRef = database.child("fishponds").push()
        val fishpondInfo = FishpondInfo(name = name)
        var res = false

        newChildRef.setValue(fishpondInfo).addOnCompleteListener {
            res = it.isSuccessful
        }

        return res
    }


    fun getFishpondList(): List<FishpondInfo> {
        val reference = databaseConnect.getReference("fishponds")
        var results = mutableListOf<FishpondInfo>()

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               if (snapshot.exists()) {
                   for (pondSnap in snapshot.children){
                       val pondInfo = pondSnap.getValue(FishpondInfo::class.java)
                       results.add(pondInfo!!)
                   }
               }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", error.toException())
            }

        })

        return results
    }
}

data class fishpondListResult(
    val fispondInfoList: FishpondInfo,
    val resultMessage: String?,
)