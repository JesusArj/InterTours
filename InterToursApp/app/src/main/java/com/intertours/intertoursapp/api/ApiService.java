package com.intertours.intertoursapp.api;

import com.intertours.intertoursapp.api.request.AuthenticationRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("auth/login")
    Call<String> login(@Body AuthenticationRequest request);

    @GET("auth/validateToken")
    Call<Boolean> validate(@Query("token") String token);
}
