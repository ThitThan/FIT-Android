package com.kmutt.sit.apps.fit.views;

import android.content.Context;
import android.util.AttributeSet;

import com.kmutt.sit.apps.fit.utils.Typefaces;

/**
 * Created by Freshy on 5/30/2017 AD.
 */

public class HighTideBoldTextView extends android.support.v7.widget.AppCompatTextView {
    public HighTideBoldTextView(Context context) {
        super(context);
        initTypeface();
    }

    public HighTideBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeface();
    }

    public HighTideBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeface();
    }

    void initTypeface() {
        if (Typefaces.HighTide_Bold == null)
            Typefaces.init(getContext());

        this.setTypeface(Typefaces.HighTide_Bold);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            ((TextView) tabViewChild).setLetterSpacing(0.05f);
    }
}
