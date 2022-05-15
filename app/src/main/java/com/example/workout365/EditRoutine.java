package com.example.workout365;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.workout365.data.ExerciseContract;
import com.example.workout365.data.RoutineContract;

public class EditRoutine extends AppCompatActivity {
    Spinner setDay;
    Spinner addExercises;
    Cursor results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_routine);

        setDay = (Spinner) findViewById(R.id.setRoutineDay);
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday","Friday","Saturday","Sunday"};

        ArrayAdapter dayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.day_spin, days);
        setDay.setAdapter(dayAdapter);



        //TODO we want the arraylist input to be all exercise names
       // Spinner addExercise = (Spinner) findViewById(R.id.addExercise);
       // ArrayAdapter exerciseAdapter = new ArrayAdapter(getApplicationContext(), R.layout.exercise_spin, days);
        //addExercise.setAdapter(exerciseAdapter);

        String[] columns = {
                ExerciseContract.ExerciseTable._ID,
                ExerciseContract.ExerciseTable.COLUMN_EXERCISE_NAME
        };
        //Here we query the Exercise table, so we can get a list of the exercises for our spinner
        results = getContentResolver().query(ExerciseContract.ExerciseTable.CONTENT_URI, columns, null, null, null);

        //Cursor cursor =
        SimpleCursorAdapter exerciseAdapter = new SimpleCursorAdapter(
                this,
                R.layout.exercise_spin,
                results,
                new String[] {ExerciseContract.ExerciseTable.COLUMN_EXERCISE_NAME},
                new int[]{R.id.exercise_2_item},
                0
        );
        addExercises = (Spinner) findViewById(R.id.addExercise);
        addExercises.setAdapter(exerciseAdapter);

        //Gets default sets and reps values
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String reps = p.getString("reps","5");
        String sets = p.getString("sets", "5");

        EditText repsTV = findViewById(R.id.repsEdit);
        EditText setsTV = findViewById(R.id.setsEdit);
        repsTV.setText(reps);  //Sets default values for convenience. Can be changed
        setsTV.setText(sets);

    }

    public void saveEdit(View view){
        EditText repsTV = findViewById(R.id.repsEdit);
        EditText setsTV = findViewById(R.id.setsEdit);

        String repsString = repsTV.getText().toString();
        String setsString = setsTV.getText().toString();
        if(repsString.length() == 0 || setsString.length() == 0 ){
            Log.i("Failed","Failed");
            Toast.makeText(getApplicationContext(),"Please fill out all fields",Toast.LENGTH_SHORT).show();
        }else{

            int reps = Integer.parseInt(repsString);
            int sets = Integer.parseInt(setsString);
            if(reps == 0 || sets == 0){
                Toast.makeText(getApplicationContext(),"Please make reps and sets non-0 values",Toast.LENGTH_SHORT).show();
            }
            else{
                String exerciseName = results.getString(1); //Gives us the exercise selected
                String daySelected = setDay.getSelectedItem().toString();

                Toast.makeText(getApplicationContext(),daySelected + " " + exerciseName,Toast.LENGTH_SHORT).show();


                //Here we insert the new values:
                ContentValues values = new ContentValues();
                values.put(RoutineContract.RoutineTable.COLUMN_EXERCISE_NAME, exerciseName);
                values.put(RoutineContract.RoutineTable.COLUMN_DAY, daySelected);
                values.put(RoutineContract.RoutineTable.COLUMN_REPS, reps);
                values.put(RoutineContract.RoutineTable.COLUMN_SETS, sets);

                getContentResolver().insert(RoutineContract.RoutineTable.CONTENT_URI, values); //Parses the new values through the URI, to insert into the database


            }
        }


    }

}