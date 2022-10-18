package com.example.savings;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        money = findViewById(R.id.editTextNumberDecimal);
        percentage = findViewById(R.id.editTextNumberDecimal2);
        time = findViewById(R.id.editTextNumberDecimal3);
        result = findViewById(R.id.textView);

        pie = findViewById(R.id.pie_chart);
    }

    @SuppressLint("SetTextI18n")
    public void calculate(View view) {

        pie.setDrawHoleEnabled(false);
        pie.setUsePercentValues(true);
        pie.setEntryLabelColor(Color.BLACK);
        pie.getDescription().setEnabled(false);
        pie.getLegend().setEnabled(false);
        ArrayList<PieEntry> entries = new ArrayList<>();

        double money_num = Double.parseDouble("0" + money.getText().toString());
        double percentage_num = Double.parseDouble("0" + percentage.getText().toString());
        double time_num = Double.parseDouble("0" + time.getText().toString());

        long result_num = Math.round(money_num * (Math.pow(1 + (percentage_num/100), time_num)));
        long earn_num = Math.round(result_num - money_num);
        result.setText("You get " + result_num + " and earn " + earn_num);

        entries.add(new PieEntry(earn_num, "New Money"));
        entries.add(new PieEntry(Math.round(money_num), "Old Money"));
        PieDataSet dataSet = new PieDataSet(entries, "");
        PieData data = new PieData(dataSet);

        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        data.setDrawValues(true);


        pie.setData(data);
        pie.invalidate();
    }
}