package com.kmutt.sit.apps.fit.core.main.firstrun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kmutt.sit.apps.fit.R;

public class FirstRunActivityV2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);

        FirstRunTask task = new FirstRunTask(this, new FirstRunTask.Callback() {
            @Override
            public void onFinish() {
                finish();
            }
        });
        task.execute();
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
