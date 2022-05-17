package com.example.workout365;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

public class FinishedFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finished, container, false);

        int score = getArguments().getInt("Score");

        //Compare new score to highscore
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();

        int highScore = sp.getInt("highScore",0);

        TextView scoreTV = view.findViewById(R.id.scoreTV);
        if(highScore < score){
            scoreTV.setText("You beat your highscore of " + highScore +"! New highscore: " + Integer.toString(score));
            editor.putInt("highScore", score); //Updates highscore in preferences
            editor.commit();
        }else if(highScore == score){
            scoreTV.setText("You matched your highscore of " + Integer.toString(score)+"!");
        }
        else{
            int scoreDiff = highScore-score;
            scoreTV.setText("You achieved a score of: " + Integer.toString(score) + " thats just "+ scoreDiff + " away from your record!");
        }
        //int oldHighscore = ;
        Log.i("FinishedFragment","highScore");
        Log.i("FinishedFragment",Integer.toString(highScore));

        Button finishWorkout = (Button) view.findViewById(R.id.finishWorkout);
        finishWorkout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent i = new Intent(getContext(), MainActivity.class);  //Brings us back to main activity
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        return view;
    }
}