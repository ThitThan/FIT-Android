package com.kmutt.sit.apps.fit.core.main.dailydata.calories.realm;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.Required;

/**
 * Created by Freshy on 7/26/2017 AD.
 */

public class CaloriesEntry extends RealmObject {

    public static final String TYPE_FOOD = "food";
    public static final String TYPE_EXERCISE = "exercise";

    @Index
    long id;

    Date time;

    @Required
    String title;

    float kcals;

    @Required
    String type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getKcals() {
        return kcals;
    }

    public void setKcals(float kcals) {
        this.kcals = kcals;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
