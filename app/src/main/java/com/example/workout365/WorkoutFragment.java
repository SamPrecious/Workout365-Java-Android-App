package com.example.workout365;

import android.content.Intent;
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
    ListView WorkoutList;
    Cursor results;
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
            public void onClick(View v){ //Onclick defined here instead of in XML, so we can access the function on clicking <- Begins routine

                if(WorkoutList.getCount() > 0){ //If todays routine isnt empty
                    Intent intent = new Intent(getActivity(), RunningRoutine.class);
                    intent.putExtra("day", day);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(),"Currently nothing in todays routine",Toast.LENGTH_SHORT).show();

                }

            }
        });

        return view;
    }

    //Function to load our Routine listview from database
    public void loadListView(){

        String selectionClause = RoutineContract.RoutineTable.COLUMN_DAY + " = ?";

        String[] selectionArgs = {day};  //Queries Routines based on the day today (i.e. retrieves Tuedsays routine, so the exercises set on Tuesday)
        //Query returns LEFT Join of routine table where the day is todays day.
        results = getActivity().getContentResolver().query(RoutineContract.RoutineTable.CONTENT_URI_EXERCISE, null, selectionClause, selectionArgs, null);  //Query by slightly different URI, as we want to return different values
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                view.getContext(),
                R.layout.todays_listview,
                results,
                new String[] {RoutineContract.RoutineTable.COLUMN_EXERCISE_NAME},  //For whats in the listview
                new int[]{R.id.todays_item},
                0
        );


        WorkoutList = (ListView) view.findViewById(R.id.todaysRoutine);
        WorkoutList.setAdapter(adapter);

    }

}