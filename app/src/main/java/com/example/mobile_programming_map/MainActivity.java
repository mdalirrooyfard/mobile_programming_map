package com.example.mobile_programming_map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class MainActivity extends AppCompatActivity {
    ThreadPoolExecutor executor;
    private Boolean isFirstPageLoading = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!checkConnection()){
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            return;
            CharSequence message = "Internet is not connected.";
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            toast.show();
        }
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.glide_custom_view_target_tag);
        swipeRefreshLayout.setRefreshing(true);
        executor = MySingleTone.getThreadPool();
        Handler handler = new Handler(Looper.getMainLooper());
        Context context = getApplicationContext();
        ArrayList<CoinModel> coins =  new ArrayList<>();

        final Boolean[] isLoading = {Boolean.FALSE};
        Integer[] start = (Integer[]) getIntent().getSerializableExtra("Start");
        Integer[] limit = null;
        if(start==null){
            start = new Integer[]{1};
            limit = new Integer[]{10};

        }else{
            limit = new Integer[]{start[0]-1};
            start = new Integer[]{1};
        }
        LoadCoins l = new LoadCoins(MainActivity.this, start, limit, coins, isLoading, context);
        try {
            l.setUiForLoading();
            isLoading[0] = true;
            executor.execute(new RunnableTask<R>(handler, l));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Button loadMore = findViewById(R.id.glide_custom_view_target_tag);


        ArrayList<CoinModel> finalCoins = coins;
        Integer[] finalStart = start;
        Integer[] finalLimit = limit;
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("load more","CLICKED");
                if(!isLoading[0]){
                    Log.i("loading","is not loading");
                    SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.glide_custom_view_target_tag);
                    swipeRefreshLayout.setRefreshing(true);
                    LoadCoins l = new LoadCoins(MainActivity.this, finalStart, finalLimit, finalCoins, isLoading, context);
                    try {
                        l.setUiForLoading();
                        isLoading[0] = true;
                        executor.execute(new RunnableTask<R>(handler, l));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Log.i("loading","Hoooooooo");
                    CharSequence message = "Please wait for full loading.";
                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

    }

    public void startLoadingFirstPage(){
        this.isFirstPageLoading = true;
    }

    public void endLoadingFirstPage(){
        this.isFirstPageLoading = false;
    }

    public Boolean getIsFirstPageLoading(){
        return this.isFirstPageLoading;
    }
    public boolean checkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}