package dev.maroo.tempmonitor;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    TextView sensorField;
    TextView updatedField;
    TextView currentTemperatureField;

    Handler handler;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
        handler = new Handler();
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        sensorField = (TextView) rootView.findViewById(R.id.sensor_name_field);
        updatedField = (TextView) rootView.findViewById(R.id.updated_field);
        currentTemperatureField = (TextView) rootView.findViewById(R.id.current_temperature_field);
        updateTemperatureData(String.valueOf(getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

    private void updateTemperatureData(final String sensorId){
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetch.getJSON(getActivity(), sensorId);
                if(json != null){
                    handler.post(new Runnable(){
                        public void run(){
                            renderTemperature(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderTemperature(JSONObject json){
        try {
            JSONObject sensor = json.getJSONObject("sensor");
            sensorField.setText(sensor.getString("name"));
            updatedField.setText("Last update: " + json.getString("createdDate"));
            currentTemperatureField.setText(String.format("%.2f", json.getDouble("value"))+ " ℃");
        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }
}
