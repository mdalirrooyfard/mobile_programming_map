package com.example.mobile_programming_map;

import android.app.Activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList maintitle;
    private final ArrayList subtitle;
//    private final Integer[] imgid;

    public MyListAdapter(Activity context, ArrayList maintitle, ArrayList subtitle) {
        super(context, R.layout.my_list_view, maintitle);
        this.context=context;
        this.maintitle=maintitle;
        this.subtitle=subtitle;
//        this.imgid=imgid;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.my_list_view, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        titleText.setText(maintitle.get(position).toString());
        subtitleText.setText(subtitle.get(position).toString());

        return rowView;

    };
}