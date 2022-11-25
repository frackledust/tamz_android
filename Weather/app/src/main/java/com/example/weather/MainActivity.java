package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    CurrentWeatherFragment currentWeatherFragment;
    ForecastFragment forecastFragment;

    static String cityName;

    @SuppressLint({"MissingInflatedId", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            cityName = savedInstanceState.getString("CITY_NAME");
        }
        else{
            cityName = "Ostrava";
        }

        currentWeatherFragment = CurrentWeatherFragment.newInstance();
        forecastFragment = ForecastFragment.newInstance(currentWeatherFragment.handler);

        boolean isLandscape = getResources().getBoolean(R.bool.isLandscape);
        changeFragment(R.id.fragment_container, currentWeatherFragment);

        if (isLandscape) {
            changeFragment(R.id.fragment_container2, forecastFragment);
        } else {

            BottomNavigationView bnw = findViewById(R.id.bottom_navigation);
            bnw.setOnItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case R.id.current_weather:
                        changeFragment(R.id.fragment_container, currentWeatherFragment);
                        break;
                    case R.id.forecast:
                        changeFragment(R.id.fragment_container, forecastFragment);
                        break;
                }
                return true;
            });
        }
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("CITY_NAME", cityName);
    }

    private void changeFragment(int container, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(container, fragment).commit();
    }

}