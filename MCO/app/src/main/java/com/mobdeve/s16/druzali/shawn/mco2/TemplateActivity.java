package com.mobdeve.s16.druzali.shawn.mco2;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class TemplateActivity extends AppCompatActivity {

    RecyclerView rvTemplates;
    ProgressBar progressBar;
    LetterTemplateAdapter adapter;
    ArrayList<LetterTemplate> templateList = new ArrayList<>();
    FirebaseStorage storage;
    StorageReference storageRef;

    private static final String TAG = "TemplateActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("letter_templates");

        rvTemplates = findViewById(R.id.rvTemplates);
        progressBar = findViewById(R.id.progressBar);

        rvTemplates.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LetterTemplateAdapter(templateList, template -> {
            showTemplateOptionsDialog(template);
        });
        rvTemplates.setAdapter(adapter);

        loadTemplatesFromStorage();
    }

    private void loadTemplatesFromStorage() {
        progressBar.setVisibility(View.VISIBLE);
        templateList.clear();

        storageRef.listAll()
                .addOnSuccessListener(listResult -> {
                    if (listResult.getItems().isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        // Show dummy templates for testing
                        loadDummyTemplates();
                        return;
                    }

                    int totalItems = listResult.getItems().size();
                    final int[] loadedItems = {0};

                    for (StorageReference item : listResult.getItems()) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            item.getMetadata().addOnSuccessListener(metadata -> {
                                String fileName = item.getName();
                                String fileType = getFileType(fileName);
                                String displayName = getDisplayName(fileName);
                                long fileSize = metadata.getSizeBytes();

                                LetterTemplate template = new LetterTemplate(
                                        displayName,
                                        fileName,
                                        uri.toString(),
                                        getDescription(fileName),
                                        fileType,
                                        fileSize
                                );

                                templateList.add(template);
                                loadedItems[0]++;

                                if (loadedItems[0] == totalItems) {
                                    adapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }).addOnFailureListener(e -> {
                            Log.e(TAG, "Error getting download URL: " + e.getMessage());
                            loadedItems[0]++;
                            if (loadedItems[0] == totalItems) {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    // If Firebase Storage fails, show dummy data
                    loadDummyTemplates();
                    Log.e(TAG, "Error listing templates", e);
                });
    }

    private void loadDummyTemplates() {
        // Add dummy templates for testing when Firebase Storage is empty
        templateList.clear();

        templateList.add(new LetterTemplate(
                "Medical Assistance",
                "medical_assistance.pdf",
                "https://example.com/dummy",
                "Template for medical assistance guarantee letters",
                "pdf",
                150000
        ));

        templateList.add(new LetterTemplate(
                "Burial Assistance",
                "burial_assistance.pdf",
                "https://example.com/dummy",
                "Template for burial assistance guarantee letters",
                "pdf",
                120000
        ));

        templateList.add(new LetterTemplate(
                "Certificate Request",
                "certificate_request.pdf",
                "https://example.com/dummy",
                "Template for certificate request letters",
                "pdf",
                95000
        ));

        templateList.add(new LetterTemplate(
                "Financial Assistance",
                "financial_assistance.pdf",
                "https://example.com/dummy",
                "Template for financial assistance guarantee letters",
                "pdf",
                180000
        ));

        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Showing sample templates (Upload files to Firebase Storage)",
                Toast.LENGTH_LONG).show();
    }

    private void showTemplateOptionsDialog(LetterTemplate template) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(template.getName())
                .setMessage(template.getDescription() + "\n\nFile Size: " + template.getFormattedSize())
                .setPositiveButton("View", (dialog, which) -> {
                    viewTemplate(template);
                })
                .setNegativeButton("Download", (dialog, which) -> {
                    downloadTemplate(template);
                })
                .setNeutralButton("Cancel", null)
                .show();
    }

    private void viewTemplate(LetterTemplate template) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(template.getFileUrl()), getMimeType(template.getFileType()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No app found to view this file. Try downloading instead.",
                    Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error viewing template", e);
        }
    }

    private void downloadTemplate(LetterTemplate template) {
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(template.getFileUrl()));
            request.setTitle(template.getName());
            request.setDescription("Downloading guarantee letter template");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, template.getFileName());

            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            if (manager != null) {
                manager.enqueue(request);
                Toast.makeText(this, "Downloading " + template.getName(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error downloading file: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error downloading template", e);
        }
    }

    private String getFileType(String fileName) {
        if (fileName.toLowerCase().endsWith(".pdf")) return "pdf";
        if (fileName.toLowerCase().endsWith(".docx") || fileName.toLowerCase().endsWith(".doc")) return "docx";
        if (fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".jpg") ||
                fileName.toLowerCase().endsWith(".jpeg")) return "image";
        return "other";
    }

    private String getMimeType(String fileType) {
        switch (fileType) {
            case "pdf":
                return "application/pdf";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "image":
                return "image/*";
            default:
                return "*/*";
        }
    }

    private String getDisplayName(String fileName) {
        String name = fileName.replace("_", " ").replaceAll("\\.[^.]+$", "");
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private String getDescription(String fileName) {
        String lowerName = fileName.toLowerCase();
        if (lowerName.contains("medical")) {
            return "Template for medical assistance guarantee letters";
        } else if (lowerName.contains("burial")) {
            return "Template for burial assistance guarantee letters";
        } else if (lowerName.contains("certificate")) {
            return "Template for certificate request letters";
        } else if (lowerName.contains("financial")) {
            return "Template for financial assistance guarantee letters";
        }
        return "Guarantee letter template";
    }
}