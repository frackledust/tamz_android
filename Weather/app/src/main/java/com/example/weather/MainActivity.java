package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    CurrentWeatherFragment currentWeatherFragment;
    ForecastFragment forecastFragment;

    Button button;

    @SuppressLint({"MissingInflatedId", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentWeatherFragment = CurrentWeatherFragment.newInstance("London");
        forecastFragment = ForecastFragment.newInstance(this);
        changeFragment(currentWeatherFragment);

        BottomNavigationView bnw = findViewById(R.id.bottom_navigation);
        bnw.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.current_weather:
                    changeFragment(currentWeatherFragment);
                    break;
                case R.id.forecast:
                    changeFragment(forecastFragment);
                    break;
            }
            return true;
        });
    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

}