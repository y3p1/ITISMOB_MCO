package com.mobdeve.s16.druzali.shawn.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    TextView tvWelcome;
    Button btnDirectory, btnMapSearch, btnEmail, btnTemplates, btnFeedback, btnLogout;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        // Initialize notification channel
       // NotificationHelper.createNotificationChannel(this);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnDirectory = findViewById(R.id.btnDirectory);
        btnMapSearch = findViewById(R.id.btnMapSearch);
        btnEmail = findViewById(R.id.btnEmail);
        btnTemplates = findViewById(R.id.btnTemplates);
        btnFeedback = findViewById(R.id.btnFeedback);
        btnLogout = findViewById(R.id.btnLogout);

        String email = getIntent().getStringExtra("userEmail");
        if (email == null && mAuth.getCurrentUser() != null) {
            email = mAuth.getCurrentUser().getEmail();
        }
        tvWelcome.setText("Welcome, " + (email == null ? "user" : email));

        btnDirectory.setOnClickListener(v -> startActivity(new Intent(this, DirectoryActivity.class)));
        btnMapSearch.setOnClickListener(v -> startActivity(new Intent(this, MapSearchActivity.class)));
        btnEmail.setOnClickListener(v -> startActivity(new Intent(this, EmailActivity.class)));
        btnTemplates.setOnClickListener(v -> startActivity(new Intent(this, TemplateActivity.class)));
        btnFeedback.setOnClickListener(v -> startActivity(new Intent(this, SubmitFeedbackActivity.class)));
        btnLogout.setOnClickListener(v -> logout());
    }

    private void logout() {
        mAuth.signOut();
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}
