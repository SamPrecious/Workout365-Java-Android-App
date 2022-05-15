package com.example.workout365.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class RoutineContract {

    public static final String CONTENT_AUTHORITY = "com.example.workout365";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ROUTINE = "routine";

    public RoutineContract() {};
    //Defines the create query for the Exercise Table
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + RoutineTable.TABLE_NAME + " (" +
            RoutineTable.COLUMN_EXERCISE_NAME + " TEXT," +
            RoutineTable.COLUMN_DAY + " TEXT," +
            RoutineTable.COLUMN_REPS + " TEXT," +
            RoutineTable.COLUMN_SETS + " TEXT," +
            "PRIMARY KEY (" + RoutineTable.COLUMN_EXERCISE_NAME + ", " + RoutineTable.COLUMN_DAY +"))";
    //            RoutineTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

    //TODO create statement for Routine table!

    public static final class RoutineTable implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROUTINE).build();

        public static final String TABLE_NAME = "ROUTINE_TABLE";
        //Column Names
        public static final String COLUMN_EXERCISE_NAME = "EXERCISE_NAME";
        public static final String COLUMN_DAY = "DAY";
        public static final String COLUMN_REPS = "REPS";
        public static final String COLUMN_SETS = "SETS";

        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/"+CONTENT_AUTHORITY+"/"+PATH_ROUTINE;
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/"+CONTENT_AUTHORITY+"/"+PATH_ROUTINE;

        public static Uri buildRoutineUriWithId(long exercise_id){
            return ContentUris.withAppendedId(CONTENT_URI, exercise_id);
        }

    }
}
