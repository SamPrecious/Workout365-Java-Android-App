package com.example.workout365;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

//This activity just hosts the activity_finished class when not on a tablet mode
public class FinishedActivity extends AppCompatActivity {
    private ShareActionProvider mShareActionProvider;
    int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);


        score = getIntent().getExtras().getInt("Score");

        Bundle bundle = new Bundle();
        bundle.putInt("Score", score);
        FinishedFragment finishedFragment = new FinishedFragment();
        finishedFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_finished, finishedFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.finished_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);



        setShareActionIntent("I just got a score of "+ score);
        return super.onCreateOptionsMenu(menu);
    }

    private void setShareActionIntent(String shareScore){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, shareScore);
        intent.setType("text/plain");
        mShareActionProvider.setShareIntent(intent);
    }


}