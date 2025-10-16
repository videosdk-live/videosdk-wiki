package live.videosdk.call_trigger.kotlin.example.Network

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import live.videosdk.call_trigger.kotlin.example.MainApplication
import live.videosdk.call_trigger.kotlin.example.MeetingIdCallBack
import org.json.JSONException
import org.json.JSONObject

class NetworkUtils {
    //Replace with the token you generated from the VideoSDK Dashboard
    var sampleToken: String = MainApplication.token
    fun createMeeting(callBack: MeetingIdCallBack) {
        AndroidNetworking.post("https://api.videosdk.live/v2/rooms")
            .addHeaders("Authorization", sampleToken) //we will pass the token in the Headers
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        // response will contain `roomId`
                        val meetingId = response.getString("roomId")
                        callBack.onMeetingIdReceived(meetingId,sampleToken)
                        //
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(anError: ANError) {
                    anError.printStackTrace()
                }
            })
    }
}

