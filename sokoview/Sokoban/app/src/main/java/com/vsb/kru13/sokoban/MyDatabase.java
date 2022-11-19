package com.vsb.kru13.sokoban;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabase extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "Sokoban.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "Level";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_TITLE = "level_title";
    public static final String COLUMN_DATA = "level_data";
    public static final String COLUMN_MIN_MOVES = "min_moves";


    public MyDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_TITLE + " TEXT," +
                        COLUMN_DATA + " TEXT, " +
                        COLUMN_MIN_MOVES + " TEXT)";

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    public void drop(){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }

    public String getMoves(String level_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_MIN_MOVES
        };
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { level_id };

        Cursor cursor = db.query(
                TABLE_NAME,             // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // The sort order
        );

        if(cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MIN_MOVES));
        }
        return "0";
    }

    public int setMoves(String level_id, String moves) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MIN_MOVES, moves);
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { level_id };

        return db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
}
