package com.example.savings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    public static final int PIE_TYPE = 0;
    public static final int BAR_TYPE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void setPieChart(View view) {
        Intent intent = new Intent();
        intent.putExtra("K_graphType", PIE_TYPE);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void setBarChart(View view) {
        Intent intent = new Intent();
        intent.putExtra("K_graphType", BAR_TYPE);
        setResult(RESULT_OK, intent);
        finish();
    }
}