package com.example.workout365.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WorkoutProvider extends ContentProvider {

    public static final int EXERCISE = 100;
    public static final int EXERCISE_WITH_ID = 101;

    public static final int ROUTINE = 102;
    public static final int ROUTINE_WITH_ID = 103;
    private static final UriMatcher uriMatcher =  buildUriMatcher();
    public static WorkoutDBHelper myDBHelper;

    private static UriMatcher buildUriMatcher(){
        //The calls to addURI go here, for all of the content URI patterns
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        //Set the code for multiple rows to 100.
        matcher.addURI(ExerciseContract.CONTENT_AUTHORITY,ExerciseContract.PATH_EXERCISE, EXERCISE);

        //Sets the code for a single row to 101. In this case, the # wildcard is used.
        matcher.addURI(ExerciseContract.CONTENT_AUTHORITY, ExerciseContract.PATH_EXERCISE+"/#", EXERCISE_WITH_ID);

        matcher.addURI(RoutineContract.CONTENT_AUTHORITY,RoutineContract.PATH_ROUTINE, ROUTINE); //If we come through the routine path, we are set to the routine code for ROUTINE

        return matcher;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Log.i("WorkoutProvider", "Creating Workout Helper");
        myDBHelper = new WorkoutDBHelper(getContext(),WorkoutDBHelper.DATABASE_NAME,null,WorkoutDBHelper.DATABASE_VERSION);
        Log.i("WorkoutProvider", "Created Workout Helper");

        return true;
    }

    @Override
    public String getType(Uri uri){
        int match_code = uriMatcher.match(uri);

        switch(match_code){
            case EXERCISE:
                return ExerciseContract.ExerciseTable.CONTENT_TYPE_DIR;
            case EXERCISE_WITH_ID:
                return ExerciseContract.ExerciseTable.CONTENT_TYPE_ITEM;
            default:
                Log.i("WorkoutProvider", "Fails Here!");
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match_code = uriMatcher.match(uri);
        Uri retUri = null;

        switch(match_code){
            case EXERCISE:{
                Log.i("WorkoutProvider", Integer.toString(match_code));
                SQLiteDatabase db = myDBHelper.getWritableDatabase();
                long _id = db.insert(ExerciseContract.ExerciseTable.TABLE_NAME,null,values);
                if (_id > 0)
                    retUri = ExerciseContract.ExerciseTable.buildExerciseUriWithId(_id);
                else{
                    throw new SQLException("failed to insert"); //could move away from this exception and do it in other classes, to avoid activity crashing?
                }
                break;
            }
            case ROUTINE:{
                SQLiteDatabase db = myDBHelper.getWritableDatabase();
                long _id = db.insert(RoutineContract.RoutineTable.TABLE_NAME,null,values);
                if (_id > 0)
                    retUri = RoutineContract.RoutineTable.buildRoutineUriWithId(_id);
                else{
                    throw new SQLException("failed to insert"); //could move away from this exception and do it in other classes, to avoid activity crashing?
                }
                break;
            }
            default:
                Log.i("WorkoutProvider", uri.toString());
                Log.i("WorkoutProvider", Integer.toString(match_code));
                throw new UnsupportedOperationException("Not yet implemented");
        }
        return retUri;
    }

    //TODO Add Delete Functionality! Only for one exercise at a time
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int match_code = uriMatcher.match(uri);
        int numRowsDeleted;

        switch(match_code){
            case EXERCISE:{
                //Delete from Exercise Table
                numRowsDeleted = myDBHelper.getWritableDatabase().delete(
                        ExerciseContract.ExerciseTable.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                //Deletes routines that contain the same exercise
                myDBHelper.getWritableDatabase().delete(
                        RoutineContract.RoutineTable.TABLE_NAME,
                        RoutineContract.RoutineTable.COLUMN_EXERCISE_NAME + " = ?",
                        selectionArgs
                );
                break;
            }
            case ROUTINE:{
                //Deletes routines by exercise name
                numRowsDeleted = myDBHelper.getWritableDatabase().delete(
                        RoutineContract.RoutineTable.TABLE_NAME,
                        RoutineContract.RoutineTable.COLUMN_EXERCISE_NAME + " = ?"
                        + "AND " + RoutineContract.RoutineTable.COLUMN_DAY + " = ?",
                        selectionArgs
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Not yet implemented " + uri);
        }

        return numRowsDeleted;
    }




    //Exercise Table Query
    public Cursor query(
        Uri uri,
        String[] projection,
        String selection,
        String[] selectionArgs,
        String sortOrder){
        int match_code = uriMatcher.match(uri);
        Cursor myCursor;

        switch(match_code){

            //If the incoming URI is for all of Exercise table (so ID 100)
            case EXERCISE: {
                Log.i("WorkoutProvider", "querying for EXERCISE");

                selection = null;
                selectionArgs = null;

                myCursor = myDBHelper.getWritableDatabase().query(
                        ExerciseContract.ExerciseTable.TABLE_NAME, //Table to Query
                        projection, //Columns
                        null, // Columns for the "where" clause
                        null, // Values for the "where" clause
                        null, // columns to group by
                        null, // columns to filter by row groups
                        null // sort order
                );

                break;
            }

            //If the incoming URI is for a single row
            case EXERCISE_WITH_ID:{
                //Because URI is for single row, the _ID part is present. Get the last path segment from URI.
                //This is the _ID value, then append value to WHERE clause for query
                //Strange bug, says this line never happens
                selection = selection + "_ID = " + uri.getLastPathSegment(); //For where clause, currently based on _ID, could change
                Log.i("WorkoutProvider", "querying for EXERCISE with ID");

                myCursor = myDBHelper.getWritableDatabase().query(
                        ExerciseContract.ExerciseTable.TABLE_NAME, //Table to Query
                        projection, //Columns
                        selection, // Columns for the "where" clause
                        selectionArgs, // Values for the "where" clause
                        null, // columns to group by
                        null, // columns to filter by row groups
                        null // sort order
                );

                break;
            }
            case ROUTINE:{

                Log.i("WorkoutProvider", "querying for ROUTINE");
                /*
                myCursor = myDBHelper.getWritableDatabase().query(
                        RoutineContract.RoutineTable.TABLE_NAME, //Table to Query
                        projection, //Columns
                        selection, // Not null, will contain DAY column
                        selectionArgs, // Will contain value for day column
                        null, // columns to group by
                        null, // columns to filter by row groups
                        null // sort order
                );*/

                //Here we use a raw query, as cursor requires _ID which we dont have, but SQLite has column numbers hidden by default, which we can name ID
                myCursor = myDBHelper.getReadableDatabase().rawQuery("SELECT rowid _id," + RoutineContract.RoutineTable.COLUMN_EXERCISE_NAME + " FROM "
                        + RoutineContract.RoutineTable.TABLE_NAME + " WHERE " + selection, selectionArgs);  //Selection args contains day, selection contains query by day row
                break;
            }

            default:
                Log.e("Query", "URI not recognised");
                Log.e("ExerciseQuery", Integer.toString(match_code));
                Log.e("ExerciseQuery", uri.toString());
                throw new UnsupportedOperationException("Not yet implemented");


        }


        myCursor.setNotificationUri(getContext().getContentResolver(),uri);  //Flags if the database is updated
        return myCursor;

    }



    //May implement update features, may not  -  Implement this to handle requests to update one or more rows.
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


}
