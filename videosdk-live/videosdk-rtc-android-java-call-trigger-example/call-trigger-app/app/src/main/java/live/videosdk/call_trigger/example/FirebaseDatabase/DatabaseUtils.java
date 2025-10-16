package live.videosdk.call_trigger.example.FirebaseDatabase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import live.videosdk.call_trigger.example.MainActivity;
import live.videosdk.call_trigger.example.Network.NetworkCallHandler;

public class DatabaseUtils {

    String calleeInfoToken ;
    public static String FcmToken ;

    public void sendUserDataToFirebase(DatabaseReference databaseReference) {

        DatabaseReference usersRef = databaseReference.child("User");

        usersRef.orderByChild("token").equalTo(FcmToken).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Token exists, update the callerId
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        userSnapshot.getRef().child("callerId").setValue(MainActivity.myCallId)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("FirebaseData", "CallerId successfully updated.");
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FirebaseError", "Failed to update callerId.", e);
                                });
                    }
                } else {
                    // Token doesn't exist, create new entry
                    String userId = usersRef.push().getKey();
                    Map<String, Object> map = new HashMap<>();
                    map.put("callerId", MainActivity.myCallId);
                    map.put("token", FcmToken);

                    if (userId != null) {
                        usersRef.child(userId).setValue(map)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("FirebaseData", "Data successfully saved.");
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FirebaseError", "Failed to save data.", e);
                                });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error checking for existing token", databaseError.toException());
            }
        });
    }

    public void retrieveUserData(DatabaseReference databaseReference, String callerNumber) {
        NetworkCallHandler callHandler = new NetworkCallHandler();
        databaseReference.child("User").orderByChild("callerId").equalTo(callerNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String token = data.child("token").getValue(String.class);
                        if (token != null) {
                            calleeInfoToken = token;
                            NetworkCallHandler.calleeInfoToken = token;
                            callHandler.initiateCall();
                            break;
                        }
                    }
                } else {
                    Log.d("TAG", "retrieveUserData: No matching callerId found");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read data from Firebase", error.toException());
            }
        });
    }

}
