package com.example.workout365.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WorkoutDBHelper extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "workout.db";
    public SQLiteDatabase myDB;

    public WorkoutDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
        myDB = getWritableDatabase();
        Log.i("WorkoutDBHelper","initialised");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {   //WeatherContract.WeatherTable.COLUMN_DAY_NO
        myDB = db;
        Log.i("WorkoutHelper", "Creating table");  //Only runs on first initiliasation of device, dont actually need 'IF NOT EXISTS'

        //Creates tables
        myDB.execSQL(ExerciseContract.SQL_CREATE_ENTRIES);
        myDB.execSQL(RoutineContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,  int oldVersion, int newVersion) {
        String upgradeExercise = "DROP TABLE IF EXISTS " + ExerciseContract.ExerciseTable.TABLE_NAME;
        String upgradeRoutine = "DROP TABLE IF EXISTS " + RoutineContract.RoutineTable.TABLE_NAME;

        myDB.execSQL(upgradeExercise);
        myDB.execSQL(upgradeRoutine);
        onCreate(myDB);
    }
}

