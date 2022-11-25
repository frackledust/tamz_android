package com.example.weather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ForecastFragment extends Fragment {

    RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    Context mContext;
    View view;
    Handler current_weather_handler;

    ArrayList<String> castTime, castDegrees, castDesc, castIcons;

    @SuppressWarnings("deprecation")
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
        @Override
        public void handleMessage(@NonNull Message msg) {
            castTime = msg.getData().getStringArrayList("times");
            castDegrees = msg.getData().getStringArrayList("degrees");
            castDesc = msg.getData().getStringArrayList("descriptions");
            castIcons = msg.getData().getStringArrayList("icons");

            Log.d("FORECAST HANDLER", castTime.get(0));

            recyclerView = view.findViewById(R.id.frame_recycled_view);

            ForecastAdapter adapter = new ForecastAdapter(mContext, current_weather_handler, castTime, castDegrees, castDesc, castIcons);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        }
    };

    public ForecastFragment() {
    }

    public static ForecastFragment newInstance(Handler current_weather_handler) {
        ForecastFragment fragment = new ForecastFragment();
        fragment.current_weather_handler = current_weather_handler;
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forecast, container, false);
        ForecastConnector con = new ForecastConnector(handler, MainActivity.cityName);
        con.start();
        return view;
    }
}