package com.kmutt.sit.apps.fit.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by Freshy on 7/12/2017 AD.
 */

public class UiUtil {
    private static DisplayMetrics displayMetrics;
    private static Activity context;

    private static void initIfNeccessary(Activity context) {
        if (displayMetrics == null || UiUtil.context != context) {
            displayMetrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }
    }

    public static int getDisplayWidthPx(Activity context) {
        initIfNeccessary(context);
        return displayMetrics.widthPixels;
    }

    public static float getDisplayWidthDp(Activity context) {
        initIfNeccessary(context);
        return displayMetrics.widthPixels / displayMetrics.density;
    }

    public static float pxToDp(Activity context, float px) {
        initIfNeccessary(context);
        return px / displayMetrics.density;
    }

    public static float dpToPx(Activity context, float dp) {
        initIfNeccessary(context);
        return dp * displayMetrics.density;
    }
}
