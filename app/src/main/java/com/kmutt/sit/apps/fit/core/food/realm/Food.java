package com.kmutt.sit.apps.fit.core.food.realm;

import android.content.Context;
import android.content.SharedPreferences;

import com.kmutt.sit.apps.fit.FITApplication;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Freshy on 7/24/2017 AD.
 */

public class Food extends RealmObject {
    private long id;
    @Required
    private String food_name;
    private long amount;
    @Required
    private String unit;
    private long kcal;

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getFoodName() { return food_name; }

    public void setFoodName(String food_name) { this.food_name = food_name; }

    public long getAmount() { return amount; }

    public void setAmount(long amount) { this.amount = amount; }

    public String getUnit() { return unit; }

    public void setUnit(String unit) { this.unit = unit; }

    public long getKcal() { return kcal; }

    public void setKcal(long kcal) { this.kcal = kcal; }

    public static boolean isFoodDataPreloaded() {
        Realm realm = FITApplication.getPreloadedDataRealm();
        return realm.where(Food.class).count() > 0;
    }

//    public static final String SHARED_PREF_FOOD_DATA = "pref_food_data";
//    public static final String KEY_IS_DATA_PRELOADED = "isDataPreloaded";
//    public static boolean isFoodDataPreloaded(Context context) {
//        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_FOOD_DATA, Context.MODE_PRIVATE);
//        return pref.getBoolean(KEY_IS_DATA_PRELOADED, false);
//    }
//    public static void setFoodDataPreloaded(Context context, boolean value) {
//        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_FOOD_DATA, Context.MODE_PRIVATE);
//        pref.edit().putBoolean(KEY_IS_DATA_PRELOADED, value).apply();
//    }
}
