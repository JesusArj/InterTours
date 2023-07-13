package com.intertours.intertoursapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;

public class HomeActivity extends AppCompatActivity {

    private TextView bienvenida_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bienvenida_txt = (TextView) findViewById(R.id.bienvenida_txt);
        String token = getIntent().getStringExtra("jwt");
        JWT jwt = new JWT(token);
        String userName = jwt.getClaim("sub").asString();
        bienvenida_txt.setText("¡Bienvenid@, " + userName + "!");
    }



}