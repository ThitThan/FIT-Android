package com.kmutt.sit.apps.fit.views;

import android.content.Context;
import android.util.AttributeSet;

import com.kmutt.sit.apps.fit.utils.Typefaces;

/**
 * Created by Freshy on 5/30/2017 AD.
 */

public class CloudBoldEditText extends android.support.v7.widget.AppCompatEditText {
    public CloudBoldEditText(Context context) {
        super(context);
        initTypeface();
    }

    public CloudBoldEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeface();
    }

    public CloudBoldEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeface();
    }

    void initTypeface() {
        if (Typefaces.Cloud_Bold == null)
            Typefaces.init(getContext());

        this.setTypeface(Typefaces.Cloud_Bold);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            ((TextView) tabViewChild).setLetterSpacing(0.05f);
    }
}
