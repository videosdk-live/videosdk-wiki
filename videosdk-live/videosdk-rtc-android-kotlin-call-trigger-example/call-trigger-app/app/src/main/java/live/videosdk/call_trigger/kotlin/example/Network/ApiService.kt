package live.videosdk.call_trigger.kotlin.example.Network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/initiate-call")
    @JvmSuppressWildcards
    fun initiateCall(@Body callRequestBody: Map<String, Any>): Call<String>

    @POST("/update-call")
    @JvmSuppressWildcards
    fun updateCall(@Body callUpdateBody: Map<String, Any>): Call<String>
}
