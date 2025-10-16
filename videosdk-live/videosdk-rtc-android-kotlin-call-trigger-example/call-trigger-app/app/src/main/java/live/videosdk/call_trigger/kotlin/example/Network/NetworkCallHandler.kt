package live.videosdk.call_trigger.kotlin.example.Network

import android.util.Log
import live.videosdk.call_trigger.kotlin.example.MainApplication

import live.videosdk.call_trigger.kotlin.example.Services.MyFirebaseMessagingService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkCallHandler  {

    companion object{
        lateinit var myCallId: String
        lateinit var FcmToken: String
        lateinit var calleeInfoToken:String
    }
        fun updateCall(call_update: String){
        val fcmToken: String = MyFirebaseMessagingService.FCMtoken

        val callerInfo: MutableMap<String, String> = HashMap()
        val callUpdateBody: MutableMap<String, Any> = HashMap()

        //callerInfo
        callerInfo["callerId"] = myCallId
        callerInfo["token"] = fcmToken
        //CallUpdateBody
        callUpdateBody["callerInfo"] = callerInfo
        callUpdateBody["type"] = call_update

        val apiService = ApiClient.client!!.create(ApiService::class.java)
        val call : Call<String> = apiService.updateCall(callUpdateBody)
        call.enqueue(object :Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("TAG", "onFailure: "+ t.message)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("TAG", "Call updated successfully: " + response.body())
            }
        })
    }


    fun initiateCall() {
        val apiService: ApiService = ApiClient.client!!.create(ApiService::class.java)

        val callerInfo: MutableMap<String, String> = HashMap()
        val calleeInfo: MutableMap<String, String> = HashMap()
        val videoSDKInfo: MutableMap<String, String> = HashMap()

        // callerInfo
        callerInfo["callerId"] = myCallId
        callerInfo["token"] = FcmToken
        // calleeInfo
        calleeInfo["token"] = calleeInfoToken
        // videoSDKInfo
        videoSDKInfo["meetingId"] = MainApplication.meetingId ?: return
        videoSDKInfo["token"] = MainApplication.token

        val callRequestBody: MutableMap<String, Any> = HashMap()
        callRequestBody["callerInfo"] = callerInfo
        callRequestBody["calleeInfo"] = calleeInfo
        callRequestBody["videoSDKInfo"] = videoSDKInfo

        val call: Call<String> = apiService.initiateCall(callRequestBody)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Log.d("API", "Call initiated: " + response.body())
                } else {
                    Log.e("API", "Failed to initiate call: " + response.message())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("API", "API call failed: " + t.message)
            }
        })
    }
}