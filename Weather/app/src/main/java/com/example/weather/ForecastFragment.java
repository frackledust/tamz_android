package com.example.weather;

import android.annotation.SuppressLint;
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
    Context context;
    View view;

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

            recyclerView = view.findViewById(R.id.frame_recycled_view);
            ForecastAdapter adapter = new ForecastAdapter(context, castTime, castDegrees, castDesc, castIcons);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
    };

    public ForecastFragment(Context context) {
        this.context = context;
    }

    public static ForecastFragment newInstance(Context context) {
        return new ForecastFragment(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forecast, container, false);
        ForecastConnector con = new ForecastConnector(handler, CurrentWeatherFragment.cityName);
        con.start();
        return view;
    }
}