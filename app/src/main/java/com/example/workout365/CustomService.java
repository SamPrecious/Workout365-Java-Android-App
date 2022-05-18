package com.example.workout365;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

//This service acts as an idle timeout service
//This aims to help motivation, as if a user is procrastinating we can kick them back into gear
public class CustomService extends Service {
    private static Timer timer = new Timer();

    @Override
    public IBinder onBind(Intent arg0){
        //On bind enables us to bind activity to serivce (lets activity directly access members and methods in service), currently returns null as not in use
        return null;
    }
    //If gone for more than 30 seconds, toast us when we are back to say HI!
    /*
    public int onStartCommand(Intent intent, int flags, int startId){
        return START_STICKY; //When using start sticky, service keeps going until explictly stopped
    }*/

    public void startService(){
        Log.i("Runnin","runnin");
        timer.schedule(new runTimer(), 20000, 20000); //First is the delay, second is the time in milliseconds between operations
    }

    private class runTimer extends TimerTask
    {
        public void run()
        {
            Log.i("Damn Son", "Teresting");
            toastHandler.sendEmptyMessage(0);
        }
    }

    //Allows us to call toast from within run
    private final Handler toastHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Toast.makeText(getApplicationContext(), "Come back", Toast.LENGTH_SHORT).show();
        }
    };

    //Could be called say, on button press
    public void onDestroy(){
        timer.cancel();
        super.onDestroy();
        Toast.makeText(this,"Service Stopped", Toast.LENGTH_SHORT).show();
    }


    //Here we have the service that runs throughout
    @Override
    public void onCreate(){
        super.onCreate();
        //Toast.makeText(this,"Service Started", Toast.LENGTH_SHORT).show();

        startService();
    }



}
