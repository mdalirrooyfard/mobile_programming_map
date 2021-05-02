package com.example.mobile_programming_map;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_programming_map.R;

public class PinViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView titleText, subtitleText;

    public PinViewHolder(@NonNull View itemView) {
        super(itemView);
        titleText = (TextView) itemView.findViewById(R.id.title);
        subtitleText = (TextView) itemView.findViewById(R.id.subtitle);
        imageView = (ImageView) itemView.findViewById(R.id.icon);

    }


}
