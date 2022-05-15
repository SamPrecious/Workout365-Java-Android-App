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

public class ExerciseFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {  //Implements allows us to refresh cursor
    SimpleCursorAdapter adapter;
    ListView ExerciseList;
    View view;
    ArrayList<String> exercise = new ArrayList<>();
    private static final int EXERCISE_LOADER = 1;

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

        String[] columns = {
                ExerciseContract.ExerciseTable._ID,
                ExerciseContract.ExerciseTable.COLUMN_EXERCISE_NAME
        };
        //getContentResolver().insert(ExerciseContract.ExerciseTable.CONTENT_URI, values);
        Cursor results = getActivity().getContentResolver().query(ExerciseContract.ExerciseTable.CONTENT_URI, columns, null, null, null);

        //Cursor cursor =
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
                int id = results.getInt(0);
                Toast.makeText(getContext(),Integer.toString(id),Toast.LENGTH_SHORT).show();
                //results.getInt(0) one cursor position i gives us our SQL db position
                //results.getString: 1 = 3, 2 = test?

                /*
                TODO DELETE CODE HERE WITH ID
                 */
            }
        });

        //adapter.notifyDataSetChanged();
    }




    @Override
    public void onResume() { //We want to make sure the database hasnt changed when we view it here, so refresh each time.
        super.onResume();
        adapter.notifyDataSetChanged(); //ReQuery not necessary anymore, only need to refresh this
        /*String[] tempData = {"Today - Storm 8 / 12", "Tomorrow - Foggy 9 / 13", "Thurs - Rainy 8 / 13", "Fri - foggy 8 / 12",
                "Sat - Sunny 9 / 14", "Sun - Sunny 10 / 15", "Mon - Sunny 11 / 15","Today - Storm 8 / 12", "Tomorrow - Foggy 9 / 13", "Thurs - Rainy 8 / 13", "Fri - foggy 8 / 12",
                "Sat - Sunny 9 / 14", "Sun - Sunny 10 / 15", "Mon - Sunny 11 / 15"};*/

        //ArrayList<String> exercise = new ArrayList<>();
        //adapter = new ArrayAdapter(getContext(), R.layout.exercise_listview, R.id.exercise_item, tempData); //Exercise where tempData normally is
        //




    }



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.i("ExerciseFragment","Creating Loader");

        String[] columns = {
                ExerciseContract.ExerciseTable._ID,
                ExerciseContract.ExerciseTable.COLUMN_EXERCISE_NAME
        };
        Loader<Cursor> loader = new CursorLoader(view.getContext(), ExerciseContract.ExerciseTable.CONTENT_URI,columns,null,null,null);
        return loader;
    }




    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}