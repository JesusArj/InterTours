package com.intertours.intertoursapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.intertours.intertoursapp.api.response.RouteResponse;

import org.w3c.dom.Text;

import java.util.List;

public class RouteDetailActivity extends AppCompatActivity {

    private BottomNavigationView nav;
    private TextView titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        nav = (BottomNavigationView) findViewById(R.id.routeNav);
        titulo = (TextView) findViewById(R.id.routeDetailTitle);
        RouteResponse route = (RouteResponse) getIntent().getSerializableExtra("route");
        titulo.setText(route.getTitulo());
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.info){
                    Toast.makeText(RouteDetailActivity.this, "INFO", Toast.LENGTH_SHORT).show();
                }
                else if(item.getItemId() == R.id.map){
                    Toast.makeText(RouteDetailActivity.this, "MAP", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
}