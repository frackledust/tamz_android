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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connector extends Thread {

    String city;

    Handler handler;

    Connector(Handler handler, String city) {
        this.handler = handler;
        this.city = city;
    }

    public void run() {
        try {
            String s = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=c0fc4b527c4fa17af1a51475072722ca";
            URL url = new URL(s);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream is = new BufferedInputStream((urlConnection.getInputStream()));

            BufferedInputStream bis = new BufferedInputStream(is);

            BufferedReader r = new BufferedReader(new InputStreamReader(bis));
            StringBuilder total = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                total.append(line).append('\n');
            }

            Log.d("CONNECTOR", total.toString());
            processJsonResult(total.toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    void processJsonResult(String jsonString) throws JSONException {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            JSONArray weatherA = jsonObject.getJSONArray("weather");
            JSONObject weatherO = weatherA.getJSONObject(0);
            String desc = weatherO.getString("description");
            String icon = weatherO.getString("icon");

            JSONObject mainO = jsonObject.getJSONObject("main");
            double temp = mainO.getDouble("temp");

            Bundle b = new Bundle();
            b.putDouble("temperature", temp);
            b.putString("description", desc);
            b.putString("icon", icon);

            Message m = handler.obtainMessage();
            m.setData(b);
            m.sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
