package com.example.workout365;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter workoutAdapter;
    WorkoutFragment workoutFragment = new WorkoutFragment();
    RoutineFragment routineFragment = new RoutineFragment();
    ExerciseFragment exerciseFragment = new ExerciseFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewWorkout(null); //Automatically calls the routine page, so we start here


    }

    public void viewWorkout(View view){
        //Sets the text to the current name relating to the fragment we are viewing
        TextView mainTitle = findViewById(R.id.mainTitle);
        mainTitle.setText("Workouts");
        ImageButton editButton = findViewById(R.id.editButton);
        editButton.setVisibility(View.GONE); //Hides the button from sight, as it is not used on this page

        updateWorkout();
    }

    public void viewRoutine(View view){
        TextView mainTitle = findViewById(R.id.mainTitle);
        mainTitle.setText("Routines");
        ImageButton editButton = findViewById(R.id.editButton);

        editButton.setImageResource(R.drawable.editicon);  //Makes the button have the edit icon
        editButton.setVisibility(View.VISIBLE); //Shows the button

        updateRoutine();
    }
    public void viewExercise(View view){
        TextView mainTitle = findViewById(R.id.mainTitle);
        mainTitle.setText("Exercises");
        ImageButton editButton = findViewById(R.id.editButton);

        editButton.setImageResource(R.drawable.plussignorange);
        editButton.setVisibility(View.VISIBLE); //Shows the button

        updateExercise();
    }



    //Handles the fragment updating
    public void updateWorkout(){
        //ArrayList<String> workouts = new ArrayList<>();

        Context context = getApplicationContext();

        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, workoutFragment).commit();

    }

    public void updateRoutine(){
        //ArrayList<String> workouts = new ArrayList<>();

        Context context = getApplicationContext();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, routineFragment).commit();

    }

    public void updateExercise(){
        //ArrayList<String> workouts = new ArrayList<>();

        Context context = getApplicationContext();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, exerciseFragment).commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_find_gym) {
            //if Refresh, do something here
            findGym();

        }else if (id == R.id.action_settings) {
            //if Settings, start SettingsActivity with an explicit intent
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }


    public void findGym(){

        Uri geoLoc = Uri.parse("geo:0,0?q="+"Gyms near me");  //By querying maps with 'Gyms Near Me' we get a list of the gyms near us
        //create an explicit intent
        Intent geointent = new Intent(Intent.ACTION_VIEW);
        //Set the URI com.example.lab32.data this intent is operating on
        geointent.setData(geoLoc);
        //check if there is an app that can handle the implicit intent
        if (geointent.resolveActivity(getPackageManager()) != null)
        {
            //start the activity that can handle this intent
            startActivity(geointent);
        }
        else
        { Toast.makeText(getApplicationContext(),"No app can view maps on your device.",Toast.LENGTH_SHORT).show();
        }
    }


}