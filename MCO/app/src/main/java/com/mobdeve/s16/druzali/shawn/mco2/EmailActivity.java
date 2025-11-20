package com.mobdeve.s16.druzali.shawn.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EmailActivity extends AppCompatActivity {

    EditText editTo, editSubject, editBody;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        editTo = findViewById(R.id.editTo);
        editSubject = findViewById(R.id.editSubject);
        editBody = findViewById(R.id.editBody);
        btnSend = findViewById(R.id.btnSend);

        String letterContent = getIntent().getStringExtra("letterContent");
        if (letterContent != null) {
            editBody.setText(letterContent);
        }

        btnSend.setOnClickListener(v -> sendEmail());
    }

    private void sendEmail() {
        String to = editTo.getText() == null ? "" : editTo.getText().toString().trim();
        String subject = editSubject.getText() == null ? "" : editSubject.getText().toString().trim();
        String body = editBody.getText() == null ? "" : editBody.getText().toString().trim();

        if (TextUtils.isEmpty(to)) {
            editTo.setError("Enter recipient email");
            return;
        }
        if (TextUtils.isEmpty(subject)) {
            editSubject.setError("Enter subject");
            return;
        }
        if (TextUtils.isEmpty(body)) {
            editBody.setError("Enter message body");
            return;
        }

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            startActivity(Intent.createChooser(emailIntent, "Choose Email App"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }
}