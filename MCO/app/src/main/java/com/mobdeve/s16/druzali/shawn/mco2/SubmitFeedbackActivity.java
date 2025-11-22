package com.mobdeve.s16.druzali.shawn.mco2;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SubmitFeedbackActivity extends AppCompatActivity {
    private EditText feedbackPT;
    private Button feedbackBtn;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_feedback);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        feedbackPT = findViewById(R.id.feedbackPT);
        feedbackBtn = findViewById(R.id.feedbackBtn);

        feedbackBtn.setOnClickListener(v -> submitFeedback());
    }

    private void submitFeedback() {
        String feedback = feedbackPT.getText().toString().trim();

        if (TextUtils.isEmpty(feedback)) {
            feedbackPT.setError("Please enter feedback");
            return;
        }

        String userId = mAuth.getCurrentUser() != null ?
                mAuth.getCurrentUser().getUid() : "anonymous";
        String userEmail = mAuth.getCurrentUser() != null ?
                mAuth.getCurrentUser().getEmail() : "anonymous";

        Map<String, Object> feedbackData = new HashMap<>();
        feedbackData.put("userId", userId);
        feedbackData.put("userEmail", userEmail);
        feedbackData.put("feedback", feedback);
        feedbackData.put("timestamp", System.currentTimeMillis());
        feedbackData.put("status", "pending");

        db.collection("feedback")
                .add(feedbackData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Feedback submitted successfully!",
                            Toast.LENGTH_SHORT).show();
                    feedbackPT.setText("");
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to submit: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }
}


