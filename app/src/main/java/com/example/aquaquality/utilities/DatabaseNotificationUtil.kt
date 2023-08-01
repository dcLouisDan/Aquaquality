package com.example.aquaquality.utilities

import android.content.Context
import android.util.Log
import com.example.aquaquality.data.FishpondInfo
import com.example.aquaquality.presentation.sign_in.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DatabaseNotificationUtil {
    private val database = Firebase.database
    private val auth = Firebase.auth
    private lateinit var fishpondsRef: DatabaseReference

    fun watch(context: Context) {
        database.useEmulator("10.0.2.2", 9000)
        val userId = getSignedInUser()?.userId
        if (userId != null) {
            fishpondsRef = database.getReference("$userId/fishponds")

            fishpondsRef.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.i("Firebase", "Fishpond Added")

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val fishpondInfo = snapshot.getValue(FishpondInfo::class.java)
                    Log.i("Child Change", "Info: ${fishpondInfo}")


                    if (fishpondInfo?.tempValue!! < 0) {
                        val notif = WarningNotification(
                            context,
                            "Temperature Warning",
                            "The temperature exceeded the recommended threshold."
                        )
                        Log.i("Value Change", "Warning!! Temperature value: ${fishpondInfo.tempValue}")
                        notif.show()
                    }

                    if (fishpondInfo.tempValue > 0) {
                        val notif = WarningNotification(
                            context,
                            "Temperature Warning",
                            "The temperature exceeded the recommended threshold."
                        )
                        Log.i("Value Change", "Warning!! Temperature value: ${fishpondInfo.tempValue}")
                        notif.show()
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

    private fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            email = email,
            profilePictureUrl = photoUrl.toString()
        )
    }
}