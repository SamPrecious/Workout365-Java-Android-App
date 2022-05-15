package com.example.workout365.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

//Contract for the exercise table
public class ExerciseContract {

    public static final String CONTENT_AUTHORITY = "com.example.workout365";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_EXERCISE = "exercise";

    private ExerciseContract() {}


    //Defines the create query for the Exercise Table
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + ExerciseTable.TABLE_NAME + " (" +
            ExerciseTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ExerciseTable.COLUMN_EXERCISE_NAME + " TEXT UNIQUE," +
            ExerciseTable.COLUMN_DESCRIPTION + " TEXT," +
            ExerciseTable.COLUMN_DIFFICULTY + " TEXT)";
    //Defines the delete query for the Exercise Table
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ExerciseTable.TABLE_NAME;

    public static final class ExerciseTable implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXERCISE).build();

        //table name
        public static final String TABLE_NAME = "EXERCISE_TABLE";
        //Column Name
        public static final String COLUMN_EXERCISE_NAME = "EXERCISE_NAME";
        public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
        public static final String COLUMN_DIFFICULTY = "DIFFICULTY";

        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/"+CONTENT_AUTHORITY+"/"+PATH_EXERCISE;
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/"+CONTENT_AUTHORITY+"/"+PATH_EXERCISE;

        public static Uri buildExerciseUriWithId(long exercise_id){
            return ContentUris.withAppendedId(CONTENT_URI, exercise_id);
        }
    }




}
