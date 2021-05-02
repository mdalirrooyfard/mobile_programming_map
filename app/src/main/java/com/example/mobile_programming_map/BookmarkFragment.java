package com.example.mobile_programming_map;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {
    private MainActivity activity;

    public BookmarkFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bookmark, container, false);
        Cursor data = activity.mydb.getAllData();
        ArrayList<String> listData = new ArrayList<>();
        ArrayList<String> list_loc = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        while(data.moveToNext()){
            ids.add(data.getString(0));
            listData.add(data.getString(1));
            list_loc.add(data.getString(2) + " , " + data.getString(3));
        }
        MyListAdapter adapter = new MyListAdapter(activity, listData, list_loc);
        Log.i("ada", "onCreateView: " + adapter.getCount());
        ListView mListView = (ListView) rootView.findViewById(R.id.listView1);
        mListView.setAdapter(adapter);

        //View listview = inflater.inflate(R.layout.my_list_view, null,true)
        return rootView;
    }
}