package com.example.weather;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ForecastConnector extends Thread{

    String city;

    Handler handler;

    ForecastConnector(Handler handler, String city) {
        this.handler = handler;
        this.city = city;
    }

    public void run() {
        try {
            String s = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&units=metric&appid=c0fc4b527c4fa17af1a51475072722ca";
            URL url = new URL(s);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream is = new BufferedInputStream((urlConnection.getInputStream()));

            BufferedInputStream bis = new BufferedInputStream(is);

            BufferedReader r = new BufferedReader(new InputStreamReader(bis));
            StringBuilder total = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                total.append(line).append('\n');
            }

            Log.d("FORECAST CONNECTOR", total.toString());
            processJsonResult(total.toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    //array -> list
    //object
    //object -> main (double temp)
    //array -> weather
    //object -> (description, icon)


    void processJsonResult(String jsonString) throws JSONException {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("list");

            ArrayList<String> times = new ArrayList<>(jsonArray.length());
            ArrayList<String> degrees = new ArrayList<>(jsonArray.length());
            ArrayList<String> descriptions = new ArrayList<>(jsonArray.length());
            ArrayList<String> icons = new ArrayList<>(jsonArray.length());

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject o = jsonArray.getJSONObject(i);
                String time = o.getString("dt_txt");

                JSONArray weatherA = o.getJSONArray("weather");
                JSONObject weatherO = weatherA.getJSONObject(0);
                String desc = weatherO.getString("description");
                String icon = weatherO.getString("icon");

                JSONObject mainO = o.getJSONObject("main");
                double temp = mainO.getDouble("temp");

                times.add(time);
                degrees.add(temp + " °C");
                descriptions.add(desc);
                icons.add(icon);
            }



            Bundle b = new Bundle();
            b.putStringArrayList("times", times);
            b.putStringArrayList("degrees", degrees);
            b.putStringArrayList("descriptions", descriptions);
            b.putStringArrayList("icons", icons);

            Message m = handler.obtainMessage();
            m.setData(b);
            m.sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
