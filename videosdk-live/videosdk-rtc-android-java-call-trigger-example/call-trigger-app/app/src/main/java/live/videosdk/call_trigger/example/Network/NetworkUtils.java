package live.videosdk.call_trigger.example.Network;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import live.videosdk.call_trigger.example.MainApplication;
import live.videosdk.call_trigger.example.MeetingIdCallBack;

public class NetworkUtils {

    //Replace with the token you generated from the VideoSDK Dashboard
    String sampleToken = MainApplication.getToken();
    public void createMeeting(MeetingIdCallBack callBack) {
        // we will make an API call to VideoSDK Server to get a roomId
        AndroidNetworking.post("https://api.videosdk.live/v2/rooms")
                .addHeaders("Authorization", sampleToken) //we will pass the token in the Headers
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // response will contain `meetingID`
                            final String meetingId = response.getString("roomId");
                            callBack.onMeetingIdReceived(meetingId,sampleToken);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                    }
                });
    }
}

