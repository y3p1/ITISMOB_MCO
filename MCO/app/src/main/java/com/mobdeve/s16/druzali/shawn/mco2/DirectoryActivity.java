package com.mobdeve.s16.druzali.shawn.mco2;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

import java.util.ArrayList;

public class DirectoryActivity extends AppCompatActivity {
    RecyclerView rvOffices;
    FirebaseFirestore db;
    ArrayList<Office> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_directory);

        db = FirebaseFirestore.getInstance();
        rvOffices = findViewById(R.id.rvOffices);
        rvOffices.setLayoutManager(new LinearLayoutManager(this));

        loadOfficesFromFirestore();
    }

    private void loadOfficesFromFirestore() {
        db.collection("offices")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        list.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String name = doc.getString("name");
                            String address = doc.getString("address");
                            Double lat = doc.getDouble("lat");
                            Double lng = doc.getDouble("lng");

                            if (name != null && address != null && lat != null && lng != null) {
                                list.add(new Office(name, address, lat, lng));
                            }
                        }

                        if (list.isEmpty()) {
                            // Fallback to dummy data if Firestore is empty
                            addDummyData();
                        }

                        setupAdapter();
                    } else {
                        Toast.makeText(this, "Failed to load offices", Toast.LENGTH_SHORT).show();
                        addDummyData();
                        setupAdapter();
                    }
                });
    }

    private void addDummyData() {
        list.add(new Office("Barangay Hall — Project 2", "Project 2, Quezon City", 14.6760, 121.0437));
        list.add(new Office("QC Health Center — Central", "Central, Quezon City", 14.6514, 121.0411));
        list.add(new Office("City Hall 1", "Elliptical Rd, Diliman", 14.6456, 121.0725));
    }

    private void setupAdapter() {
        OfficeAdapter adapter = new OfficeAdapter(list, office -> {
            Toast.makeText(this, "Selected: " + office.name, Toast.LENGTH_SHORT).show();
        });
        rvOffices.setAdapter(adapter);
    }
}