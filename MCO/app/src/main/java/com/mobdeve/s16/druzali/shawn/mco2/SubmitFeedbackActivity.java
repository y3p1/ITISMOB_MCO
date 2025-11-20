package com.mobdeve.s16.druzali.shawn.mco2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class SubmitFeedbackActivity extends AppCompatActivity {
    private EditText feedbackPT;
    private Button feedbackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_feedback);

        feedbackPT = findViewById(R.id.feedbackPT);
        feedbackBtn = findViewById(R.id.feedbackBtn);

        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // submission feature for later
            }
        });
    }
}

