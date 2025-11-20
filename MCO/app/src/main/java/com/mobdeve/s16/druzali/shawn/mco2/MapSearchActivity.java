package com.mobdeve.s16.druzali.shawn.mco2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MapSearchActivity extends AppCompatActivity {
    RecyclerView rvNearby;
    Button btnLocate;
    ArrayList<Office> offices = new ArrayList<>();

    ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_map_search);
        rvNearby = findViewById(R.id.rvNearby);
        btnLocate = findViewById(R.id.btnLocate);

        populateDummyOffices();

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    simulateLocateAndShowNearby();
                } else {
                    Toast.makeText(MapSearchActivity.this, "Location permission denied. Showing default list ordered by proximity (mock).", Toast.LENGTH_SHORT).show();
                    showOffices(offices); // fallback
                }
            }
        });

        btnLocate.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                simulateLocateAndShowNearby();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        });

        // initial list
        showOffices(offices);
    }

    private void populateDummyOffices() {
        offices.clear();
        offices.add(new Office("Barangay Hall — Project 2", "Project 2, QC", 14.6760, 121.0437));
        offices.add(new Office("QC Health Center — Central", "Central, QC", 14.6514, 121.0411));
        offices.add(new Office("City Hall 1", "Elliptical Rd", 14.6456, 121.0725));
        offices.add(new Office("Barangay Hall — Batasan", "Batasan Hills", 14.6616, 121.0702));
    }

    // For Phase 2 we simulate a user location (e.g., somewhere in QC). Phase 3: use FusedLocationProviderClient
    private void simulateLocateAndShowNearby() {
        // fake current location — e.g., 14.6570, 121.0290 (Quezon City)
        double userLat = 14.6570, userLng = 121.0290;

        // compute simple straight-line distances and sort
        Collections.sort(offices, new Comparator<Office>() {
            @Override
            public int compare(Office o1, Office o2) {
                float[] r1 = new float[1], r2 = new float[1];
                Location.distanceBetween(userLat, userLng, o1.lat, o1.lng, r1);
                Location.distanceBetween(userLat, userLng, o2.lat, o2.lng, r2);
                return Float.compare(r1[0], r2[0]);
            }
        });

        Toast.makeText(this, "Location acquired (simulated). Showing nearest offices.", Toast.LENGTH_SHORT).show();
        showOffices(offices);
    }

    private void showOffices(ArrayList<Office> list) {
        OfficeAdapter adp = new OfficeAdapter(list, office -> {
            Toast.makeText(MapSearchActivity.this, "Selected: " + office.name, Toast.LENGTH_SHORT).show();
        });
        rvNearby.setLayoutManager(new LinearLayoutManager(this));
        rvNearby.setAdapter(adp);
    }
}
