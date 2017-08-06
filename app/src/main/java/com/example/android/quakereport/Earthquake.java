package com.example.android.quakereport;

import java.text.DecimalFormat;

/**
 * Created by mahyar on 2017-06-09.
 */

public class Earthquake {
    private String mPrimaryLocation;
    private String mLocationOffset;
    private String mTime;
    private String mHour;
    private double mMagnitude;
    private String mUrl;


    public String getPrimaryLocation() {
        return mPrimaryLocation;
    }

    public String getLocationOffset() {
        return mLocationOffset;
    }

    public String getUrl(){
        return mUrl;
    }

    public String getTime() {
        return mTime;
    }

    public String getHour() {
        return mHour;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public void setPrimaryLocation(String city) {
        this.mPrimaryLocation = city;
    }

    public void setLocationOffset(String locationOffset) {
        this.mLocationOffset = locationOffset;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }

    public void setHour(String mHour) {
        this.mHour = mHour;
    }

    public void setMagnitude(double mMagnitude) {
        this.mMagnitude = mMagnitude;
    }

    public void setUrl (String url) { this.mUrl = url; }

    public Earthquake(double magnitude, String city, String locationOffset, String time, String hour){
        mPrimaryLocation = city;
        mLocationOffset = locationOffset;
        mTime = time;
        mMagnitude = magnitude;
        mHour = hour;
    }

    public Earthquake(double magnitude, String city, String locationOffset, String time, String hour, String url){
        mPrimaryLocation = city;
        mLocationOffset = locationOffset;
        mTime = time;
        mMagnitude = magnitude;
        mHour = hour;
        mUrl = url;
    }


}
