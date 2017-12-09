package dev.maroo.tempmonitor;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Maroo on 2017-04-26.
 */

public class RemoteFetch {
    private static final String TEMPERATURE_REST_API = "http://192.168.55.102:9000/sensor/%s";
    private static final String CHECK_ERROR_REST_API = "http://192.168.55.102:9000/checkError";

    public static JSONObject getJSON(FragmentActivity activity, String sensorId){
        try {
            URL url = new URL(String.format(TEMPERATURE_REST_API, sensorId));
            return getJSON(url);
        }catch(Exception e){
            return null;
        }
    }

    public static JSONObject getCheckErrorJSON(){
        try {
            URL url = new URL(CHECK_ERROR_REST_API);
            return getJSON(url);
        }catch(Exception e){
            return null;
        }
    }

    private static JSONObject getJSON(URL url){
        try {
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());
            return data;
        }catch(Exception e){
            return null;
        }
    }
}
