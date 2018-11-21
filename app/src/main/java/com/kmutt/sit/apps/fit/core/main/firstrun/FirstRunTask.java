package com.kmutt.sit.apps.fit.core.main.firstrun;

import android.app.Activity;
import android.os.AsyncTask;

import com.kmutt.sit.apps.fit.FITApplication;
import com.kmutt.sit.apps.fit.R;
import com.kmutt.sit.apps.fit.core.food.realm.Food;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Freshy on 7/25/2017 AD.
 */

public class FirstRunTask extends AsyncTask {

    Activity mContext;
    Callback mCallback;

    public FirstRunTask(Activity context, Callback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        copyBundledRealmFile(mContext.getResources().openRawResource(R.raw.food_db_compact), FITApplication.DB_FILE_NAME_PRELOADED);

//        realm = Realm.getDefaultInstance();
//        realm.close();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

//        Food.setFoodDataPreloaded(mContext, true);

        if (mCallback != null)
            mCallback.onFinish();
    }

    private String copyBundledRealmFile(InputStream inputStream, String outFileName) {
        try {
            File file = new File(mContext.getFilesDir(), outFileName);
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

    public interface Callback {
        void onFinish();
    }
}
