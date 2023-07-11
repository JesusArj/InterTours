package com.intertours.intertoursapp.api;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login")
    Call<Void> login();
}
