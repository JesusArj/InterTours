package com.intertours.intertoursapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.intertours.intertoursapp.api.ApiService;
import com.intertours.intertoursapp.api.response.CoordenadasDTO;
import com.intertours.intertoursapp.api.response.RouteResponse;
import com.intertours.intertoursapp.utils.AppConstants;
import com.intertours.intertoursapp.utils.AppUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformationFragment extends Fragment {

    private TextView titulo, description, stops;

    private RatingBar ratingBar, ratingIndicatorBar;

    private Button ratingButton;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.API_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    private ApiService apiService = retrofit.create(ApiService.class);

    public InformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        titulo = (TextView) view.findViewById(R.id.routeDetailTitle);
        description = (TextView) view.findViewById(R.id.routeDescription);
        stops= (TextView) view.findViewById(R.id.routeStops);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        ratingIndicatorBar = (RatingBar) view.findViewById(R.id.ratingIndicatorBar);
        ratingBar.setStepSize(1f);
        ratingButton = (Button) view.findViewById(R.id.ratingButton);
        Bundle args = getArguments();
        Call<Integer> call = apiService.getUserValoration(args.getInt("idRuta"), "jwt=" + args.getString("jwt"));
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    if(response.body() != -1){
                        ratingBar.setRating(response.body());
                    }
                }else{
                    Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        Call<Float> callIndicator = apiService.getRouteValoration(args.getInt("idRuta"), "jwt=" + args.getString("jwt"));
        callIndicator.enqueue(new Callback<Float>() {
            @Override
            public void onResponse(Call<Float> call, Response<Float> response) {
                if (response.isSuccessful()) {
                    if(response.body() != -1){
                        ratingIndicatorBar.setRating(response.body());
                    }
                }else{
                    Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Float> call, Throwable t) {
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        if (args != null) {
            String title = args.getString("title");
            String description = args.getString("description");
            String stops = args.getString("stops");
            updateFields(title, description, stops);
        }
        return view;
    }

    public static InformationFragment newInstance(RouteResponse route, String jwt) {
        List<CoordenadasDTO> coordenadas = route.getCoordenadas();
        List<CoordenadasDTO> coordenadasOrdenadas = coordenadas.stream()
                .sorted((c1, c2) -> Integer.compare(c1.getOrden(), c2.getOrden()))
                .collect(Collectors.toList());
        String[] nombresParadas = coordenadasOrdenadas.stream()
                .map(CoordenadasDTO::getNombreParada)
                .toArray(String[]::new);

        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString("title", route.getTitulo());
        args.putString("description", route.getDescripcion());
        args.putString("stops", AppUtils.buildStopsDescription(nombresParadas));
        args.putString("jwt", jwt);
        args.putInt("idRuta", route.getIdRuta());
        fragment.setArguments(args);
        return fragment;
    }

    public void updateFields(String title, String descripcion, String paradas) {
        titulo.setText(title);
        description.setText(descripcion);
        stops.setText(paradas);
    }
    public void ratingButtonClick(View view){
        Toast.makeText(getContext(), "PULSADO!", Toast.LENGTH_SHORT).show();
    }
}