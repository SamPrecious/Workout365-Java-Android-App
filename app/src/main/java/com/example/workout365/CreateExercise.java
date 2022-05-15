package com.example.workout365;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workout365.data.ExerciseContract;

import java.sql.SQLException;
import java.util.ArrayList;

public class CreateExercise extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);
        Spinner setDifficulty = (Spinner) findViewById(R.id.setExerciseDifficulty);


        String[] difficulties = {"Easy", "Medium", "Hard"};
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.difficulty_spin, difficulties);
        setDifficulty.setAdapter(adapter);

    }

    /*
    @Override
    public void onBackPressed() {
        Log.i("CreateExercise","BackPressed");
        super.onBackPressed();
    }*/

    public void saveExercise(View view){

        TextView setExerciseName = (TextView) findViewById(R.id.setExerciseName);
        TextView setExerciseDescription = (TextView) findViewById(R.id.setExerciseDescription);
        Spinner setExerciseDifficulty = (Spinner) findViewById(R.id.setExerciseDifficulty);

        String exerciseName = setExerciseName.getText().toString();
        String exerciseDescription = setExerciseDescription.getText().toString();
        String exerciseDifficulty = setExerciseDifficulty.getSelectedItem().toString();
        Log.i("PreQuery","Gonna Query");

        if(exerciseName.length() < 3 || exerciseDescription.length() < 3){ //Check if too short
            Toast.makeText(getApplicationContext(),"Fields contain at least 3 characters",Toast.LENGTH_SHORT).show();
        }else{
            //getContentResolver().delete(ExerciseContract.ExerciseTable.CONTENT_URI,null,null);



                ContentValues values = new ContentValues();
                //Here we insert the new exercise into the database
                values.put(ExerciseContract.ExerciseTable.COLUMN_EXERCISE_NAME, exerciseName); //Puts the exercise name into values, which will soon be queried. Mapped with the column_exercise_name for the URI
                values.put(ExerciseContract.ExerciseTable.COLUMN_DESCRIPTION, exerciseDescription);
                values.put(ExerciseContract.ExerciseTable.COLUMN_DIFFICULTY, exerciseDifficulty);
                //Unknown URL

                Log.i("URI",ExerciseContract.ExerciseTable.CONTENT_URI.toString()); //URI has been checked and is correct

                //Inserts may be incorrect, it doesnt like trying to parse them through

                getContentResolver().insert(ExerciseContract.ExerciseTable.CONTENT_URI, values); //Parses the new values through the URI, to insert into the database
                getContentResolver().notifyChange(ExerciseContract.ExerciseTable.CONTENT_URI, null);

                finish(); //Returns us to main activity



        }


    }

}