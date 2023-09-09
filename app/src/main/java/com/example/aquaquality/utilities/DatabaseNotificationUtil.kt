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
    private val database =
        Firebase.database("https://aquaquality-fe2e7-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val auth = Firebase.auth
    private lateinit var fishpondsRef: DatabaseReference
    private val settingsRef: DatabaseReference
    private var sentNotifications: Map<String, MutableSet<Int>> = emptyMap()


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

            val notif1 = WarningNotification(
                1,
                context,
                "Temperature Warning",
                "The temperature is below the recommended threshold."
            )
            val notif2 = WarningNotification(
                2,
                context,
                "Temperature Warning",
                "The temperature exceeded the recommended threshold."
            )
            val notif3 = WarningNotification(
                3,
                context,
                "pH Warning",
                "The ph level is below the recommended threshold."
            )
            val notif4 = WarningNotification(
                4,
                context,
                "pH Warning",
                "The ph level exceeded the recommended threshold."
            )
            val notif5 = WarningNotification(
                5,
                context,
                "Turbidity Warning",
                "The turbidity is below the recommended threshold."
            )
            val notif6 = WarningNotification(
                6,
                context,
                "Turbidity Warning",
                "The turbidity exceeded the recommended threshold."
            )

            initializeSettings { settingsInfo ->
                fishpondsRef.addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        Log.i("Firebase", "Fishpond Added")
                        sentNotifications = sentNotifications.plus(Pair(snapshot.key!!, mutableSetOf()))
                        Log.i("Notifications", "Fishpond Added: $sentNotifications")
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                        val fishpondInfo = snapshot.getValue(FishpondInfo::class.java)!!
                        Log.i("Child Change", "Info: $fishpondInfo")

                        if (fishpondInfo.connectedDeviceId != null) {
                            checkParameterStatus(
                                settingsInfo = settingsInfo,
                                fishpondInfo = fishpondInfo,
                                onLowTemp = {
                                    if (!sentNotifications[fishpondInfo.id]?.contains(1)!!) {
                                        Log.i(
                                            "Value Change",
                                            "Warning!! Temperature value: ${fishpondInfo.tempValue} Min-Temp: ${settingsInfo.minTemp}"
                                        )
                                        notif1.show()
                                        sentNotifications[fishpondInfo.id]?.add(1)
                                    }
                                },
                                onHighTemp = {
                                    if (!sentNotifications[fishpondInfo.id]?.contains(2)!!) {

                                        Log.i(
                                            "Value Change",
                                            "Warning!! Temperature value: ${fishpondInfo.tempValue}"
                                        )
                                        notif2.show()
                                        sentNotifications[fishpondInfo.id]?.add(2)
                                    }
                                },
                                onSafeTemp = {
                                    if (sentNotifications[fishpondInfo.id]?.contains(1)!!) {
                                        Log.i("Notification Removal", "Removed alert: 1")
                                        sentNotifications[fishpondInfo.id]?.remove(1)
                                        notif1.clear()
                                    }
                                    if (sentNotifications[fishpondInfo.id]?.contains(2)!!) {
                                        sentNotifications[fishpondInfo.id]?.remove(2)
                                        notif2.clear()
                                    }
                                },
                                onLowPh = {
                                    if (!sentNotifications[fishpondInfo.id]?.contains(3)!!) {

                                        Log.i(
                                            "Value Change",
                                            "Warning!! pH value: ${fishpondInfo.phValue}"
                                        )
                                        notif3.show()
                                        sentNotifications[fishpondInfo.id]?.add(3)
                                    }
                                },
                                onHighPh = {
                                    if (!sentNotifications[fishpondInfo.id]?.contains(4)!!) {

                                        Log.i(
                                            "Value Change",
                                            "Warning!! pH value: ${fishpondInfo.phValue}"
                                        )
                                        notif4.show()
                                        sentNotifications[fishpondInfo.id]?.add(4)
                                    }
                                },
                                onSafePh = {
                                    if (sentNotifications[fishpondInfo.id]?.contains(3)!!) {
                                        sentNotifications[fishpondInfo.id]?.remove(3)
                                        notif3.clear()
                                    }
                                    if (sentNotifications[fishpondInfo.id]?.contains(4)!!) {
                                        sentNotifications[fishpondInfo.id]?.remove(4)
                                        notif4.clear()
                                    }
                                },
                                onLowTurb = {
                                    if (!sentNotifications[fishpondInfo.id]?.contains(5)!!) {

                                        Log.i(
                                            "Value Change",
                                            "Warning!! Turbidity value: ${fishpondInfo.turbidityValue}"
                                        )
                                        notif5.show()
                                        sentNotifications[fishpondInfo.id]?.add(5)
                                    }
                                },
                                onHighTurb = {
                                    if (!sentNotifications[fishpondInfo.id]?.contains(6)!!) {

                                        Log.i(
                                            "Value Change",
                                            "Warning!! Turbidity value: ${fishpondInfo.turbidityValue}"
                                        )
                                        notif6.show()
                                        sentNotifications[fishpondInfo.id]?.add(6)
                                    }
                                },
                                onSafeTurb = {
                                    if (sentNotifications[fishpondInfo.id]?.contains(5)!!) {
                                        sentNotifications[fishpondInfo.id]?.remove(5)
                                        notif5.clear()
                                    }
                                    if (sentNotifications[fishpondInfo.id]?.contains(6)!!) {
                                        sentNotifications[fishpondInfo.id]?.remove(6)
                                        notif6.clear()
                                    }
                                }
                            )
                        }
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