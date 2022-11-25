package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

// THIS CLASS NO LONGER WORKING

public class ForecastActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Context context;
    Handler current_weather_handler;

    ArrayList<String> castTime, castDegrees, castDesc, castIcons;

    @SuppressWarnings("deprecation")
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){

        @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
        @Override
        public void handleMessage(@NonNull Message msg) {
            castTime = msg.getData().getStringArrayList("times");
            castDegrees = msg.getData().getStringArrayList("degrees");
            castDesc = msg.getData().getStringArrayList("descriptions");
            castIcons = msg.getData().getStringArrayList("icons");

            Log.d("FORECAST HANDLER", castTime.get(0));

            recyclerView = findViewById(R.id.my_recycled_view);
            ForecastAdapter adapter = new ForecastAdapter(context, current_weather_handler, castTime, castDegrees, castDesc, castIcons);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

//        this.current_weather_handler = current_weather_handler;
        context = this;
        String cityName = "Ostrava";
        ForecastConnector con = new ForecastConnector(handler, cityName);
        con.start();
    }
}