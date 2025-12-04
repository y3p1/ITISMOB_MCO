package com.mobdeve.s16.druzali.shawn.mco2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LetterTemplateAdapter extends RecyclerView.Adapter<LetterTemplateAdapter.TemplateViewHolder> {

    private List<LetterTemplate> templateList;
    private OnTemplateClickListener listener;

    public interface OnTemplateClickListener {
        void onTemplateClick(LetterTemplate template);
    }

    public LetterTemplateAdapter(List<LetterTemplate> templateList, OnTemplateClickListener listener) {
        this.templateList = templateList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_template, parent, false);
        return new TemplateViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder holder, int position) {
        LetterTemplate template = templateList.get(position);
        holder.tvTemplateName.setText(template.getName());
        holder.tvTemplateDescription.setText(template.getDescription());
        holder.tvFileSize.setText(template.getFormattedSize());

        int iconRes = getIconForFileType(template.getFileType());
        holder.ivFileIcon.setImageResource(iconRes);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTemplateClick(template);
            }
        });
    }

    @Override
    public int getItemCount() {
        return templateList.size();
    }

    private int getIconForFileType(String fileType) {
        switch (fileType) {
            case "pdf":
                return android.R.drawable.ic_menu_report_image;
            case "docx":
                return android.R.drawable.ic_menu_edit;
            case "image":
                return android.R.drawable.ic_menu_gallery;
            default:
                return android.R.drawable.ic_menu_info_details;
        }
    }

    static class TemplateViewHolder extends RecyclerView.ViewHolder {
        TextView tvTemplateName, tvTemplateDescription, tvFileSize;
        ImageView ivFileIcon;

        TemplateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTemplateName = itemView.findViewById(R.id.tvTemplateName);
            tvTemplateDescription = itemView.findViewById(R.id.tvTemplateDescription);
            tvFileSize = itemView.findViewById(R.id.tvFileSize);
            ivFileIcon = itemView.findViewById(R.id.ivFileIcon);
        }
    }
}