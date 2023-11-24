package com.example.gjaves;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.gjaves.databinding.ActivityLocationViewerBinding;
import com.example.gjaves.databinding.ActivityPredictThenSaveBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class location_viewer extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap googleMap;
    private GoogleMap googleMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location_viewer);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMaps) {

        googleMap = googleMaps;
        LatLng Digos;

        double v1 = 6.757509;
        double v2 = 125.352398;
        Digos = new LatLng( v1,v2);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(Digos)
                .title("Custom Marker Title")
                .snippet("Custom Marker Snippet");

        googleMap.addMarker((markerOptions).position(Digos).title("DIRI KO DAPIT"));
        float zoomlvl = 20.0f;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Digos, zoomlvl));

    }
}