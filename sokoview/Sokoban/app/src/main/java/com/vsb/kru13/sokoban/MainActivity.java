package com.vsb.kru13.sokoban;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    SokoView sokoView;

    Intent levelsIntent;
    Intent levelsRIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sokoView = findViewById(R.id.sokoView);

        levelsIntent = new Intent(this, LevelsActivity.class);
        levelsRIntent = new Intent(this, LevelsRecycledActivity.class);
        MyDatabase dbHelper = new MyDatabase(this);


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.drop();
        loadFile(db);
    }

    public void loadFile(SQLiteDatabase db){
        InputStream input;
        try {
            AssetManager assetManager = getAssets();

            input = assetManager.open("levels.txt");

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            String text = new String(buffer);
            text = text.replace("\r\n", "\n");
            String[] lines = text.split("\n\n", 0);

            for (String line : lines) {
                String[] data = line.split("\'", 0);
                if (data.length == 3) {
                    if (!SokoView.levelData.contains(data[2])) {
                        SokoView.names.add(data[1]);
                        SokoView.levelData.add(data[2]);

                        ContentValues values = new ContentValues();
                        values.put(MyDatabase.COLUMN_TITLE, data[1]);
                        values.put(MyDatabase.COLUMN_MIN_MOVES, "closed");
                        values.put(MyDatabase.COLUMN_DATA, data[2]);
                        db.insert(MyDatabase.TABLE_NAME, null, values);
                    }
                }
            }

            sokoView.load(SokoView.current_index);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset:
                sokoView.reset();
                return true;
            case R.id.levels:
//                startActivityForResult(levelsIntent, 1);
                startActivityForResult(levelsRIntent, 1);
//                startActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            int i = Integer.parseInt(data.getStringExtra("level_id"));
            sokoView.load(i - 1);
        }
    }
}
