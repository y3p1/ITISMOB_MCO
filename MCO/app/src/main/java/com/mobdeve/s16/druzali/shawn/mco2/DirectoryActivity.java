package com.mobdeve.s16.druzali.shawn.mco2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DirectoryActivity extends AppCompatActivity {

    RecyclerView rvOffices;
    OfficeAdapter adapter;
    ArrayList<Office> officeList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        rvOffices = findViewById(R.id.rvOffices);
        rvOffices.setLayoutManager(new LinearLayoutManager(this));

        adapter = new OfficeAdapter(officeList, office -> {
            showOfficeDetailsDialog(office);
        });
        rvOffices.setAdapter(adapter);

        loadOffices();
    }

    private void loadOffices() {
        db.collection("offices")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    officeList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Office office = doc.toObject(Office.class);
                        officeList.add(office);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("FIRESTORE", "Error loading offices", e));
    }

    private void showOfficeDetailsDialog(Office office) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_office_details, null);

        // Find views in dialog
        TextView tvName = dialogView.findViewById(R.id.tvDetailName);
        TextView tvAddress = dialogView.findViewById(R.id.tvDetailAddress);
        TextView tvType = dialogView.findViewById(R.id.tvDetailType);
        TextView tvContact = dialogView.findViewById(R.id.tvDetailContact);
        TextView tvEmail = dialogView.findViewById(R.id.tvDetailEmail);
        Button btnCall = dialogView.findViewById(R.id.btnCall);
        Button btnEmail = dialogView.findViewById(R.id.btnEmail);
        Button btnNavigate = dialogView.findViewById(R.id.btnNavigate);
        Button btnClose = dialogView.findViewById(R.id.btnClose);

        // Set office details
        tvName.setText(office.getName());
        tvAddress.setText(office.getAddress());
        tvType.setText(formatType(office.getType()));
        tvContact.setText(office.getContact() != null ? office.getContact() : "N/A");
        tvEmail.setText(office.getEmail() != null ? office.getEmail() : "N/A");

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Call button
        btnCall.setOnClickListener(v -> {
            if (office.getContact() != null && !office.getContact().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + office.getContact()));
                startActivity(intent);
            }
        });

        // Email button
        btnEmail.setOnClickListener(v -> {
            if (office.getEmail() != null && !office.getEmail().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + office.getEmail()));
                startActivity(intent);
            }
        });

        // Navigate button
        btnNavigate.setOnClickListener(v -> {
            String uri = "google.navigation:q=" + office.getLat() + "," + office.getLng();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                // Fallback to browser if Google Maps not installed
                String browserUri = "https://www.google.com/maps/dir/?api=1&destination=" +
                        office.getLat() + "," + office.getLng();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(browserUri)));
            }
            dialog.dismiss();
        });

        // Close button
        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private String formatType(String type) {
        if (type == null) return "N/A";

        // Convert type to readable format
        switch (type.toLowerCase()) {
            case "city_hall":
                return "City Hall";
            case "barangay_hall":
                return "Barangay Hall";
            case "health_center":
                return "Health Center";
            case "police_station":
                return "Police Station";
            default:
                return type.replace("_", " ").toUpperCase();
        }
    }
}