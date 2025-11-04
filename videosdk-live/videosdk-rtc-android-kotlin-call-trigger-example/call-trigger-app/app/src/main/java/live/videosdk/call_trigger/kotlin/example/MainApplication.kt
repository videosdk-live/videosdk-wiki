package live.videosdk.call_trigger.kotlin.example

import android.app.Application
import android.content.ComponentName
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import live.videosdk.call_trigger.kotlin.example.Services.CallConnectionService
import live.videosdk.rtc.android.VideoSDK

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        VideoSDK.initialize(applicationContext)

        telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
        val componentName = ComponentName(this, CallConnectionService::class.java)
        phoneAccountHandle = PhoneAccountHandle(componentName, "myAccountId")
    }

    companion object {
        var telecomManager: TelecomManager? = null
        var phoneAccountHandle: PhoneAccountHandle? = null
        var meetingId: String? = null
        var token: String = "VideoSDK Token"
    }
}