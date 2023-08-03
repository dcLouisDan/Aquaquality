package com.example.aquaquality.utilities

import android.content.Context
import android.util.Log
import com.example.aquaquality.data.FishpondInfo
import com.example.aquaquality.data.SettingsInfo
import com.example.aquaquality.data.checkParameterStatus
import com.example.aquaquality.presentation.sign_in.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class DatabaseNotificationUtil {
    private val database = Firebase.database("https://aquaquality-fe2e7-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val auth = Firebase.auth
    private lateinit var fishpondsRef: DatabaseReference
    private val settingsRef: DatabaseReference

    init {
        val userId = getSignedInUser()?.userId
        settingsRef = database.getReference("$userId/settings")
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

    fun watch(context: Context) {
        val userId = getSignedInUser()?.userId
        if (userId != null) {
            fishpondsRef = database.getReference("$userId/fishponds")

            initializeSettings{ settingsInfo  ->
                fishpondsRef.addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        Log.i("Firebase", "Fishpond Added")

                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                        val fishpondInfo = snapshot.getValue(FishpondInfo::class.java)!!
                        Log.i("Child Change", "Info: $fishpondInfo")


                        checkParameterStatus(
                            settingsInfo = settingsInfo,
                            fishpondInfo = fishpondInfo,
                            onLowTemp = {
                                val notif = WarningNotification(
                                    1,
                                    context,
                                    "Temperature Warning",
                                    "The temperature is below the recommended threshold."
                                )
                                Log.i(
                                    "Value Change",
                                    "Warning!! Temperature value: ${fishpondInfo.tempValue}"
                                )
                                notif.show()
                            },
                            onHighTemp = {
                                val notif = WarningNotification(
                                    2,
                                    context,
                                    "Temperature Warning",
                                    "The temperature exceeded the recommended threshold."
                                )
                                Log.i(
                                    "Value Change",
                                    "Warning!! Temperature value: ${fishpondInfo.tempValue}"
                                )
                                notif.show()
                            },
                            onLowPh = {
                                val notif = WarningNotification(
                                    3,
                                    context,
                                    "pH Warning",
                                    "The ph level is below the recommended threshold."
                                )
                                Log.i(
                                    "Value Change",
                                    "Warning!! pH value: ${fishpondInfo.phValue}"
                                )
                                notif.show()
                            },
                            onHighPh = {
                                val notif = WarningNotification(
                                    4,
                                    context,
                                    "pH Warning",
                                    "The ph level exceeded the recommended threshold."
                                )
                                Log.i(
                                    "Value Change",
                                    "Warning!! pH value: ${fishpondInfo.phValue}"
                                )
                                notif.show()
                            },
                            onLowTurb = {
                                val notif = WarningNotification(
                                    5,
                                    context,
                                    "Turbidity Warning",
                                    "The turbidity is below the recommended threshold."
                                )
                                Log.i(
                                    "Value Change",
                                    "Warning!! Turbidity value: ${fishpondInfo.turbidityValue}"
                                )
                                notif.show()
                            },
                            onHighTurb = {
                                val notif = WarningNotification(
                                    6,
                                    context,
                                    "Turbidity Warning",
                                    "The turbidity exceeded the recommended threshold."
                                )
                                Log.i(
                                    "Value Change",
                                    "Warning!! Turbidity value: ${fishpondInfo.turbidityValue}"
                                )
                                notif.show()
                            }
                        )
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        Log.i("Firebase", "Fishpond Removed")

                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                        Log.i("Firebase", "Fishpond Moved")

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Firebase", "Action Cancelled")

                    }

                })
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