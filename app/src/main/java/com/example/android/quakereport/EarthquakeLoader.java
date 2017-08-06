package com.example.android.quakereport;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by mahyar on 2017-07-17.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    public final String LOG_TAG = EarthquakeLoader.class.getSimpleName();
    // Create a member of the object to hold the inputURL
    String input;

    public EarthquakeLoader(Context context, String url){
        super(context);
        this.input = url;
    }

    @Override
    protected void onStartLoading(){
        //Log.e(LOG_TAG, "We now called onStartLoading");
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {

        //Log.e(LOG_TAG, "We now called loadInBackground");
        if (input == null)
            return null;

        // return the ArrayList of Earthquakes

        return QueryUtils.extractEarthquakes(QueryUtils.fetchEarthquakeData(input));

    }
}
