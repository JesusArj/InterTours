package com.intertours.intertoursapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.datatransport.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.intertours.intertoursapp.api.ApiService;
import com.intertours.intertoursapp.api.request.AudioRequest;
import com.intertours.intertoursapp.api.response.CoordenadasDTO;
import com.intertours.intertoursapp.utils.AppConstants;

import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RouteMapsFragment extends Fragment {

    private FloatingActionButton fab;

    private List<CoordenadasDTO> coord;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.API_BASE_URL) // Reemplaza esto con la URL base de tu servidor
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    private ApiService apiService = retrofit.create(ApiService.class);

    private MediaPlayer mediaPlayer;

    private String token;

    public List<CoordenadasDTO> getCoord() {
        return coord;
    }

    public void setCoord(List<CoordenadasDTO> coord) {
        this.coord = coord;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private RouteMapsFragment(){}

    public RouteMapsFragment(List<CoordenadasDTO> coord){
        this.coord = coord;
    }


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            // Check and request location permissions if not granted
            if (ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        requireActivity(),
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        LOCATION_PERMISSION_REQUEST_CODE
                );
                return;
            }
            // Enable my location layer and set a button to move the camera to the current location
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            getCoord().forEach(coordenada -> {
                LatLng aux = new LatLng(coordenada.getLatitud(), coordenada.getLongitud());

                if(coordenada.getOrden() == 1){
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((aux), 12.0f));
                    googleMap.addMarker(new MarkerOptions().position(aux).title(coordenada.getOrden()+ ". " + coordenada.getNombreParada()).snippet(coordenada.getDescripcionParada()).icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.baseline_flag_24)));
                }else{
                    googleMap.addMarker(new MarkerOptions().position(aux).title(coordenada.getOrden()+ ". " + coordenada.getNombreParada()).snippet(coordenada.getDescripcionParada()).icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.baseline_place_24)));
                }
            });

            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                    TextView titleTextView = view.findViewById(R.id.info_window_title);
                    TextView snippetTextView = view.findViewById(R.id.info_window_snippet);

                    titleTextView.setText(marker.getTitle());
                    snippetTextView.setText(marker.getSnippet());

                    return view;
                }
            });

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    marker.showInfoWindow();
                    CoordenadasDTO selectedCoord = coord.stream()
                            .filter(coord -> coord.getLatitud() == marker.getPosition().latitude && coord.getLongitud() == marker.getPosition().longitude)
                            .findFirst()
                            .orElse(null);

                        if(selectedCoord.getAudio() != null && !selectedCoord.getAudio().isEmpty()){
                            fab.show();
                        }

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Call<ResponseBody> call = apiService.getAudio(selectedCoord.getIdRuta(), selectedCoord.getOrden(), "jwt=" + token);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        try {
                                            byte[] audioBytes = response.body().bytes();
                                            if (audioBytes.length > 0) {
                                                // Detener la reproducción anterior si ya estaba en marcha
                                                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                                    mediaPlayer.stop();
                                                    mediaPlayer.release();
                                                    mediaPlayer = null;
                                                }

                                                // Crear un archivo temporal para el audio
                                                File tempAudioFile = File.createTempFile("temp_audio", ".mp3", getActivity().getCacheDir());
                                                FileOutputStream fos = new FileOutputStream(tempAudioFile);
                                                fos.write(audioBytes);
                                                fos.close();

                                                // Crear el nuevo MediaPlayer y reproducir el audio desde el archivo temporal
                                                mediaPlayer = new MediaPlayer();
                                                mediaPlayer.setDataSource(tempAudioFile.getAbsolutePath());
                                                mediaPlayer.prepare();
                                                Log.d(TAG, "MediaPlayer isPlaying: " + mediaPlayer.isPlaying());
                                                mediaPlayer.start();
                                                Log.d(TAG, "MediaPlayer isPlaying: " + mediaPlayer.isPlaying());
                                            } else {
                                                // El audio está vacío
                                                Toast.makeText(getContext(), "El audio está vacío.", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            // Error al leer el audio
                                            Toast.makeText(getContext(), "Error al leer el audio.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // Respuesta no exitosa
                                        Toast.makeText(getContext(), "Error en la respuesta del servidor.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    // Error en la solicitud
                                    Toast.makeText(getContext(), "Error en la solicitud.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    return false;
                }
            });

            googleMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
                @Override
                public void onInfoWindowClose(Marker marker) {
                    marker.hideInfoWindow();
                    fab.hide();
                }
            });

        }


        private BitmapDescriptor
        BitmapFromVector(Context context, int vectorResId)
        {
            Drawable vectorDrawable = ContextCompat.getDrawable(
                    context, vectorResId);

            vectorDrawable.setBounds(
                    0, 0, vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight());

            Bitmap bitmap = Bitmap.createBitmap(
                    vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);

            vectorDrawable.draw(canvas);

            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        token = this.getArguments().getString("jwt");
        fab = (FloatingActionButton) view.findViewById(R.id.soundFAB);
        fab.hide();
                SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

}