package live.videosdk.call_trigger.example.Network;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import live.videosdk.call_trigger.example.MainActivity;
import live.videosdk.call_trigger.example.MainApplication;
import live.videosdk.call_trigger.example.Services.MyFirebaseMessagingService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public  class NetworkCallHandler {

    static ApiService apiService = ApiClient.getClient().create(ApiService.class);
    public static String FcmToken;
    public static String calleeInfoToken;

    public static void updateCall(String call_update) {

        String fcmToken = MyFirebaseMessagingService.FCMtoken;

        Map<String, String> callerInfo = new HashMap<>();
        Map<String, Object> callUpdateBody = new HashMap<>();

        //callerInfo
        callerInfo.put("callerId", MainActivity.myCallId);
        callerInfo.put("token",fcmToken);

        //CallUpdateBody
        callUpdateBody.put("callerInfo",callerInfo);
        callUpdateBody.put("type",call_update);

        Call<String> call = apiService.updateCall(callUpdateBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "Call updated successfully: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e("API", "Call update failed", t);
            }
        });
    }


    public void initiateCall() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Map<String,String> callerInfo = new HashMap<>();
        Map<String,String> calleeInfo = new HashMap<>();
        Map <String,String> videoSDKInfo= new HashMap<>();

        //callerInfo
        callerInfo.put("callerId",MainActivity.myCallId);
        callerInfo.put("token",FcmToken);

        //calleeInfo
        calleeInfo.put("token",calleeInfoToken);

        //videoSDKInfo
        videoSDKInfo.put("meetingId", MainApplication.getMeetingId());
        videoSDKInfo.put("token",MainApplication.getToken());

        Map<String,Object> callRequestBody = new HashMap<>();
        callRequestBody.put("callerInfo",callerInfo);
        callRequestBody.put("calleeInfo",calleeInfo);
        callRequestBody.put("videoSDKInfo",videoSDKInfo);

        Call<String> call = apiService.initiateCall(callRequestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "Call initiated: " + response.body());
                } else {
                    Log.e("API", "Failed to initiate call: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e("API", "API call failed: " + t.getMessage());
            }
        });
    }
}
