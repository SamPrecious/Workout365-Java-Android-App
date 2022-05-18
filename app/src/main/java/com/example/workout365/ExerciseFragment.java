package com.example.workout365;


import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentProvider;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.UserDictionary;
import android.widget.AdapterView;
import android.widget.CursorAdapter;


import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workout365.data.ExerciseContract;
import com.example.workout365.data.WorkoutProvider;

import java.util.ArrayList;

public class ExerciseFragment extends Fragment {  //Implements allows us to refresh cursor
    SimpleCursorAdapter adapter;
    ListView ExerciseList;
    View view;
    ArrayList<String> exercise = new ArrayList<>();

    private static final int EXERCISE_LOADER = 1;
    String[] columns = {
            ExerciseContract.ExerciseTable._ID,
            ExerciseContract.ExerciseTable.COLUMN_EXERCISE_NAME
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getLoaderManager().initLoader(EXERCISE_LOADER, null, this);
        //adapter.notifyDataSetChanged();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        view = inflater.inflate(R.layout.fragment_exercise, container, false);
        String[] columns = {
                ExerciseContract.ExerciseTable._ID,
                ExerciseContract.ExerciseTable.COLUMN_EXERCISE_NAME
        };




        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        loadListview();

        //adapter.notifyDataSetChanged();
    }

    public void loadListview(){
        //getContentResolver().insert(ExerciseContract.ExerciseTable.CONTENT_URI, values);
        Cursor results = getActivity().getContentResolver().query(ExerciseContract.ExerciseTable.CONTENT_URI, columns, null, null, null);

        adapter = new SimpleCursorAdapter(
                view.getContext(),
                R.layout.exercise_listview,
                results,
                new String[] {ExerciseContract.ExerciseTable.COLUMN_EXERCISE_NAME},
                new int[]{R.id.exercise_item},
                0
        );

        ExerciseList = (ListView) view.findViewById(R.id.listview);
        ExerciseList.setAdapter(adapter);


        ExerciseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String name = results.getString(1);

                String selectionClause = ExerciseContract.ExerciseTable.COLUMN_EXERCISE_NAME + " = ?";  //OLD String selectionClause = ExerciseContract.ExerciseTable._ID + " = ?";
                String[] selectionArgs = {name};


                getActivity().getContentResolver().delete(
                        ExerciseContract.ExerciseTable.CONTENT_URI,
                        selectionClause,
                        selectionArgs
                );

                loadListview(); //We have to recall this after changing the data, as if we dont recall everything there are issues (like the onClickListener getting stuck on the deleted ID)
                /*
                TODO DELETE CODE HERE WITH ID
                 */
            }
        });
    }




    @Override
    public void onResume() { //We want to make sure the database hasnt changed when we view it here, so refresh each time.
        super.onResume();
        Log.i("ExerciseFragment", "OnResume");
        loadListview();




    }




}