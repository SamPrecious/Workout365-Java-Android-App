package com.example.workout365;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewFirebase extends AppCompatActivity {
    ArrayList<String> list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_firebase);
        FirebaseFirestore databaseReference = FirebaseFirestore.getInstance(); //Gets exercise table from firebase


        databaseReference.collection("exercise")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot results = task.getResult();
                        String[] exercises = new String[results.size()];
                        int i = 0;
                        for (QueryDocumentSnapshot document : results) {
                            exercises[i] = document.getString("exercise_name");
                            i++;
                        }
                        Log.i("ViewFirebase", Integer.toString(i));
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exercises);
                        ListView firebaseList = (ListView) this.findViewById(R.id.firebaseLV);
                        firebaseList.setAdapter(adapter);

                    }
                });

    }
}