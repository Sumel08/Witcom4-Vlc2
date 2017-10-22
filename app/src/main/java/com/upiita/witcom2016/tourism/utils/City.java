package com.upiita.witcom2016.tourism.utils;

/**
 * Created by oscar on 13/09/16.
 */
public class City {
    private int id;
    private String city;
    private String imageCity;

    public City(int id, String city, String imageCity) {
        this.id = id;
        this.city = city;
        this.imageCity = imageCity;
    }

    public int getId() { return id; }

    public String getCity() {
        return city;
    }

    public String getImageCity() {
        return imageCity;
    }
}