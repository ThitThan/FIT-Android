package com.kmutt.sit.apps.fit.core.main.users;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.kmutt.sit.apps.fit.FITApplication;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.Required;

/**
 * Created by Freshy on 7/27/2017 AD.
 */

public class User extends RealmObject {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBiologicalGender() {
        return biologicalGender;
    }

    public void setBiologicalGender(String biologicalGender) {
        this.biologicalGender = biologicalGender;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Index
    String id;

    @Required
    String nickName;
    String biologicalGender;

    double weight;
    double height;

    /**
     *   Biological Genders
     */
    public static final String BIO_GENDER_MALE = "male";
    public static final String BIO_GENDER_FEMALE = "female";
//    public static final String GENDER_OTHER = "other";

    /**
     *   Current User & userId
     */
    private static final String USER_SHARED_PREF_NAME = "user_pref";
    private static final String KEY_USER_ID = "user_id";
    public static String getCurrentUserId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(USER_SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(KEY_USER_ID, null);
    }
    private static void setUserId(Context context, String userId) {
        SharedPreferences.Editor prefEditor = context.getSharedPreferences(USER_SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
        prefEditor.putString(KEY_USER_ID, userId);
        prefEditor.apply();
    }
    public static User getCurrentUser(Context context) {
        String userId = getCurrentUserId(context);

        Log.d("User", "isCurrentUserExist : " + (userId == null));

        if (userId == null)
            return null;

        return FITApplication.getUserDataRealm().where(User.class).equalTo(KEY_USER_ID, userId).findFirst();
    }

    public static void createUserIfHavent(final Context context, final String nickName, final String biologicalGender, final double weight, final double height, final CreateUserCallback callback) {
        // if user already exist --> return
        if (getCurrentUserId(context) != null) {
            if (callback != null) {
                Log.d("User", "user already exist!!");
                callback.onFinish(null, false);
            }
            return;
        }

        FITApplication.getUserDataRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User newUser = realm.createObject(User.class);
                newUser.id = UUID.randomUUID().toString();
                newUser.nickName = nickName;
                newUser.biologicalGender = biologicalGender;
                newUser.weight = weight;
                newUser.height = height;

                setUserId(context, newUser.id);

                if (callback != null)
                    callback.onFinish(newUser, true);

//                realm.beginTransaction();
                realm.copyToRealm(newUser);
//                realm.commitTransaction();

                Log.d("User", "new user is created!! (id = " + newUser.id + ")");
            }
        });
    }

    public interface CreateUserCallback {
        void onFinish(User newUser, boolean isNewUserCreated);
    }
}
