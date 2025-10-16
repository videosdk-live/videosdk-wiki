package live.videosdk.call_trigger.example.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telecom.TelecomManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Toast;
import java.util.Map;

import live.videosdk.call_trigger.example.MainActivity;
import live.videosdk.call_trigger.example.MainApplication;
import live.videosdk.call_trigger.example.Meeting.MeetingActivity;
import live.videosdk.call_trigger.example.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";
    private static final String CHANNEL_ID = "notification_channel";

    String callerID;
    String meetingId ;
    String token ;
    public static String FCMtoken;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle incoming message (call request)
        Map<String, String> data = remoteMessage.getData();

        if (!data.isEmpty()) {
            try {
                JSONObject object = new JSONObject(data.get("info"));
                JSONObject callerInfo = object.getJSONObject("callerInfo");
                callerID = callerInfo.getString("callerId");
                FCMtoken  =  callerInfo.getString("token");
                if (object.has("videoSDKInfo")) {
                    JSONObject videoSdkInfo = object.getJSONObject("videoSDKInfo");
                    meetingId = videoSdkInfo.getString("meetingId");
                    token = videoSdkInfo.getString("token");
                    handleIncomingCall(callerID);
                }
                String type = (String) object.get("type");

                if(type.equals("ACCEPTED")){
                    startMeeting();
                } else if (type.equals("REJECTED")) {
                    showIncomingCallNotification(callerID);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(), "CALL REJECTED FROM CALLER ID: " + callerID, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            Log.d(TAG, "onMessageReceived: No data found in the notification payload.");
        }
    }

    private void startMeeting() {
        Intent intent = new Intent(getApplicationContext(), MeetingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("meetingId", MainApplication.getMeetingId());
        intent.putExtra("token", MainApplication.getToken());
        startActivity(intent);
    }

    private void handleIncomingCall(String callerId) {

        // Create a bundle to pass call details
        Bundle extras = new Bundle();
        Uri uri = Uri.fromParts("tel", callerId, null);
        extras.putParcelable(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS, uri);
        extras.putString("meetingId", meetingId);
        extras.putString("token", token);
        extras.putString("callerID",callerId);

        try {
            MainApplication.telecomManager.addNewIncomingCall(MainApplication.phoneAccountHandle, extras);
        } catch (Throwable cause) {
            Log.e("handleIncomingCall", "error in addNewIncomingCall ", cause.getCause());
        }
    }

    private void showIncomingCallNotification(String callerId) {
        createNotificationChannel();

        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_call_24)
                .setContentTitle("Call REJECTED ")
                .setContentText("Call from " + callerId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(pendingIntent, true)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Incoming Calls", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}