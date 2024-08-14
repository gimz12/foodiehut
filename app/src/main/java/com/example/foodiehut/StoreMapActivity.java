package com.example.foodiehut;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class StoreMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private Button mapTypeButton;
    private Button directionsButton;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LatLng userLocation;
    private LatLng nearestStore;

    private LatLng[] stores = {
            new LatLng(6.9271, 79.8612), // Colombo
            new LatLng(7.2906, 80.6337), // Kandy
            new LatLng(6.0535, 80.2210), // Galle
            new LatLng(9.6615, 80.0255), // Jaffna
            new LatLng(7.2008, 79.8737)  // Negombo
    };

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_map);

        mapView = findViewById(R.id.mapView);
        mapTypeButton = findViewById(R.id.mapTypeButton);
        directionsButton = findViewById(R.id.directionsButton);
        directionsButton.setEnabled(false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mapTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });

        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDirectionsToNearestStore(nearestStore);
            }
        });

        startLocationUpdates(); // Start location updates
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000); // 5 seconds interval
        locationRequest.setFastestInterval(2000); // 2 seconds fastest interval
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    // Find the nearest store based on the updated location
                    nearestStore = findNearestStore(userLocation, stores);

                    // Update the map to show the nearest store
                    updateMap(nearestStore);

                    // Enable the directions button after finding the nearest store
                    if (nearestStore != null) {
                        directionsButton.setEnabled(true);
                    }
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add store locations
        for (LatLng store : stores) {
            mMap.addMarker(new MarkerOptions().position(store).title("Store in " + store.toString()));
        }

        // Default camera position
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(6.9271, 79.8612), 8));
    }

    private LatLng findNearestStore(LatLng userLocation, LatLng[] stores) {
        LatLng nearestStore = null;
        float minDistance = Float.MAX_VALUE;

        for (LatLng store : stores) {
            float[] results = new float[1];
            Location.distanceBetween(userLocation.latitude, userLocation.longitude, store.latitude, store.longitude, results);
            float distance = results[0];

            if (distance < minDistance) {
                minDistance = distance;
                nearestStore = store;
            }
        }

        return nearestStore;
    }

    private void updateMap(LatLng nearestStore) {
        if (userLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));
            if (nearestStore != null) {
                mMap.clear();
                for (LatLng store : stores) {
                    mMap.addMarker(new MarkerOptions().position(store).title("Store in " + store.toString()));
                }
                mMap.addMarker(new MarkerOptions().position(nearestStore).title("Nearest Store"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(nearestStore, 12));
            }
        }
    }

    private void getDirectionsToNearestStore(LatLng nearestStore) {
        if (nearestStore != null && userLocation != null) {
            String uri = "http://maps.google.com/maps?saddr=" + userLocation.latitude + "," + userLocation.longitude + "&daddr=" + nearestStore.latitude + "," + nearestStore.longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        startLocationUpdates(); // Resume location updates
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback); // Stop location updates
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        fusedLocationClient.removeLocationUpdates(locationCallback); // Clean up location updates
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
