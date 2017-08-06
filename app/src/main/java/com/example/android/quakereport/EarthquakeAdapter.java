package com.example.android.quakereport;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import android.graphics.drawable.GradientDrawable;

import static java.lang.String.valueOf;

/**
 * Created by mahyar on 2017-06-09.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public static final String LOG_TAG = EarthquakeAdapter.class.getSimpleName();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }


        //Find where in the list we are
        Earthquake currentEarthquakeItem = getItem(position);


        // set the appropriate values for the Magnitude (including color)
        DecimalFormat formatter = new DecimalFormat("0.0");
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);

        //TextView magnitudeColorView = (TextView) listItemView.findViewById(R.id.magnitude);

        magnitudeView.setText(formatter.format(currentEarthquakeItem.getMagnitude()));

        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magColor= getMagnitudeColor(currentEarthquakeItem.getMagnitude());
        magnitudeCircle.setColor(magColor);


        TextView cityTextView = (TextView) listItemView.findViewById(R.id.primary_location);
        cityTextView.setText(currentEarthquakeItem.getPrimaryLocation());

        TextView locationOffsetTextView = (TextView) listItemView.findViewById(R.id.location_offset);
        locationOffsetTextView.setText(currentEarthquakeItem.getLocationOffset());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        dateTextView.setText(currentEarthquakeItem.getTime());

        TextView hourTextView = (TextView) listItemView.findViewById(R.id.time);
        hourTextView.setText(currentEarthquakeItem.getHour());

        return listItemView;
    }

    private int getMagnitudeColor (double magnitude){
        int mag = (int) magnitude;

        switch(mag){
            case 1:
                return (ContextCompat.getColor(getContext(), R.color.magnitude1));
            case 2:
                return (ContextCompat.getColor(getContext(),R.color.magnitude2));
            case 3:
                return (ContextCompat.getColor(getContext(),R.color.magnitude3));
            case 4:
                return (ContextCompat.getColor(getContext(),R.color.magnitude4));
            case 5:
                return (ContextCompat.getColor(getContext(),R.color.magnitude5));
            case 6:
                return (ContextCompat.getColor(getContext(),R.color.magnitude6));
            case 7:
                return (ContextCompat.getColor(getContext(),R.color.magnitude7));
            case 8:
                return (ContextCompat.getColor(getContext(),R.color.magnitude8));
            case 9:
                return (ContextCompat.getColor(getContext(),R.color.magnitude9));
            case 10:
                return (ContextCompat.getColor(getContext(),R.color.magnitude10plus));

        }
        return (ContextCompat.getColor(getContext(), R.color.magnitude1));
    }


    public EarthquakeAdapter(Context context, List<Earthquake> quakes) {
        super(context, 0 , quakes);
    }

}
