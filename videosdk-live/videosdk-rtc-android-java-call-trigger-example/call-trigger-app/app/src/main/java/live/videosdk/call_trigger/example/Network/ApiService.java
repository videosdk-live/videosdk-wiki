package live.videosdk.call_trigger.example.Network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/initiate-call")
    Call<String> initiateCall(@Body Map<String, Object> callRequestBody);

    @POST("/update-call")
    Call<String> updateCall(@Body Map<String,Object> callUpdateBody);
}
