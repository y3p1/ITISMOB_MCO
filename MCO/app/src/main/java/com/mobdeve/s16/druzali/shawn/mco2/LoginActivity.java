package com.mobdeve.s16.druzali.shawn.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegisterLink;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);

        btnLogin.setOnClickListener(v -> attemptLogin());
        tvRegisterLink.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    // Simple client-side validation + simulated auth
    private void attemptLogin() {
        String email = etEmail.getText() == null ? "" : etEmail.getText().toString().trim();
        String pass = etPassword.getText() == null ? "" : etPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter email");
            return;
        }
        if (TextUtils.isEmpty(pass) || pass.length() < 6) {
            etPassword.setError("Password must be at least 6 chars");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        navigateToHome(user != null ? user.getEmail() : email);
                    } else {
                        Toast.makeText(this, "Authentication failed: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void navigateToHome(String email) {
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra("userEmail", email);
        startActivity(i);
        finish();
    }
}