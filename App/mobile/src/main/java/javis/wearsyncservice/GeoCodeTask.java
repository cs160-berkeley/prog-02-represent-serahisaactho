package javis.wearsyncservice;

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


public class GeoCodeTask extends AsyncTask<Object,Void,String> {

    private Exception exception;
    private Context mContext;
    public static String county_name;
    public String last_latitude;
    public String last_longitude;
    public String api_key;
    public String sunlight_api;
    public String location;


    protected void onPreExecute() {
    }

    // protected String doInBackground(Void... urls) {
    //Can make it take in Object type so can be of different types
    protected String doInBackground(Object... zipcodes) {
        int index = 0;
        mContext = (Context)zipcodes[index]; index++;
        api_key = (String)zipcodes[index]; index++;
        last_latitude = (String)zipcodes[index]; index++;
        last_longitude = (String)zipcodes[index]; index++;
        sunlight_api = (String)zipcodes[index]; index++;
        location = (String)zipcodes[index];index++; //either current location or the zipcode

        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng="+last_latitude+","+last_longitude+"&result_type=administrative_area_level_2&key="+api_key);
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
        //progressBar.setVisibility(View.GONE);
        /*Intent i = new Intent(mContext, ParsingActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(SunlightInstructions.CONGRESS_JSON,response);
        mContext.startActivity(i);*/
        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray arr = jsonObj.getJSONArray("results");
            //int num_people = arr.length();
            JSONObject obj = arr.getJSONObject(0);
            JSONArray arr1 = obj.getJSONArray("address_components");
            //System.out.println("BAZOOKA county name = " + county_obj.getString("short_name"));
            for(int i=0;i<arr1.length();i++)
            {
                JSONObject curr_obj = arr1.getJSONObject(i);
                if (curr_obj.getString("types").equals("["+
                        "\"administrative_area_level_2\""+","+
                        "\"political\""+"]"))
                {
                    county_name = curr_obj.getString("short_name");
                    break;
                }
            }

            //JSON ELECTION STUFF
            try {
                JSONArray election_arr = new JSONArray(loadJSONFromAsset());
                for (int i=0;i<election_arr.length();i++) {
                    JSONObject election_obj = election_arr.getJSONObject(i);
                    if (county_name.startsWith(election_obj.getString("county-name")))//((election_obj.getString("county-name")+" County").equals(county_name))///(county_name.startsWith(election_obj.getString("county-name"))==true);
                    {
                        /*System.out.println("BAZOOKA county-name = "+county_name+" json name = "+
                                election_obj.getString("county-name"));*/
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (location.equals("Current Location"))
            {
                SunlightTask task = new SunlightTask();
                task.execute(mContext, SunlightInstructions.CURRENT_LOC,sunlight_api,last_latitude, last_longitude);
            }
            else
            {
                SunlightTask task = new SunlightTask();
                task.execute(mContext,SunlightInstructions.CUSTOM_ZIP,sunlight_api,location);
            }




            //JSON ELECTION STUFF

        }
        catch(Exception e)
        {
            System.out.println("BAZOOKA Failure in GeoCodeTask " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        /**
         * Loads the election json file into a string that we can use.
         */
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("election-county-2012.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}

