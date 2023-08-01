package com.intertours.intertoursapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.intertours.intertoursapp.api.response.RouteResponse;
import com.intertours.intertoursapp.utils.AppConstants;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<RouteResponse> {

    private final int resourceLayout;
    private final List<RouteResponse> items;

    public CustomListAdapter(Context context, int resource, List<RouteResponse> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(resourceLayout, null);
        }

        RouteResponse route = items.get(position);

        if (route != null) {

            CardView cardView = v.findViewById(R.id.cardView);
            TextView titleTextView = v.findViewById(R.id.titleTextView);
            TextView subtitleTextView = v.findViewById(R.id.subtitleTextView);

            if (titleTextView != null) {
                titleTextView.setText(route.getTitulo());
            }

            if (subtitleTextView != null) {
                subtitleTextView.setText(route.getDescripcion().length() < AppConstants.MAX_DESCRIPTION_SIZE ? route.getDescripcion() : route.getDescripcion().substring(0,AppConstants.MAX_DESCRIPTION_SIZE).trim() + "...");
            }
        }

        return v;
    }
}

