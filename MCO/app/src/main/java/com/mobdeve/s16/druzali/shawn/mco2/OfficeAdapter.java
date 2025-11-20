package com.mobdeve.s16.druzali.shawn.mco2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OfficeAdapter extends RecyclerView.Adapter<OfficeAdapter.VH> {
    List<Office> items;
    OnItemClickListener listener;

    public interface OnItemClickListener { void onClick(Office office); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvAddr;
        VH(View v) { super(v); tvName = v.findViewById(R.id.tvOfficeName); tvAddr = v.findViewById(R.id.tvOfficeAddress); }
    }

    public OfficeAdapter(List<Office> items, OnItemClickListener listener) {
        this.items = items; this.listener = listener;
    }

    @Override public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_office, parent, false);
        return new VH(v);
    }
    @Override public void onBindViewHolder(VH holder, int position) {
        Office o = items.get(position);
        holder.tvName.setText(o.name);
        holder.tvAddr.setText(o.address);
        holder.itemView.setOnClickListener(v -> listener.onClick(o));
    }
    @Override public int getItemCount() { return items.size(); }
}
