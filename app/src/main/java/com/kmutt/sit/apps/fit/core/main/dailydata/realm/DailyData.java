package com.kmutt.sit.apps.fit.core.main.dailydata.realm;

import com.kmutt.sit.apps.fit.FITApplication;
import com.kmutt.sit.apps.fit.utils.DateTimeUtil;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
//import io.realm.annotations.Index;

/**
 * Created by Freshy on 7/26/2017 AD.
 */

public class DailyData extends RealmObject {
//    @Index
//    long id;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getCurrentKCal() {
        return currentKCal;
    }

    public void setCurrentKCal(double currentKCal) {
        this.currentKCal = currentKCal;
    }

    public double getMinKCal() {
        return minKCal;
    }

    public void setMinKCal(double minKCal) {
        this.minKCal = minKCal;
    }

    public double getMaxKCal() {
        return maxKCal;
    }

    public void setMaxKCal(double maxKCal) {
        this.maxKCal = maxKCal;
    }

    public double getLimitKCal() {
        return limitKCal;
    }

    public void setLimitKCal(double limitKCal) {
        this.limitKCal = limitKCal;
    }

    Date date;

    double currentKCal;

    double minKCal;
    double maxKCal;
    double limitKCal;

    public static DailyData startNewDay(double minKCal, double maxKCal, double limitKCal) {
        DailyData dailyData = new DailyData();
        dailyData.date = DateTimeUtil.getTodaysDateAt0AM();
        dailyData.currentKCal = 0;

        dailyData.minKCal = minKCal;
        dailyData.maxKCal = maxKCal;
        dailyData.limitKCal = limitKCal;
//        dailyData.minKCal = 1280;
//        dailyData.maxKCal = 1600;
//        dailyData.limitKCal = 2000;

        // Save the data to realm
        Realm realm = FITApplication.getUserDataRealm();
        realm.beginTransaction();
        realm.copyToRealm(dailyData);
        realm.commitTransaction();

        return dailyData;
    }
}
