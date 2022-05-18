package com.example.workout365;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    WorkoutFragment workoutFragment = new WorkoutFragment();
    RoutineFragment routineFragment = new RoutineFragment();
    ExerciseFragment exerciseFragment = new ExerciseFragment();
    enum buttonState{  //An enum for our button states  - Could save if we want to reopen same page?
        RoutineFragment,
        ExerciseFragment,
        WorkoutFragment
    }
    buttonState currentFragmentState = buttonState.WorkoutFragment;




    private GestureDetectorCompat gestureDetector;
    private String fragmentState; //The current state the fragment is in, used so we can determine where gestures move to
    private static final int REQUEST_IMAGE_CAPTURE = 52;  //unique id for capturing image
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //viewWorkout(null); //Automatically calls the routine page, so we start here
        updateWorkout();
        gestureDetector = new GestureDetectorCompat(this, new GestureListener());  //Initialises class to detect gestures

        setAlarm();


        createNotificationChannel();
    }


    /*
      Creates a channel for notifications!
      Only necessary for OREO and above, so we only do it in these scenarios
     */
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification","ReminderChannel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Our notification channel for reminders & motivational notifications");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }





    public void refreshScreen(){
        rebootService();
        switch(currentFragmentState){  //Determines what button state we are currently in, and runs related function
            case RoutineFragment:
                Log.i("refreshing","Routine");
                updateRoutine();
                break;
            case ExerciseFragment:
                Log.i("refreshing","Exercise");
                updateExercise();
                break;
            case WorkoutFragment:
                Log.i("refreshing","Workout");
                updateWorkout();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + currentFragmentState);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){   //By using dispatchTouchEvent instead of onTouchEvent, the touchEvent works over items like views
        this.gestureDetector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }


    //Casts the update commands to normal views
    public void viewWorkout(View view){ //Instead of calling viewWorkout(null) when called within Gestures, we have simplified this, by having an updateWorkout function, making it more simple
        rebootService();
        updateWorkout();
    }
    public void viewRoutine(View view){
        rebootService();
        updateRoutine();
    }
    public void viewExercise(View view){
        rebootService();
        updateExercise();
    }


    //Resets the service idle countdown (as we have shown we arent idle)
    public void rebootService(){
        stopService(new Intent(getBaseContext(), CustomService.class));
        startService(new Intent(getBaseContext(), CustomService.class));
    }

    public void variableButton(View view){
        switch(currentFragmentState){  //Determines what button state we are currently in, and runs related function
            case RoutineFragment:
                editRoutine();
                break;
            case ExerciseFragment:
                addExercise();
                break;
        }
    }

    public void editRoutine(){

        Intent intent = new Intent(MainActivity.this, EditRoutine.class);
        startActivity(intent);

    }
    public void addExercise(){
        Toast.makeText(getApplicationContext(),"Adding Exercise",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, CreateExercise.class);
        startActivity(intent);
    }




    //Handles the fragment updating
    public void updateWorkout(){
        //ArrayList<String> workouts = new ArrayList<>();

        //Sets the text to the current name relating to the fragment we are viewing
        TextView mainTitle = findViewById(R.id.mainTitle);
        mainTitle.setText("Workouts");
        ImageButton editButton = findViewById(R.id.variableButton);
        editButton.setVisibility(View.GONE); //Hides the button from sight, as it is not used on this page
        fragmentState = "workout";

        Context context = getApplicationContext();

        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, workoutFragment).commit();




    }


    /*
    Sets an alarm for 24 hours. The goal is that if the user opened the app at this time, it will probably fit with their schedule tomorrow as well
     */
    public void setAlarm(){
        Intent intent = new Intent(MainActivity.this, RemindMeBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //Sets alarm in 24 hours
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    //Requests permission for camera
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                } else {
                    Toast.makeText(getApplicationContext(),"Cannot open Camera without permission",Toast.LENGTH_SHORT).show();

                }
            });

    //Sets profile picture on button
    public void checkPermissions(View view){

        //Checks if user has granted camera permission
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            //Yes so try it
            setProfilePicture();
        }else{
            //No so requests permission
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                //If the user has now accepted the permission, then take the picture
                setProfilePicture();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){ //if successfull

            Bundle extras = data.getExtras();
            if(extras != null){
                Bitmap profileBitmap =  (Bitmap) extras.get("data");
                ImageButton profilePic = (ImageButton) findViewById(R.id.profilePic);
                profilePic.setImageBitmap(profileBitmap);
            }
        }
    }

    public void setProfilePicture(){
        Intent takeProfilePhoto =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if (takeProfilePhoto.resolveActivity(getPackageManager()) == null) {
        startActivityForResult(takeProfilePhoto, REQUEST_IMAGE_CAPTURE);

    }

    public void updateRoutine(){

        /*
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_DAY,
                AlarmManager.INTERVAL_DAY, pendingIntent);*/


        /* Creates notif in 5 secs
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long timeAtButtonClick = System.currentTimeMillis();
        long fiveSeconds = 500 * 10;


        //RTC_WAKEUP wakes up the device to shoot the pending intent
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                timeAtButtonClick + fiveSeconds,
                pendingIntent);*/



        currentFragmentState = buttonState.RoutineFragment;  //This tells us the button should do the routine function


        //ArrayList<String> workouts = new ArrayList<>();
        TextView mainTitle = findViewById(R.id.mainTitle);
        mainTitle.setText("Routines");
        ImageButton editButton = findViewById(R.id.variableButton);
        editButton.setImageResource(R.drawable.editicon);  //Changes the image to the create icon
        editButton.setVisibility(View.VISIBLE); //Shows the button
        fragmentState = "routine";

        Context context = getApplicationContext();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, routineFragment).commit();

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putSerializable("CurrentState", currentFragmentState);  //Saves the current enum of our state
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        currentFragmentState = (buttonState) savedInstanceState.get("CurrentState");
        refreshScreen();
    }

    public void updateExercise(){
        currentFragmentState = buttonState.ExerciseFragment;  //This tells us the button should do the routine function

        //ArrayList<String> workouts = new ArrayList<>();
        TextView mainTitle = findViewById(R.id.mainTitle);
        mainTitle.setText("Exercises");
        ImageButton editButton = findViewById(R.id.variableButton);
        editButton.setImageResource(R.drawable.plussignorange);  //Changes the image to the create icon
        editButton.setVisibility(View.VISIBLE); //Shows the button
        fragmentState = "exercise";

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
        else if(id == R.id.help){
            help();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }


    public void help(){
        Intent webIntent = new Intent(MainActivity.this, UserGuide.class);
        startActivity(webIntent);
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



    //This gesture listener allows us to move between home pages with swipes
    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
    /*
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Toast.makeText(MainActivity.this, "Fling " + Float.toString(distanceX), Toast.LENGTH_SHORT).show();

            return super.onScroll(e1, e2, distanceX, distanceY);
        }*/

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //Switch to the right page
            Log.i("VelocityX", Float.toString(velocityX));
            int velocityThreshold = 1000;  //This is the sensitivity that determines how hard it is to activate a gesture (stops accidental swipes)
            if(fragmentState == "workout"){
                if(velocityX<velocityThreshold){
                    Log.i("Moving", "Moving to Routine");
                    updateRoutine(); //Calls the viewRoutine class
                }
            }else if(fragmentState == "routine"){
                if(velocityX>velocityThreshold){
                    Log.i("Moving", "Moving to Workout");
                    updateWorkout();
                }else if(velocityX<velocityThreshold){
                    Log.i("Moving", "Moving to Exercise");

                    updateExercise();
                }
            }else if(fragmentState == "exercise"){
                if(velocityX>velocityThreshold){
                    Log.i("Moving", "Moving to Routine");

                    updateRoutine();
                }
            }


            return super.onFling(e1, e2, velocityX, velocityY);
        }
        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }
    }





}