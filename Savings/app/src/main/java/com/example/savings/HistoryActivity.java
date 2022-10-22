package com.example.savings;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {

    SharedPreferences pref;
    TextView history;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        pref = this.getSharedPreferences("DATA", Context.MODE_PRIVATE);
        history = findViewById(R.id.historyText);

        history.setText(pref.getString("K_history", ""));
    }

    public void clearHistory(View view) {
        pref.edit().clear().apply();
        history.setText(pref.getString("K_history", ""));
    }
}