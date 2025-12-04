package com.mobdeve.s16.druzali.shawn.mco2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OfficeAdapter extends RecyclerView.Adapter<OfficeAdapter.OfficeViewHolder> {

    private List<Office> officeList;
    private OnOfficeClickListener listener;

    public interface OnOfficeClickListener {
        void onOfficeClick(Office office);
    }

    public OfficeAdapter(List<Office> officeList, OnOfficeClickListener listener) {
        this.officeList = officeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OfficeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_office, parent, false);
        return new OfficeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficeViewHolder holder, int position) {
        Office o = officeList.get(position);
        holder.tvOfficeName.setText(o.getName());
        holder.tvOfficeAddress.setText(o.getAddress());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOfficeClick(o);
            }
        });
    }

    @Override
    public int getItemCount() {
        return officeList.size();
    }

    static class OfficeViewHolder extends RecyclerView.ViewHolder {
        TextView tvOfficeName, tvOfficeAddress;

        OfficeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOfficeName = itemView.findViewById(R.id.tvOfficeName);
            tvOfficeAddress = itemView.findViewById(R.id.tvOfficeAddress);
        }
    }
}