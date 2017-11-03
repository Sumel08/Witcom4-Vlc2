package com.upiita.witcom2016;

import android.graphics.PointF;

/**
 * Created by Edgar on 02/11/2017.
 */

public class RegionGeofence extends PointF {

    public String id;
    public String name;
    public double latitude;
    public double longitude;
    public int radius;


    public RegionGeofence(String id, String name, double latitude, double longitude, int radius){
        this.set((float) latitude,(float) longitude);
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public double Length(){
        double l = (double)this.length();
        return l;
    }

    public final void set(String id, String name, double latitude, double longitude){
        this.set((float) latitude,(float) longitude);
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

}
