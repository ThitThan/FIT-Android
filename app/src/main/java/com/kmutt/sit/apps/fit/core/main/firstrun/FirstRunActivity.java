package com.kmutt.sit.apps.fit.core.main.firstrun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmutt.sit.apps.fit.FITApplication;
import com.kmutt.sit.apps.fit.R;
import com.kmutt.sit.apps.fit.core.food.realm.Food;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;

public class FirstRunActivity extends AppCompatActivity {

//    public static final String RESULT_ARG_FOOD_NAME = "food_name";
//    public static final String RESULT_ARG_FOOD_KCAL = "food_kcals";

    public static final String TAG = FirstRunActivity.class.getSimpleName();

    private LinearLayout rootView = null;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);

        rootView = ((LinearLayout) findViewById(R.id.rootView));
        rootView.removeAllViews();

        // 3 versions of the databases for testing. Normally you would only have one.
        copyBundledRealmFile(this.getResources().openRawResource(R.raw.food_db_compact), FITApplication.DB_FILE_NAME_PRELOADED);
//        copyBundledRealmFile(this.getResources().openRawResource(R.raw.default1), "default1.realm");
//        copyBundledRealmFile(this.getResources().openRawResource(R.raw.default2), "default2.realm");

        // When you create a RealmConfiguration you can specify the version of the schema.
        // If the schema does not have that version a RealmMigrationNeededException will be thrown.
//        RealmConfiguration config0 = new RealmConfiguration.Builder()
//                .name(DB_FILE_NAME_PRELOADED)
//                .schemaVersion(1)
//                .migration(new Migration())
//                .build();

//        realm = Realm.getInstance(config0);
        realm = FITApplication.getPreloadedDataRealm();
        showStatus("Default0");
        showStatus(realm);
        realm.close();

//        // Or you can add the migration code to the configuration. This will run the migration code without throwing
//        // a RealmMigrationNeededException.
//        RealmConfiguration config1 = new RealmConfiguration.Builder()
//                .name("default1.realm")
//                .schemaVersion(3)
//                .migration(new Migration())
//                .build();
//
//        realm = Realm.getInstance(config1); // Automatically run migration if needed
//        showStatus("Default1");
//        showStatus(realm);
//        realm.close();
//
//        // or you can set .deleteRealmIfMigrationNeeded() if you don't want to bother with migrations.
//        // WARNING: This will delete all data in the Realm though.
//        RealmConfiguration config2 = new RealmConfiguration.Builder()
//                .name("default2.realm")
//                .schemaVersion(3)
//                .deleteRealmIfMigrationNeeded()
//                .build();
//
//        realm = Realm.getInstance(config2);
//        showStatus("default2");
//        showStatus(realm);
//        realm.close();
    }

    private String copyBundledRealmFile(InputStream inputStream, String outFileName) {
        try {
            File file = new File(this.getFilesDir(), outFileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String realmString(Realm realm) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Food food: realm.where(Food.class).findAll()) {
            stringBuilder.append(food.toString()).append("\n");
        }

        return (stringBuilder.length() == 0) ? "<data was deleted>" : stringBuilder.toString();
    }

    private void showStatus(Realm realm) {
        showStatus(realmString(realm));
    }

    private void showStatus(String txt) {
        Log.i(TAG, txt);
        TextView tv = new TextView(this);
        tv.setText(txt);
        rootView.addView(tv);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
