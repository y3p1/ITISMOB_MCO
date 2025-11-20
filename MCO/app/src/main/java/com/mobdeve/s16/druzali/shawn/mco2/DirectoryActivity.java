package com.mobdeve.s16.druzali.shawn.mco2;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DirectoryActivity extends AppCompatActivity {
    RecyclerView rvOffices;
    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_directory);
        rvOffices = findViewById(R.id.rvOffices);

        ArrayList<Office> list = new ArrayList<>();
        // Dummy data (Quezon City examples)
        list.add(new Office("Barangay Hall — Project 2", "Project 2, Quezon City", 14.6760, 121.0437));
        list.add(new Office("QC Health Center — Central", "Central, Quezon City", 14.6514, 121.0411));
        list.add(new Office("City Hall 1", "Elliptical Rd, Diliman", 14.6456, 121.0725));

        OfficeAdapter adapter = new OfficeAdapter(list, office -> {
            Toast.makeText(this, "Selected: " + office.name, Toast.LENGTH_SHORT).show();
            // In Phase 3: navigate to details or pre-fill email form
        });

        rvOffices.setLayoutManager(new LinearLayoutManager(this));
        rvOffices.setAdapter(adapter);
    }
}