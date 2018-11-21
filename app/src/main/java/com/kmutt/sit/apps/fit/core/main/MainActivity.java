package com.kmutt.sit.apps.fit.core.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.kmutt.sit.apps.fit.R;
import com.kmutt.sit.apps.fit.core.workout.ExerciseChooserActivity;
import com.kmutt.sit.apps.fit.utils.Typefaces;

public class MainActivity extends AppCompatActivity {

    Button btnBeginExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init views
        btnBeginExercise = (Button) findViewById(R.id.btnBeginExercise);

        // button
        btnBeginExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ExerciseChooserActivity.class);
                startActivity(i);
            }
        });

        initTypefaces();
    }

    void initTypefaces() {
        Typefaces.init(this);
        btnBeginExercise.setTypeface(Typefaces.Cloud_Bold);
    }
}
