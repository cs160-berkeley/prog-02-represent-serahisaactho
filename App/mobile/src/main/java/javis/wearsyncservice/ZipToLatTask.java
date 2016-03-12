package javis.wearsyncservice;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;


public class ZipToLatTask extends AsyncTask<Object,Void,String> {

    private Exception exception;
    private Context mContext;
    public static String county_name;
    public String last_latitude;
    public String last_longitude;
    public String api_key;
    public String sunlight_api;
    public String zip;


    protected void onPreExecute() {
    }

    // protected String doInBackground(Void... urls) {
    //Can make it take in Object type so can be of different types
    protected String doInBackground(Object... zipcodes) {
        int index = 0;
        mContext = (Context)zipcodes[index]; index++;
        api_key = (String)zipcodes[index]; index++;
        zip = (String)zipcodes[index]; index++;
        sunlight_api = (String)zipcodes[index]; index++;

        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="+zip);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }




    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray arr = jsonObj.getJSONArray("results");
            JSONObject obj = arr.getJSONObject(0);
            JSONObject geom_obj = obj.getJSONObject("geometry");
            JSONObject location_obj = geom_obj.getJSONObject("location");
            last_latitude = location_obj.getString("lat");
            last_longitude = location_obj.getString("lng");
//            System.out.println("BAZOOKA last_longitude = "+ last_longitude +
//                    " last_latitude = "+ last_latitude);

            //JSON ELECTION STUFF

            GeoCodeTask task = new GeoCodeTask();
            task.execute(mContext, api_key, last_latitude, last_longitude, sunlight_api, zip);


            //JSON ELECTION STUFF

        }
        catch(Exception e)
        {
            System.out.println("BAZOOKA Failure in GeoCodeTask " + e.getMessage());
            e.printStackTrace();
        }
    }




}

