package live.videosdk.call_trigger.kotlin.example.Services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telecom.TelecomManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import live.videosdk.call_trigger.kotlin.example.MainActivity
import live.videosdk.call_trigger.kotlin.example.MainApplication
import live.videosdk.call_trigger.kotlin.example.Meeting.MeetingActivity
import live.videosdk.call_trigger.kotlin.example.R
import org.json.JSONException
import org.json.JSONObject

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCMService"
        private const val CHANNEL_ID = "notification_channel"
        lateinit var FCMtoken: String
    }

    private var callerID: String? = null
    private var meetingId: String? = null
    private var token: String? = null

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data

        if (data.isNotEmpty()) {
            try {
                val `object` = JSONObject(data["info"]!!)

                val callerInfo = `object`.getJSONObject("callerInfo")
                callerID = callerInfo.getString("callerId")
                FCMtoken = callerInfo.getString("token")

                if (`object`.has("videoSDKInfo")) {
                    val videoSdkInfo = `object`.getJSONObject("videoSDKInfo")
                    meetingId = videoSdkInfo.getString("meetingId")
                    token = videoSdkInfo.getString("token")
                    handleIncomingCall(callerID)
                }

                val type = `object`.getString("type")
                when (type) {
                    "ACCEPTED" -> startMeeting()
                    "REJECTED" -> {
                        showIncomingCallNotification(callerID)
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(applicationContext, "CALL REJECTED FROM CALLER ID: $callerID", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            } catch (e: JSONException) {
                throw RuntimeException(e)
            }
        } else {
            Log.d(TAG, "onMessageReceived: No data found in the notification payload.")
        }
    }

    private fun startMeeting() {
        val intent = Intent(applicationContext, MeetingActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra("meetingId", MainApplication.meetingId)
            putExtra("token", MainApplication.token)
        }
        startActivity(intent)
    }

    private fun handleIncomingCall(callerId: String?) {
        val extras = Bundle().apply {
            val uri = Uri.fromParts("tel", callerId, null)
            putParcelable(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS, uri)
            putString("meetingId", meetingId)
            putString("token", token)
            putString("callerID", callerId)
        }

        try {
            MainApplication.telecomManager?.addNewIncomingCall(MainApplication.phoneAccountHandle, extras)
        } catch (cause: Throwable) {
            Log.e("handleIncomingCall", "error in addNewIncomingCall", cause)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showIncomingCallNotification(callerId: String?) {
        createNotificationChannel()

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_call_24)
            .setContentTitle("Call REJECTED")
            .setContentText("Call from $callerId")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(pendingIntent, true)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, "Incoming Calls", NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}
