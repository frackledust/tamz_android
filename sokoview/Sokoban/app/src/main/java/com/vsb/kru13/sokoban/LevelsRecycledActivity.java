package com.vsb.kru13.sokoban;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class LevelsRecycledActivity extends AppCompatActivity {

    RecyclerView recycledView;
    ArrayList<String> level_id, level_title, level_moves, level_data;
    MyDatabase dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels_recycled);

        recycledView = findViewById(R.id.my_recycled_view);

        dbHelper = new MyDatabase(this);
        level_id = new ArrayList<>();
        level_title = new ArrayList<>();
        level_moves = new ArrayList<>();
        level_data = new ArrayList<>();

        DisplayData();

        CustomAdapter customAdapter = new CustomAdapter(this, this, level_id, level_title, level_moves, level_data);
        recycledView.setAdapter(customAdapter);
        recycledView.setLayoutManager(new LinearLayoutManager(this));
    }

    void DisplayData(){
        Cursor cursor = dbHelper.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "NO DATA", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                level_id.add(cursor.getString(0));
                level_title.add(cursor.getString(1));
                level_moves.add(cursor.getString(2));
                level_data.add(cursor.getString(3));
            }
        }
    }

    void Finish(String id){
        Intent intent = new Intent();
        intent.putExtra("level_id", id);
        setResult(RESULT_OK, intent);
        finish();
    }
}