package live.videosdk.call_trigger.example;

import android.app.Application;
import android.content.ComponentName;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;

import live.videosdk.call_trigger.example.Services.CallConnectionService;
import live.videosdk.rtc.android.VideoSDK;

public class MainApplication extends Application {

    public static TelecomManager telecomManager;
    public static PhoneAccountHandle phoneAccountHandle;
    static String meetingId;
    static String token="VideoSDK Token";

    public static void setMeetingId(String meetingId) {
        MainApplication.meetingId = meetingId;
    }

    public  static String getMeetingId(){
        return meetingId;
    }

    public static String getToken() {
        return token;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        VideoSDK.initialize(getApplicationContext());

        telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);
        ComponentName componentName = new ComponentName(this, CallConnectionService.class);
        phoneAccountHandle = new PhoneAccountHandle(componentName, "myAccountId");

    }

}