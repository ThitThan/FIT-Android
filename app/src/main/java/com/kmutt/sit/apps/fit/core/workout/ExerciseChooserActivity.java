package com.kmutt.sit.apps.fit.core.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kmutt.sit.apps.fit.R;
import com.kmutt.sit.apps.fit.utils.Typefaces;

public class ExerciseChooserActivity extends AppCompatActivity {

    Toolbar appBar;

    TextView tvTitle;
    Button btnFreeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_chooser);

        // init views
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        btnFreeMode = (Button) findViewById(R.id.btnFreeMode);

        // button
        btnFreeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ExerciseChooserActivity.this, WorkoutActivity.class);
                startActivity(i);
                finish();
            }
        });

        initTypefaces();
        initAppBar();
    }

    void initAppBar() {
        appBar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void initTypefaces() {
        Typefaces.init(this);
        tvTitle.setTypeface(Typefaces.Cloud_Bold);
        btnFreeMode.setTypeface(Typefaces.Cloud_Bold);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
