package com.mobdeve.s16.druzali.shawn.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    TextView tvWelcome;
    Button btnDirectory, btnMapSearch;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_home);
        tvWelcome = findViewById(R.id.tvWelcome);
        btnDirectory = findViewById(R.id.btnDirectory);
        btnMapSearch = findViewById(R.id.btnMapSearch);

        String email = getIntent().getStringExtra("userEmail");
        tvWelcome.setText("Welcome, " + (email == null ? "user" : email));

        btnDirectory.setOnClickListener(v -> startActivity(new Intent(this, DirectoryActivity.class)));
        btnMapSearch.setOnClickListener(v -> startActivity(new Intent(this, MapSearchActivity.class)));
    }
}
