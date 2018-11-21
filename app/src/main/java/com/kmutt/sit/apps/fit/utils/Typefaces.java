package com.kmutt.sit.apps.fit.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Freshy on 5/29/2017 AD.
 */

public class Typefaces {
    public static Typeface Cloud_Bold;
    public static Typeface HighTide_Bold;
    public static Typeface HighTide_Sans;

    public static void init(Context context) {
        if (Cloud_Bold == null)
            Cloud_Bold = Typeface.createFromAsset(context.getAssets(), "typefaces/cloud_bold.otf");
        if (HighTide_Bold == null)
            HighTide_Bold = Typeface.createFromAsset(context.getAssets(), "typefaces/hightide_bold.otf");
        if (HighTide_Sans == null)
            HighTide_Sans = Typeface.createFromAsset(context.getAssets(), "typefaces/hightide_sans.otf");
    }
}
