package com.example.mobile_programming_map;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Formatter;

public class MyListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final MainActivity context;
    private final ArrayList maintitle;
    private final ArrayList subtitle;
    private final ArrayList ids;
    private BookmarkFragment bf;
//    private final Integer[] imgid;

    public MyListAdapter(MainActivity context, BookmarkFragment bf, ArrayList maintitle, ArrayList subtitle, ArrayList ids) {

        this.context=context;
        this.maintitle=maintitle;
        this.subtitle=subtitle;
        this.ids = ids;
        this.bf = bf;
//        this.imgid=imgid;

    }



    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.my_list_view, parent,false);

        return new PinViewHolder(rowView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((PinViewHolder) holder).titleText.setText(maintitle.get(position).toString());
        ((PinViewHolder) holder).subtitleText.setText(subtitle.get(position).toString());
        AppCompatImageView trash = holder.itemView.findViewById(R.id.bin);
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt((String) ids.get(position));
                context.mydb.deleteLocation(id);
                bf.updateData();
            }
        });

    }

    @Override
    public int getItemCount() {
        return ids.size();
    }
}