package org.maziarz.org;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LocationApi {
    @POST("report")
    Call<Void> sendLocation(@Body LocationData locationData);
}

