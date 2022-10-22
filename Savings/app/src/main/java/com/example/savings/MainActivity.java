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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final int SETTINGS_ACTIVITY_REQUEST_CODE = 1;
    EditText money;
    EditText percentage;
    EditText time;
    TextView result;

    PieChart pie;
    BarChart barChart;

    Intent aboutIntent;
    Intent historyIntent;
    Intent settingsIntent;

    int graphType = SettingsActivity.PIE_TYPE;

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
        barChart = findViewById(R.id.barChart_view);

        aboutIntent = new Intent(this, AboutActivity.class);
        historyIntent = new Intent(this, HistoryActivity.class);
        settingsIntent = new Intent(this, SettingsActivity.class);

        pref = this.getSharedPreferences("DATA", Context.MODE_PRIVATE);
    }

    @Override
    public void onResume() {
        super.onResume();

        money.setText(pref.getString("K_money", ""));
        percentage.setText(pref.getString("K_perc", ""));
        time.setText(pref.getString("K_time", ""));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            graphType = data.getIntExtra("K_graphType", 0);

            pie.setVisibility(View.GONE);
            barChart.setVisibility(View.GONE);

            if(graphType == SettingsActivity.PIE_TYPE){
                pie.setVisibility(View.VISIBLE);
            }
            else{
                barChart.setVisibility(View.VISIBLE);
            }
        }
    }

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
                startActivity(aboutIntent);
                return true;
            case R.id.history:
                Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();
                startActivity(historyIntent);
                return true;
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                startActivityForResult(settingsIntent, SETTINGS_ACTIVITY_REQUEST_CODE);
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

        if(graphType == SettingsActivity.PIE_TYPE){
            createPieChart(earn_num, Math.round(money_num));
        }
        else{
            createBarChart(earn_num, result_num);
        }
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

    private void createBarChart(long earn_num, long total){
        barChart.getDescription().setEnabled(false);
        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.getAxisRight().setDrawLabels(false);

        final String[] labels = new String[] {"Earn", "Total"};
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);

        ArrayList<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(0, earn_num));
        entries.add(new BarEntry(1, total));

        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet.setValueTextColors(Collections.singletonList(Color.WHITE));

        BarData data = new BarData(barDataSet);
        barChart.setData(data);
        barChart.invalidate();
    }
}