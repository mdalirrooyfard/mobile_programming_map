package com.example.mobile_programming_map;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private final MainActivity context;
    private final ArrayList<PinModel> pins;
    private ArrayList<PinModel> ppins;
    private BookmarkFragment bf;
//    private final Integer[] imgid;

    public MyListAdapter(MainActivity context, BookmarkFragment bf, ArrayList<PinModel> pins) {

        this.context=context;
        this.pins=pins;
        this.bf = bf;
        this.ppins = new ArrayList<>(pins);

    }



    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.my_list_view, parent,false);

        return new PinViewHolder(rowView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((PinViewHolder) holder).titleText.setText(pins.get(position).getMaintitle().toString());
        ((PinViewHolder) holder).subtitleText.setText(pins.get(position).getLocation().toString());
        AppCompatImageView trash = holder.itemView.findViewById(R.id.bin);
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt((String) pins.get(position).getId());
                context.mydb.deleteLocation(id);
                ArrayList<PinModel> p = new ArrayList<>();
                Cursor data = context.mydb.getAllData();
                while(data.moveToNext()){
                    p.add(new PinModel(data.getString(0),data.getString(1), data.getString(2) + " , " + data.getString(3)));

                }
                pins.clear();
                pins.addAll(p);
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return pins.size();
    }

    @Override
    public Filter getFilter() {
        return locFilter;
    }
    private Filter locFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<PinModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(ppins);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PinModel item : ppins) {
                    if (item.getMaintitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            pins.clear();
            pins.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}