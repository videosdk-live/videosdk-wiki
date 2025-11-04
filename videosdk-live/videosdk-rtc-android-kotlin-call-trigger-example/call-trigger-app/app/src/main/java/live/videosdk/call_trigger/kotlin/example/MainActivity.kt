package live.videosdk.call_trigger.kotlin.example

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import live.videosdk.call_trigger.kotlin.example.FirebaseDatabase.DatabaseUtils
import live.videosdk.call_trigger.kotlin.example.Network.NetworkUtils
import live.videosdk.call_trigger.kotlin.example.Network.NetworkCallHandler
import java.util.Random

class MainActivity : AppCompatActivity() {
    private lateinit var callerIdInput: EditText
    private lateinit var myId: TextView
    private lateinit var copyIcon: ImageView
    private var myCallId: String = (10000000 + Random().nextInt(90000000)).toString()
    private lateinit var FcmToken: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)
        }
        myId = findViewById(R.id.txt_callId)
        callerIdInput = findViewById(R.id.caller_id_input)

        copyIcon = findViewById(R.id.copyIcon)
        val callButton = findViewById<Button>(R.id.call_button)
        myId.text = myCallId


        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("copied text", myCallId)

        copyIcon.setOnClickListener {
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        NetworkCallHandler.myCallId = myCallId
        DatabaseUtils.myCallId = myCallId


        NetworkUtils().createMeeting(object : MeetingIdCallBack {
            override fun onMeetingIdReceived(meetingId: String, token: String) {
                MainApplication.meetingId=meetingId

            }
        })
        //Firebase Notification
        val channel = NotificationChannel("notification_channel", "notification_channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
        FirebaseMessaging.getInstance().subscribeToTopic("general")
            .addOnCompleteListener { task ->
                var msg = "Subscribed Successfully"
                if (!task.isSuccessful) {
                    msg = "Subscription failed"
                }
                Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
            }

        //Firebase Database Actions
        val databaseUtils = DatabaseUtils()
        val databaseReference = FirebaseDatabase.getInstance().reference
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
            FcmToken = task.result
            NetworkCallHandler.FcmToken = task.result
            DatabaseUtils.FcmToken = task.result
            databaseUtils.sendUserDataToFirebase(databaseReference)
        }

        //telecom Api
        registerPhoneAccount()

        callButton.setOnClickListener {
            val callerNumber = callerIdInput.text.toString()
            if (callerNumber.length == 8) {
                databaseUtils.retrieveUserData(databaseReference, callerNumber)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Please input the correct caller ID",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 133) {
            var allPermissionsGranted = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false
                    break
                }
            }

            if (allPermissionsGranted) {
                registerPhoneAccount()
            } else {
                Toast.makeText(this, "Permissions are required for call management", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun registerPhoneAccount() {
        val phoneAccount = PhoneAccount.builder(MainApplication.phoneAccountHandle, "VideoSDK")
            .setCapabilities(PhoneAccount.CAPABILITY_CALL_PROVIDER)
            .build()

        MainApplication.telecomManager?.registerPhoneAccount(phoneAccount)


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        var checkAccount = 0
        val list: List<PhoneAccountHandle> =
            MainApplication.telecomManager!!.callCapablePhoneAccounts
        for (handle in list) {
            if (handle.componentName.className == "live.videosdk.call_trigger.kotlin.example.Services.CallConnectionService") {
                checkAccount++
                break
            }
        }
        if (checkAccount == 0) {
            val intent = Intent(TelecomManager.ACTION_CHANGE_PHONE_ACCOUNTS)
            startActivity(intent)
        }
    }

    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode)
            return false
        }
        return true
    }

    companion object {
        private const val PERMISSION_REQ_ID = 22

        private val REQUESTED_PERMISSIONS = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.POST_NOTIFICATIONS
        )
    }
}
