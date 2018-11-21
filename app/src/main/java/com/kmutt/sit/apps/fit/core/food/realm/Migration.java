package com.kmutt.sit.apps.fit.core.food.realm;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

/**
 * Created by Freshy on 7/24/2017 AD.
 */

public class Migration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        Log.d(getClass().getSimpleName(), "migrate from version \'" + oldVersion + "\' to version \'" + newVersion + "\'");

//        RealmSchema realmSchema = realm.getSchema();
//        if (oldVersion == 0) {
//            realmSchema.create()
//        }
    }
}
