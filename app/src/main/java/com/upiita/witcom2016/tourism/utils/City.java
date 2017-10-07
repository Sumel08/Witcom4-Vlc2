package com.upiita.witcom2016.tourism.utils;

/**
 * Created by oscar on 13/09/16.
 */
public class City {
    private String city;
    private String imageCity;

    public City(String city, String imageCity) {
        this.city = city;
        this.imageCity = imageCity;
    }

    public String getCity() {
        return city;
    }

    public String getImageCity() {
        return imageCity;
    }
}