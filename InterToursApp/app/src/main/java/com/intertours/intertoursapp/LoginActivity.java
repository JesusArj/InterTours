package com.intertours.intertoursapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.jwt.JWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intertours.intertoursapp.api.ApiService;
import com.intertours.intertoursapp.api.request.AuthenticationRequest;
import com.intertours.intertoursapp.utils.AppConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;

    private final Gson gson = new GsonBuilder().setLenient().create();

    private final Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConstants.API_BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();

    private ApiService apiService = retrofit.create(ApiService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.LOGIN_USERNAME);
        password = (EditText) findViewById(R.id.LOGIN_PASSWORD);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        Call<Boolean> call = apiService.validate( sharedPref.getString("jwt", ""));
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body()) {
                    Intent homepageRedirection = new Intent(LoginActivity.this, HomeActivity.class);
                    homepageRedirection.putExtra("jwt", sharedPref.getString("jwt", ""));
                    startActivity(homepageRedirection);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Algo ha salido mal.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loginButtonClick(View view) {
        AuthenticationRequest request = new AuthenticationRequest(username.getText().toString(), password.getText().toString());
        Call<String> call = apiService.login(request);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String token = response.body();
                    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("jwt", token);
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Sesión iniciada correctamente.", Toast.LENGTH_LONG).show();
                    Intent homepageRedirection = new Intent(LoginActivity.this, HomeActivity.class);
                    homepageRedirection.putExtra("jwt", sharedPref.getString("jwt", ""));
                    startActivity(homepageRedirection);
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciales inválidas", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Algo ha salido mal, inténtelo de nuevo más tarde.", Toast.LENGTH_LONG).show();
            }
        });

    }

}