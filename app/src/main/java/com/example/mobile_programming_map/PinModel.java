package com.example.mobile_programming_map;

public class PinModel {
    public String id ;
    public String maintitle ;
    public String location;

    public PinModel(){
    }

    public PinModel(String id, String title, String location) {
        this.id = id;
        this.maintitle = title;
        this.location = location;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaintitle() {
        return maintitle;
    }

    public void setMaintitle(String name) {
        this.maintitle = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
