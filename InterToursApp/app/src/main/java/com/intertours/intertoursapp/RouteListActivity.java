package com.intertours.intertoursapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.intertours.intertoursapp.api.response.RouteResponse;

import java.io.Serializable;
import java.util.List;

public class RouteListActivity extends AppCompatActivity {
    private ListView listView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);
        textView = findViewById(R.id.titulo_rutas_txt);
        listView = findViewById(R.id.routesListView);
        List<RouteResponse> list = (List<RouteResponse>) getIntent().getSerializableExtra("routeList");
        textView.setText(list.size() != 0 ? list.size() + " Resultados en " + list.get(0).getMunicipio() + "," + list.get(0).getProvincia() : "Lo sentimos, no hay resultados en el lugar indicado.");
        String token = getIntent().getStringExtra("jwt");
        CustomListAdapter adapter = new CustomListAdapter(this, R.layout.routes_list_item, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent routeDetailRedirection = new Intent(RouteListActivity.this, RouteDetailActivity.class);
                routeDetailRedirection.putExtra("route", (Serializable) list.get(i));
                routeDetailRedirection.putExtra("jwt", token);
                view.setBackgroundColor(ContextCompat.getColor(adapterView.getContext(), R.color.list_item_pressed_color));
                startActivity(routeDetailRedirection);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int visibleItemCount = listView.getChildCount();

        // Recorrer cada elemento visible y cambiar el fondo
        for (int i = 0; i < visibleItemCount; i++) {
            View itemView = listView.getChildAt(i);
            // Cambiar el color de fondo a lo que desees
            itemView.setBackgroundColor(getResources().getColor(R.color.list_item_default_color));
        }
    }
}