package com.example.savings;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText money;
    EditText percentage;
    EditText time;
    TextView result;

    PieChart pie;

    Intent about_intent;
    Intent history_intent;
    Intent settings_intent;

    int[] colors = ColorTemplate.LIBERTY_COLORS;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        money = findViewById(R.id.editTextNumberDecimal);
        percentage = findViewById(R.id.editTextNumberDecimal2);
        time = findViewById(R.id.editTextNumberDecimal3);
        result = findViewById(R.id.textView);

        pie = findViewById(R.id.pie_chart);

        about_intent = new Intent(this, AboutActivity.class);
        history_intent = new Intent(this, HistoryActivity.class);
        settings_intent = new Intent(this, SettingsActivity.class);

        pref = this.getSharedPreferences("DATA", Context.MODE_PRIVATE);
    }

    @Override
    public void onResume() {
        super.onResume();

        money.setText(pref.getString("K_money", ""));
        percentage.setText(pref.getString("K_perc", ""));
        time.setText(pref.getString("K_time", ""));
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        int SECOND_ACTIVITY_REQUEST_CODE = 1;
//        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
//            if(resultCode == RESULT_OK){
//                String result=data.getStringExtra("result");
//
//                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                startActivity(about_intent);
                return true;
            case R.id.history:
                Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();
                startActivity(history_intent);
                return true;
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                startActivity(settings_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("SetTextI18n")
    public void calculate(View view) {

        pref.edit().putString("K_money", money.getText().toString()).apply();
        pref.edit().putString("K_perc", percentage.getText().toString()).apply();
        pref.edit().putString("K_time", time.getText().toString()).apply();


        double money_num = Double.parseDouble("0" + money.getText().toString());
        double percentage_num = Double.parseDouble("0" + percentage.getText().toString());
        double time_num = Double.parseDouble("0" + time.getText().toString());

        long result_num = Math.round(money_num * (Math.pow(1 + (percentage_num/100), time_num)));
        long earn_num = Math.round(result_num - money_num);
        result.setText("You get " + result_num + " and earn " + earn_num);

        String history_text = pref.getString("K_history", "");
        history_text +=
                "Deposit: " + money_num +
                " Perc: " + percentage_num +
                " Time: " + time_num +
                " Result: " + result_num + "\n";
        pref.edit().putString("K_history", history_text).apply();

        createPieChart(earn_num, Math.round(money_num));

    }

    private void createPieChart(long earn_num, long money_num){
        pie.setDrawHoleEnabled(false);
        pie.setUsePercentValues(true);
        pie.setEntryLabelColor(Color.BLACK);
        pie.getDescription().setEnabled(false);
        pie.getLegend().setEnabled(false);
        pie.setEntryLabelTextSize(15f);
        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(earn_num, "New Money"));
        entries.add(new PieEntry(money_num, "Old Money"));

        PieDataSet dataSet = new PieDataSet(entries, "");

        PieData data = new PieData(dataSet);

        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        data.setDrawValues(true);
        data.setValueTextSize(10f);


        pie.setData(data);
        pie.invalidate();
    }
}