package com.example.android.quakereport;

import android.app.DownloadManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mahyar on 2017-06-15.
 */

public class QueryUtils {

    private QueryUtils(){

    }

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Query the USGS dataset and return an JSON String object to represent a single earthquake.
     */
    public static String fetchEarthquakeData(String requestUrl) {

        //Log.e(LOG_TAG, "We now called FetchEarthquakeData");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return jsonResponse;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200 /*|| urlConnection.getResponseCode() == 301*/) {        // TODO: Handle Redirections appropriately
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode() + " Series Code error " + (urlConnection.getResponseCode()%100) );
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }




    public static List<Earthquake> extractEarthquakes(String jsonInput){


        if (TextUtils.isEmpty(jsonInput)) {
            return null;
        }

        List<Earthquake> earthquakes = new ArrayList<Earthquake>();


        SimpleDateFormat dateFormatter = (SimpleDateFormat) SimpleDateFormat.getDateInstance(); // new SimpleDateFormat("MMM DD, yyyy");    // TODO: why is this shoing Nov. 326

        //SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
        //SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm");

        SimpleDateFormat hourFormatter = (SimpleDateFormat) SimpleDateFormat.getTimeInstance();
        try {
            JSONObject root = new JSONObject(jsonInput);
            JSONArray features = root.getJSONArray("features");

            for (int i=0; i< features.length(); i++) {
                JSONObject quakeObject = features.getJSONObject(i);
                JSONObject quakeProperties = quakeObject.getJSONObject("properties");
                double mag = quakeProperties.getDouble("mag");

                //parse time into the date & hour
                long time = quakeProperties.getLong("time");
                Date dateObject = new Date (time);
                String dateToDisplay = dateFormatter.format(dateObject);
                String hourToDisplay = hourFormatter.format(dateObject);


                String url = quakeProperties.getString("url");

                String place = quakeProperties.getString("place");
                // parse the place to get location and offset
                String locationOffset, city;

                if (place.contains("of")){
                    int delimiter = place.indexOf ("of");
                    locationOffset = place.substring(0, delimiter+2);
                    city = place.substring(delimiter+3);
                }
                else{
                    locationOffset="";
                    city = place;
                }


                earthquakes.add(new Earthquake(mag, city, locationOffset, dateToDisplay, hourToDisplay, url));
            }

        }catch (JSONException e){
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return earthquakes;

    }


}
