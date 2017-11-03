package com.upiita.witcom2016;

import android.graphics.PointF;

/**
 * Created by Edgar on 02/11/2017.
 */

public class Coordinate extends PointF {
    public double latitude;
    public double longitude;

    public Coordinate(double latitude, double longitude){
        this.set((float) latitude,(float) longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double Length(){
        double l = (double)this.length();
        return l;
    }

    public final void set(double latitude, double longitude){
        this.set((float) latitude,(float) longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
