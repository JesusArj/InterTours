package com.intertours.intertoursapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.google.android.datatransport.Priority;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intertours.intertoursapp.api.ApiService;
import com.intertours.intertoursapp.api.request.RouteRequest;
import com.intertours.intertoursapp.api.response.RouteResponse;
import com.intertours.intertoursapp.utils.AppConstants;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    private TextView bienvenida_txt, result;

    private AutocompleteSupportFragment autocomplete;

    private final Gson gson = new GsonBuilder().setLenient().create();

    private final Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConstants.API_BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();

    private ApiService apiService = retrofit.create(ApiService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bienvenida_txt = (TextView) findViewById(R.id.bienvenida_txt);
        autocomplete = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        Places.initialize(getApplicationContext(), "AIzaSyBD-G6oLUdzpDbD9mPRepdMhXgny0XP1BU", Locale.forLanguageTag("es"));
        PlacesClient placesClient = Places.createClient(this);
        String token = getIntent().getStringExtra("jwt");
        JWT jwt = new JWT(token);
        String userName = jwt.getClaim("sub").asString();
        bienvenida_txt.setText("¡Bienvenid@, " + userName + "!");
        autocomplete.setTypeFilter(TypeFilter.CITIES);
        autocomplete.setPlaceFields(Arrays.asList(Place.Field.ADDRESS_COMPONENTS));
        autocomplete.setHint("¿Dónde estás?");
        autocomplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                RouteRequest request = new RouteRequest();
                for (AddressComponent addressComponent : place.getAddressComponents().asList()) {
                    if(addressComponent.getTypes().get(0).equals("locality")){
                        request.setMunicipio(addressComponent.getName());
                    }
                    else if(addressComponent.getTypes().get(0).equals("administrative_area_level_2")){
                        request.setProvincia(addressComponent.getName());
                    }
                }
                Call<List<RouteResponse>> call = apiService.findRoute(request.getProvincia(), request.getMunicipio(), "jwt=" + token);
                call.enqueue(new Callback<List<RouteResponse>>() {
                    @Override
                    public void onResponse(Call<List<RouteResponse>> call, Response<List<RouteResponse>> response) {
                        if (response.isSuccessful()) {
                            List<RouteResponse> rutas = response.body();

                        }
                    }

                    @Override
                    public void onFailure(Call<List<RouteResponse>> call, Throwable t) {
                        Toast.makeText(HomeActivity.this, "FALLO AL LLAMAR A INTERTOURS", Toast.LENGTH_LONG ).show();
                    }
                });
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.println(Priority.HIGHEST.ordinal(), TAG, status.toString());
            }
        });

    }
    }