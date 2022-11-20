package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static String cityName;

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

            TextView tempView = findViewById(R.id.tempText);
            TextView descView = findViewById(R.id.descText);
            ImageView iconView = findViewById(R.id.imageView);

            tempView.setText(temperature + " °C");
            descView.setText(description);

            int id = getResources().getIdentifier(icon, "drawable", getPackageName());
            //id = R.drawable.icon_02d;
            iconView.setImageResource(id);

            Log.d("HANDLER", description);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = "Ostrava";
        TextView tw = findViewById(R.id.cityTextBox);
        tw.setText(cityName);

        Connector con = new Connector(handler, cityName);
        con.start();
    }

    public void changeCity(View view) {
        TextView tw = findViewById(R.id.cityTextBox);
        String city = String.valueOf(tw.getText());
        if(city.length() > 0){
            cityName = city;
            Connector con = new Connector(handler, cityName);
            con.start();
        }
    }

    public void showForecast(View view) {
        Intent forecastIntent = new Intent(this, ForecastActivity.class);
        startActivity(forecastIntent);
    }
}