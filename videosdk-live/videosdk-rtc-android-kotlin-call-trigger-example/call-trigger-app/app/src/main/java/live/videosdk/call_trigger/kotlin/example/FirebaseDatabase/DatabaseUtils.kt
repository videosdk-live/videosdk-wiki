package live.videosdk.call_trigger.kotlin.example.FirebaseDatabase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import live.videosdk.call_trigger.kotlin.example.Network.NetworkCallHandler

class DatabaseUtils {
    companion object{
    lateinit var myCallId: String
    lateinit var FcmToken: String
}
 private lateinit var calleeInfoToken : String

    val networkUtils: NetworkCallHandler = NetworkCallHandler()
    fun sendUserDataToFirebase(databaseReference: DatabaseReference) {
        val usersRef = databaseReference.child("User")
        usersRef.orderByChild("token").equalTo(FcmToken)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Token exists, update the callerId
                        for (userSnapshot in dataSnapshot.children) {
                            userSnapshot.ref.child("callerId").setValue(myCallId)
                                .addOnSuccessListener { aVoid: Void? ->
                                    Log.d("FirebaseData", "CallerId successfully updated.")
                                }
                                .addOnFailureListener { e: Exception? ->
                                    Log.e("FirebaseError", "Failed to update callerId.", e)
                                }
                        }
                    } else {
                        // Token doesn't exist, create new entry
                        val userId = usersRef.push().key
                        val map: MutableMap<String, Any?> = HashMap()
                        map["callerId"] = myCallId
                        map["token"] = FcmToken
                        if (userId != null) {
                            usersRef.child(userId).setValue(map)
                                .addOnSuccessListener { aVoid: Void? ->
                                    Log.d("FirebaseData", "Data successfully saved.")
                                }
                                .addOnFailureListener { e: Exception? ->
                                    Log.e("FirebaseError", "Failed to save data.", e)
                                }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(
                        "FirebaseError",
                        "Error checking for existing token",
                        databaseError.toException()
                    )
                }
            })
    }


    fun retrieveUserData(databaseReference: DatabaseReference, callerNumber: String) {
        databaseReference.child("User").orderByChild("callerId").equalTo(callerNumber)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (data in snapshot.children) {
                            val token = data.child("token").getValue(
                                String::class.java
                            )
                            if (token != null) {
                                calleeInfoToken = token
                                NetworkCallHandler.calleeInfoToken = token
                                networkUtils.initiateCall()
                                break
                            }
                        }
                    } else {
                        Log.d("TAG", "retrieveUserData: No matching callerId found")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Failed to read data from Firebase", error.toException())
                }
            })
    }
}