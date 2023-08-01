package com.intertours.intertoursapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.intertours.intertoursapp.api.response.CoordenadasDTO;
import com.intertours.intertoursapp.api.response.RouteResponse;

import org.w3c.dom.Text;

import java.util.List;
import java.util.stream.Collectors;

public class RouteDetailActivity extends AppCompatActivity {

    private BottomNavigationView nav;

    private InformationFragment informacionFragment;

    private RouteMapsFragment routeMapsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        RouteResponse route = (RouteResponse) getIntent().getSerializableExtra("route");
        if (savedInstanceState == null) {
            InformationFragment informationFragment = InformationFragment.newInstance(route, getIntent().getStringExtra("jwt"));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, informationFragment)
                    .commit();
        }
        nav = (BottomNavigationView) findViewById(R.id.routeNav);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("jwt", getIntent().getStringExtra("jwt"));
                if(item.getItemId() == R.id.info){
                    informacionFragment = InformationFragment.newInstance(route, getIntent().getStringExtra("jwt"));
                    fragmentTransaction.replace(R.id.fragment_container, informacionFragment);
                }
                else if(item.getItemId() == R.id.map){
                    routeMapsFragment = new RouteMapsFragment(route.getCoordenadas());
                    if (!routeMapsFragment.isAdded()) {
                        routeMapsFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, routeMapsFragment)
                                .commit();
                    }
                }
                fragmentTransaction.commit();
                return true;
            }
        });

    }
}