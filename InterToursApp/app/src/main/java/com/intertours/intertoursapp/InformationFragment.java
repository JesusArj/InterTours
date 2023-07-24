package com.intertours.intertoursapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformationFragment extends Fragment {

    private TextView titulo, description, stops;

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

        // Obtener los argumentos y actualizar los campos
        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString("title");
            String description = args.getString("description");
            String stops = args.getString("stops");

            updateFields(title, description, stops);
        }
        return view;
    }

    public static InformationFragment newInstance(String title, String description, String stops) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
        args.putString("stops", stops);
        fragment.setArguments(args);
        return fragment;
    }

    public void updateFields(String title, String descripcion, String paradas) {
        titulo.setText(title);
        description.setText(descripcion);
        stops.setText(paradas);
    }
}