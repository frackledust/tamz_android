package com.example.weather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentWeatherFragment extends Fragment {

    @SuppressWarnings("deprecation")
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            Double temperature = msg.getData().getDouble("temperature", 0);
            String description = msg.getData().getString("description", "");
            String icon = msg.getData().getString("icon", "");
            icon = "icon_" + icon;

            View current_view = getView();

            assert current_view != null;
            TextView tempView = current_view.findViewById(R.id.tempText);
            TextView descView = current_view.findViewById(R.id.descText);
            ImageView iconView = current_view.findViewById(R.id.current_weather_icon);

            tempView.setText(temperature + " °C");
            descView.setText(description);

            Activity current_activity = getActivity();

            assert current_activity != null;
            @SuppressLint("DiscouragedApi")
            int id = getResources().getIdentifier(icon, "drawable", current_activity.getPackageName());
            iconView.setImageResource(id);
        }
    };

    View view;

    public static String cityName;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    public static CurrentWeatherFragment newInstance(String cityName) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        Bundle args = new Bundle();
        args.putString("CITY_NAME", cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cityName = getArguments().getString("CITY_NAME");
        }

        Connector con = new Connector(handler, cityName);
        con.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_current_weather, container, false);

        TextView tw = view.findViewById(R.id.cityTextBox);
        tw.setText(cityName);

        Button button = view.findViewById(R.id.changeCityButton);
        button.setOnClickListener(this::changeCity);
        return view;
    }

    public void changeCity(View view) {
        TextView tw = this.view.findViewById(R.id.cityTextBox);
        String city = String.valueOf(tw.getText());
        if(city.length() > 0){
            cityName = city;
            Connector con = new Connector(handler, cityName);
            con.start();
        }
    }
}