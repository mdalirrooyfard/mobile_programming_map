package com.example.mobile_programming_map;

import android.graphics.drawable.Drawable;
import android.support.v4.app.INotificationSideChannel;

import com.bumptech.glide.RequestBuilder;

public class CoinModel {
    public Integer id ;
    public String name ;
    public String symbol;
    public double price ;
    public double percent_change_1h ;
    public double percent_change_24h ;
    public double percent_change_7d ;
    public RequestBuilder<Drawable> icon;

    public RequestBuilder<Drawable> getIcon() {
        return icon;
    }

    public void setIcon(RequestBuilder<Drawable> icon) {
        this.icon = icon;
    }


    public CoinModel(){
    }

    public CoinModel(Integer id, String name, String symbol, double price, double percent_change_1h, double percent_change_24h, double percent_change_7d) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.percent_change_1h = percent_change_1h;
        this.percent_change_24h = percent_change_24h;
        this.percent_change_7d = percent_change_7d;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPercent_change_1h() {
        return percent_change_1h;
    }

    public void setPercent_change_1h(double percent_change_1h) {
        this.percent_change_1h = percent_change_1h;
    }

    public double getPercent_change_24h() {
        return percent_change_24h;
    }

    public void setPercent_change_24h(double percent_change_24h) {
        this.percent_change_24h = percent_change_24h;
    }

    public double getPercent_change_7d() {
        return percent_change_7d;
    }

    public void setPercent_change_7d(double percent_change_7d) {
        this.percent_change_7d = percent_change_7d;
    }
    public String toString(){
        return "This is coin with id: "+ id.toString()+" and name: "+ name;
    }
}
