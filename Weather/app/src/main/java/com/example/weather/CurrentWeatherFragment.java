package com.example.weather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
            String temperature = msg.getData().getString("temperature", "0 °C");
            String description = msg.getData().getString("description", "");
            String icon = msg.getData().getString("icon", "");
            icon = "icon_" + icon;


            TextView tempView = view.findViewById(R.id.tempText);
            TextView descView = view.findViewById(R.id.descText);
            ImageView iconView = view.findViewById(R.id.current_weather_icon);

            tempView.setText(temperature);
            descView.setText(description);

            @SuppressLint("DiscouragedApi")
            int id = mContext.getResources().getIdentifier(icon, "drawable", mContext.getPackageName());
            iconView.setImageResource(id);
        }
    };

    Context mContext;
    View view;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    public static CurrentWeatherFragment newInstance() {
        return new CurrentWeatherFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Connector con = new Connector(handler, MainActivity.cityName);
        con.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_current_weather, container, false);

        TextView tw = view.findViewById(R.id.cityTextBox);
        tw.setText(MainActivity.cityName);

        Button button = view.findViewById(R.id.changeCityButton);
        button.setOnClickListener(this::changeCity);
        return view;
    }

    public void changeCity(View view) {
        TextView tw = this.view.findViewById(R.id.cityTextBox);
        String city = String.valueOf(tw.getText());
        if(city.length() > 0){
            MainActivity.cityName = city;
            Connector con = new Connector(handler, MainActivity.cityName);
            con.start();
        }
    }
}