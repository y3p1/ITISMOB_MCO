package com.mobdeve.s16.druzali.shawn.mco2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MapSearchActivity extends AppCompatActivity implements OnMapReadyCallback {

    RecyclerView rvNearby;
    Button btnLocate;
    GoogleMap mMap;
    ArrayList<Office> offices = new ArrayList<>();
    ActivityResultLauncher<String> requestPermissionLauncher;
    FusedLocationProviderClient fusedLocationClient;
    FirebaseFirestore db;
    Location currentLocation;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_map_search);

        db = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        rvNearby = findViewById(R.id.rvNearby);
        btnLocate = findViewById(R.id.btnLocate);

        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        loadOfficesFromFirestore();

        // Permission launcher
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), result -> {
                    if (result) {
                        getUserLocationAndSort();
                    } else {
                        Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                    }
                });

        btnLocate.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                getUserLocationAndSort();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Default camera to Quezon City
        LatLng quezonCity = new LatLng(14.6760, 121.0437);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(quezonCity, 12));

        // Enable location button if permission granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        // Add markers for offices
        addMarkersToMap();
    }

    private void loadOfficesFromFirestore() {
        db.collection("offices")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        offices.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String name = doc.getString("name");
                            String address = doc.getString("address");
                            Double lat = doc.getDouble("lat");
                            Double lng = doc.getDouble("lng");

                            if (name != null && address != null && lat != null && lng != null) {
                                offices.add(new Office(name, address, lat, lng));
                            }
                        }

                        if (offices.isEmpty()) {
                            populateDummyOffices();
                        }

                        addMarkersToMap();
                        showOffices(offices);
                    } else {
                        populateDummyOffices();
                        addMarkersToMap();
                        showOffices(offices);
                    }
                });
    }

    private void populateDummyOffices() {
        offices.clear();
        offices.add(new Office("Barangay Hall — Project 2", "Project 2, QC", 14.6760, 121.0437));
        offices.add(new Office("QC Health Center — Central", "Central, QC", 14.6514, 121.0411));
        offices.add(new Office("City Hall 1", "Elliptical Rd", 14.6456, 121.0725));
        offices.add(new Office("Barangay Hall — Batasan", "Batasan Hills", 14.6616, 121.0702));
    }

    private void addMarkersToMap() {
        if (mMap != null && !offices.isEmpty()) {
            mMap.clear();
            for (Office office : offices) {
                LatLng position = new LatLng(office.lat, office.lng);
                mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(office.name)
                        .snippet(office.address));
            }
        }
    }

    private void getUserLocationAndSort() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLocation = location;

                        // Move camera to user location
                        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14));

                        // Sort offices by distance
                        Collections.sort(offices, new Comparator<Office>() {
                            @Override
                            public int compare(Office o1, Office o2) {
                                float[] r1 = new float[1], r2 = new float[1];
                                Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                                        o1.lat, o1.lng, r1);
                                Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                                        o2.lat, o2.lng, r2);
                                return Float.compare(r1[0], r2[0]);
                            }
                        });

                        Toast.makeText(this, "Showing nearest offices", Toast.LENGTH_SHORT).show();
                        showOffices(offices);
                    } else {
                        Toast.makeText(this, "Unable to get location. Please try again.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error getting location: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }

    private void showOffices(ArrayList<Office> list) {
        OfficeAdapter adp = new OfficeAdapter(list, office -> {
            Toast.makeText(MapSearchActivity.this, "Selected: " + office.name,
                    Toast.LENGTH_SHORT).show();

            // Move camera to selected office
            if (mMap != null) {
                LatLng officePos = new LatLng(office.lat, office.lng);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(officePos, 16));
            }
        });
        rvNearby.setLayoutManager(new LinearLayoutManager(this));
        rvNearby.setAdapter(adp);
    }
}