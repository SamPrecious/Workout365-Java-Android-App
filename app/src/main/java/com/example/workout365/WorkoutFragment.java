package com.example.workout365;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workout365.data.RoutineContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class WorkoutFragment extends Fragment {
    View view;
    String day;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_workout, container, false);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime()); //This returns the current day. Pattern EEEE represents full day


        TextView currentDay = (TextView) view.findViewById(R.id.tvDay);
        currentDay.setText("Today is "+ day);

        loadListView();

        Button button = (Button) view.findViewById(R.id.beginWorkout);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){ //Onclick defined here instead of in XML, so we can access the function on clicking


            }
        });

        return view;
    }

    //Function to load our Routine listview from database
    public void loadListView(){

        String selectionClause = RoutineContract.RoutineTable.COLUMN_DAY + " = ?";

        String[] selectionArgs = {day};  //Queries Routines based on the day today (i.e. retrieves Tuedsays routine, so the exercises set on Tuesday)
        //Query returns LEFT Join of routine table where the day is todays day.
        Cursor results = getActivity().getContentResolver().query(RoutineContract.RoutineTable.CONTENT_URI_EXERCISE, null, selectionClause, selectionArgs, null);  //Query by slightly different URI, as we want to return different values
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                view.getContext(),
                R.layout.todays_listview,
                results,
                new String[] {RoutineContract.RoutineTable.COLUMN_EXERCISE_NAME},  //For whats in the listview
                new int[]{R.id.todays_item},
                0
        );

        /*
        RESULTS FORMAT:
            0 - ID
            1 - Exercise Name
            2 - Reps
            3 - Sets
            4 - Difficulty
            5 - Description
         */
        results.moveToFirst();
        results.moveToNext();
        Log.i("WorkoutFragment", results.getString(1));
        Log.i("WorkoutFragment", results.getString(2));
        Log.i("WorkoutFragment", results.getString(3));
        Log.i("WorkoutFragment", results.getString(4));
        Log.i("WorkoutFragment", results.getString(5));
        ListView WorkoutList = (ListView) view.findViewById(R.id.todaysRoutine);
        WorkoutList.setAdapter(adapter);

        /*
        TODO
            Send cursor through to new Activity
            OnCreate moveToFirst
            OnButtonPress moveToNext (if valid)
            Add current score (difficulty 1-3 * reps * sets)
            When no longer valid, finish
            If score higher than previously saved high score congratulate user (else maybe tell them the difference)


         */
    }

}