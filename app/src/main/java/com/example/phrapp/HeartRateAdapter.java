package com.example.phrapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class HeartRateAdapter extends RecyclerView.Adapter<HeartRateAdapter.ViewHolder> {


    private List<Map<String, String>> mHeartRates;

    public HeartRateAdapter(List<Map<String, String>> heartRates) {
        mHeartRates = heartRates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> heartRate = mHeartRates.get(position);
        holder.mDateTextView.setText(heartRate.get("date"));
        holder.mHeartRateTextView.setText(heartRate.get("heart_rate"));
    }

    @Override
    public int getItemCount() {
        return mHeartRates.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mDateTextView;
        public TextView mHeartRateTextView;

        public ViewHolder(View view) {
            super(view);
            mDateTextView = view.findViewById(R.id.date_text_view);
            mHeartRateTextView = view.findViewById(R.id.heart_rate_text_view);

        }
    }
}

