/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String SAMPLE_URL= "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2015-01-01&endtime=2015-12-02&minfelt=500&minmagnitude=7";
    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";   //TODO: Figure out how to use http and follow through the redirection (Code will be 301, not 200)

    EarthquakeAdapter adaptor;
    ListView earthquakeListView;
    TextView emptyView;
    ProgressBar loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Check Connectivity first
        ConnectivityManager cm = (ConnectivityManager)getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);       // Should it be get Application Context or getBaseContext, both work when I try
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        Log.e(LOG_TAG, "Connection status is " + isConnected);

        // Try to fetch data only if there's a connection
        if (isConnected) {

            getLoaderManager().initLoader(0, null, this);
            //Log.e(LOG_TAG, "We now called initLoader");
        }

        else{
            TextView emptyListView = (TextView) findViewById(R.id.emptyTextView);
            emptyListView.setText("No Internet Connection");
        }

    }


    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(getString(R.string.settings_min_magnitude_key), getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        String numberOfResults = sharedPrefs.getString(getString(R.string.settings_num_of_results_key), getString(R.string.settings_num_of_results_default));

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon()
                .appendQueryParameter("format", "geojson")
                .appendQueryParameter("limit", numberOfResults)
                .appendQueryParameter("minmag", minMagnitude)
                .appendQueryParameter("orderby", orderBy);

        Log.e(LOG_TAG, "The url is " + uriBuilder);

        //Log.e(LOG_TAG, "We now called onCreateLoader");
        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        loadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);
        loadingSpinner.setVisibility(GONE);


        emptyView = (TextView) findViewById(R.id.emptyTextView);
        emptyView.setText("No earthquakes found");
        //Log.e(LOG_TAG, "We now called onLoadFinished");
        if (earthquakes == null)
            return;

        updateUi(earthquakes);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        //Log.e(LOG_TAG, "We now called onLoaderReset");
        //TODO: Figure out what to put here, if anything?
    }

    private void updateUi(List<Earthquake> earthquakes){

        // created another array list so it can be declared final and called from an inner class
        final List<Earthquake> quakes = earthquakes;


        adaptor = new EarthquakeAdapter(this, quakes);

        earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setAdapter(adaptor);

        //emptyView = (TextView) findViewById(R.id.emptyTextView);
        earthquakeListView.setEmptyView(emptyView);


        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Earthquake quake = quakes.get(i);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(quake.getUrl()));
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {                        // these would match those defined in the menu/main.xml item
            Intent settingsIntent = new Intent(this, SettingsActivity.class);            // open the settings activity
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
