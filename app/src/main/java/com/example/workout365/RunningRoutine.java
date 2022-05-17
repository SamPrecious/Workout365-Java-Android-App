package com.example.workout365;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.example.workout365.data.RoutineContract;

public class RunningRoutine extends AppCompatActivity {
    Cursor results;
    int score = 0;
    String exerciseName;
    int reps;
    int sets;
    String difficulty;
    String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_routine);

        String day = getIntent().getStringExtra("day");
        String[] selectionArgs = {day};
        String selectionClause = RoutineContract.RoutineTable.COLUMN_DAY + " = ?";
        results = getContentResolver().query(RoutineContract.RoutineTable.CONTENT_URI_EXERCISE, null, selectionClause, selectionArgs, null);  //Query by slightly different URI, as we want to return different values


        results.moveToFirst(); //Starts at the beginning of cursor
        setVariables();


        //results.moveToNext();


    }

    public void nextExercise(){
        Log.i("RunningRoutine","Current Score: "+ score);
        if(results.moveToNext()){
            setVariables();
        }else{ //here we have reached the end of our workout
            finish(); //Finish this activity so we cant return before starting new one:
            Log.i("RunningRoutine","This is reached");
            Intent intent = new Intent(RunningRoutine.this, FinishedActivity.class);
            intent.putExtra("Score",score);
            startActivity(intent);
        }
    }

    public void skipExercise(View view){ //Skips the exercise, does the same as Done but doesnt add on the score
        nextExercise();
    }

    public void calculateScore(View view){
        switch(difficulty){
            case "Easy":  //Easy has a multiplier of 1, medium 2 and hard 3, increasing the score based on exercise difficulty
                score += (reps * sets);
                break;
            case "Medium":
                score += 2*(reps*sets);
                break;
            case "Hard":
                score += 3*(reps*sets);
                break;
        }
        nextExercise(); //Move to the next one after calculating score (score not added if exercise skipped)
    }

    //ON NEXT Call Calculation, then Call set variables for new variables

    @SuppressLint("SetTextI18n")
    public void setVariables(){
        /*
        RESULTS FORMAT:
            0 - ID
            1 - Exercise Name
            2 - Reps
            3 - Sets
            4 - Difficulty
            5 - Description
         */

        exerciseName = results.getString(1);
        reps = results.getInt(2); //Integer.parseInt(results.getString(2))
        sets = results.getInt(3);
        difficulty = results.getString(4);
        description = results.getString(5);

        //Updates page with current exercise
        TextView currentlyOn = findViewById(R.id.currentlyOn);
        currentlyOn.setText("Currently On: "+exerciseName);

        TextView currentDescription = findViewById(R.id.currentDescription);
        currentDescription.setText(description);

        TextView currentReps = findViewById(R.id.currentReps);
        currentReps.setText("Reps: " + reps);

        TextView currentSets = findViewById(R.id.currentSets);
        currentSets.setText("Sets: " + sets);

        TextView currentScore = findViewById(R.id.currentScore);
        currentScore.setText("Current Score: " + score);

        Log.i("RunningRoutine", Integer.toString(reps));
        Log.i("RunningRoutine", Integer.toString(sets));
        Log.i("RunningRoutine", difficulty);
    }
}