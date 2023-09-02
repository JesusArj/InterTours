package com.intertours.intertoursapp.api;

import com.intertours.intertoursapp.api.request.AuthenticationRequest;
import com.intertours.intertoursapp.api.request.RatingRequest;
import com.intertours.intertoursapp.api.response.RatingResponse;
import com.intertours.intertoursapp.api.response.RouteResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
/*
* INTERFAZ USADA PARA LLAMAR A LA API REST USANDO LA TECNOLOGÍA RETROFIT2
*/
public interface ApiService {
    @POST("auth/login")
    Call<String> login(@Body AuthenticationRequest request);

    @POST("auth/registerTurista")
    Call<Void> register(@Body AuthenticationRequest request);

    @GET("auth/validateToken")
    Call<Boolean> validate(@Query("token") String token);

    @GET("rutas/detalleRuta")
    Call<List<RouteResponse>> findRoute(@Query("provincia") String provincia, @Query("municipio") String municipio, @Query("pais") String pais,  @Header("Cookie") String token);

    @GET("rutas/audio")
    Call<ResponseBody> getAudio(@Query("idRuta") Integer idRuta, @Query("orden") Integer orden, @Header("Cookie") String token);

    @GET("rutas/valoracionUser")
    Call<Integer> getUserValoration(@Query("id") Integer id, @Header("Cookie") String token);

    @GET("rutas/valoracion/{id}")
    Call<Float> getRouteValoration(@Path("id") Integer id, @Header("Cookie") String token);

    @POST("rutas/insertarValoracion")
    Call<RatingResponse> sendRouteRating(@Body RatingRequest request, @Header("Cookie") String token);
}
