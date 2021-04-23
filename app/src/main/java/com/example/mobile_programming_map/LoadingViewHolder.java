package com.example.mobile_programming_map;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = (ProgressBar)itemView.findViewById(R.id.glide_custom_view_target_tag);

    }
}
