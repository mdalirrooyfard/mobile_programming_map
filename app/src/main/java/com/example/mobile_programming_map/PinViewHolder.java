package com.example.mobile_programming_map;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_programming_map.R;

public class PinViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

    public ImageView imageView;
    public TextView titleText, subtitleText;
    private ItemClickListener mClickListener;

    public PinViewHolder(@NonNull View itemView) {
        super(itemView);
        titleText = (TextView) itemView.findViewById(R.id.title);
        subtitleText = (TextView) itemView.findViewById(R.id.subtitle);
        imageView = (ImageView) itemView.findViewById(R.id.icon);
        itemView.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
