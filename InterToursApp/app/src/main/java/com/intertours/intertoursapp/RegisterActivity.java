package com.intertours.intertoursapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity {

    private EditText username, password, repeatPassword;

    private final Gson gson = new GsonBuilder().setLenient().create();

    private final Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConstants.API_BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();

    private ApiService apiService = retrofit.create(ApiService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.REGISTER_USERNAME);
        password = (EditText) findViewById(R.id.REGISTER_PASSWORD);
        repeatPassword = (EditText) findViewById(R.id.REGISTER_PASSWORD_REPEAT);

    }


    public void registerClick(View view){
        if(isRegisterValid(username.getText().toString(), password.getText().toString(), repeatPassword.getText().toString())){
            Call<Void> call = apiService.register(new AuthenticationRequest(username.getText().toString(), password.getText().toString()));
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.code() == 409){
                        Toast.makeText(RegisterActivity.this, "Nombre de usuario ya en uso.", Toast.LENGTH_SHORT).show();
                    }
                    else if(response.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente. Ya puede iniciar sesión en el sistema.", Toast.LENGTH_LONG).show();
                        Intent loginPageRedirection = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(loginPageRedirection);
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Error interno. Inténtelo de nuevo más tarde.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean isRegisterValid( String username, String pass, String repeatPass){
        if(username.trim().isEmpty()){
            Toast.makeText(this, "Nombre de usuario no válido.", Toast.LENGTH_SHORT).show();
        }
        else if(!pass.equals(repeatPass)){
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(pass.length() < 8 ){
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}