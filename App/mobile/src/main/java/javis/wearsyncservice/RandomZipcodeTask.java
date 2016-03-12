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
import java.util.Random;
import java.util.Scanner;


public class RandomZipcodeTask extends AsyncTask<Object,Void,String> {

    private Exception exception;
    private Context mContext;
    public String api_key;
    public String sunlight_api;
    public String random_state;
    public int random_index;
    public String zipcode;


    protected void onPreExecute() {
    }

    // protected String doInBackground(Void... urls) {
    //Can make it take in Object type so can be of different types
    protected String doInBackground(Object... items) {
        int index = 0;
        mContext = (Context)items[index]; index++;
        api_key = (String)items[index]; index++;
        sunlight_api = (String)items[index]; index++;
        random_state = (String)items[index]; index++;
        random_index = (Integer)items[index]; index++;

        try {
            URL url = new URL("http://gomashup.com/json.php?fds=geo/usa/zipcode/state/"+random_state+"&json");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                System.out.println("BAZOOKA inside RandomZipcodeTask");
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
            JSONObject obj = new JSONObject(response.substring(1,response.length()-1));
            //removing the first and last bracket to ensure that it is in JSON object format
            JSONArray arr = obj.getJSONArray("result");
            Random rn = new Random();
            int index = rn.nextInt(arr.length() -1 - 0 + 1) + 0;
            JSONObject random_obj = arr.getJSONObject(index);
            zipcode = random_obj.getString("Zipcode");

            ZipToLatTask task = new ZipToLatTask();
            task.execute(mContext, api_key,zipcode, sunlight_api);

        }
        catch(Exception e)
        {
            System.out.println("BAZOOKA Failure in RandomZipcodeTask " + e.getMessage());
            e.printStackTrace();
        }
    }



}

