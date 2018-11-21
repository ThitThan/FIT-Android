package com.kmutt.sit.apps.fit;

import android.app.Application;

import com.kmutt.sit.apps.fit.core.food.realm.Food;
import com.kmutt.sit.apps.fit.core.food.realm.Migration;
import com.kmutt.sit.apps.fit.core.main.dailydata.calories.realm.CaloriesEntry;
import com.kmutt.sit.apps.fit.core.main.dailydata.realm.DailyData;
import com.kmutt.sit.apps.fit.core.main.users.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.annotations.RealmModule;

/**
 * Created by Freshy on 7/24/2017 AD.
 */

public class FITApplication extends Application {

    public static final String DB_FILE_NAME_PRELOADED = "preloaded.realm";
    public static final String DB_FILE_NAME_USER_DATA = "user_data.realm";

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         *   Realm
         */
        Realm.init(this);

        /**
         *   User
         */
        User.createUserIfHavent(this, "Sally", User.BIO_GENDER_MALE, 60, 175, null);
//        if (User.getCurrentUser(this) == null) {
//        }
    }

    /**
     *   PRELOADED
     */
    static RealmConfiguration preloadedConfig;
    public static Realm getPreloadedDataRealm() {
        if (preloadedConfig == null) {
            preloadedConfig = new RealmConfiguration.Builder()
                    .name(DB_FILE_NAME_PRELOADED)
                    .schemaVersion(0)
                    .migration(new Migration())
                    .modules(new PreloadedModule())
                    .build();
        }
        return Realm.getInstance(preloadedConfig);
    }

    /**
     *   USER DATA
     */
    static RealmConfiguration userDataConfig;
    public static Realm getUserDataRealm() {
        if (userDataConfig == null) {
            userDataConfig = new RealmConfiguration.Builder()
                    .name(DB_FILE_NAME_USER_DATA)
                    .schemaVersion(2)
                    .migration(new Migration())
                    .modules(new UserDataModule())
                    .build();
        }
        return Realm.getInstance(userDataConfig);
    }

    @RealmModule(classes = { Food.class })
    public static class PreloadedModule{

    }

    @RealmModule(classes = { User.class, DailyData.class, CaloriesEntry.class })
    public static class UserDataModule{

    }
}
