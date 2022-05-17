package com.example.workout365;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.workout365.data.ExerciseContract;
import com.example.workout365.data.RoutineContract;


public class RoutineFragment extends Fragment {
    Spinner getDay;
    String day;
    View view;
    ListView routineList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_routine, container, false);


        getDay = (Spinner) view.findViewById(R.id.getDay);
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday","Friday","Saturday","Sunday"};

        ArrayAdapter dayAdapter = new ArrayAdapter(getContext(), R.layout.day_spin, days);
        getDay.setAdapter(dayAdapter);

        getDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                loadListView();
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadListView(); //Refreshes list view
    }

    public void loadListView(){
        String[] columns = {
                RoutineContract.RoutineTable.COLUMN_EXERCISE_NAME
        };
        String selectionClause = RoutineContract.RoutineTable.COLUMN_DAY + " = ?";
        day = getDay.getSelectedItem().toString(); //Gets day in spinner
        String[] selectionArgs = {day};  //Queries Routines based on the current day.
       Cursor results = getActivity().getContentResolver().query(RoutineContract.RoutineTable.CONTENT_URI, columns, selectionClause, selectionArgs, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                view.getContext(),
                R.layout.fullroutine_listview,
                results,
                new String[] {RoutineContract.RoutineTable.COLUMN_EXERCISE_NAME},
                new int[]{R.id.fullroutine_item},
                0
        );


        routineList = (ListView) view.findViewById(R.id.viewRoutine);
        routineList.setAdapter(adapter);



    }
}