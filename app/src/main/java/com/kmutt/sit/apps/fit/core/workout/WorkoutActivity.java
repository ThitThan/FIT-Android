package com.kmutt.sit.apps.fit.core.workout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.kmutt.sit.apps.fit.R;
import com.kmutt.sit.apps.fit.ijoint.sensor.FitOrientation;
import com.kmutt.sit.apps.fit.utils.Typefaces;

public class WorkoutActivity extends AppCompatActivity {

    Button btnStop;
    FitOrientation mOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        // init views
        btnStop = (Button) findViewById(R.id.btnStop);

        // button
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initTypefaces();
    }

    void initTypefaces() {
        Typefaces.init(this);
        btnStop.setTypeface(Typefaces.Cloud_Bold);
    }

//    boolean shouldGoBack = false;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("หยุดออกกำลังกาย?");
//            builder.setMessage(message);
        builder.setPositiveButton("หยุด", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                WorkoutActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("ยกเลิก", null);
        builder.create().show();
    }
}
