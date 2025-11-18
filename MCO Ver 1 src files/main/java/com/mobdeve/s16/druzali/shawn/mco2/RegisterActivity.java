package com.mobdeve.s16.druzali.shawn.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText etName, etEmail, etPassword;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_register);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etRegEmail);
        etPassword = findViewById(R.id.etRegPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> attemptRegister());
    }

    private void attemptRegister() {
        String name = etName.getText()==null? "": etName.getText().toString().trim();
        String email = etEmail.getText()==null? "": etEmail.getText().toString().trim();
        String pass = etPassword.getText()==null? "": etPassword.getText().toString();

        if (TextUtils.isEmpty(name)) { etName.setError("Enter name"); return; }
        if (TextUtils.isEmpty(email)) { etEmail.setError("Enter email"); return; }
        if (TextUtils.isEmpty(pass) || pass.length() < 6) { etPassword.setError("Password >= 6"); return; }

        // Phase 2: simulate account creation
        Toast.makeText(this, "Registered (prototype). Logging in...", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra("userEmail", email);
        startActivity(i);
        finish();

        // Phase 3: replace with FirebaseAuth.getInstance().createUserWithEmailAndPassword(...)
    }
}
