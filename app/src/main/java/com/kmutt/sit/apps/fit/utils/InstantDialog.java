package com.kmutt.sit.apps.fit.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by Freshy on 9/9/16 AD.
 */
public class InstantDialog {
    public static ProgressDialog makeProgressDialog(Context context, String message, boolean canceledOnTouchOutside) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);

        return mProgressDialog;
    }

    public static AlertDialog makeAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null)
            builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        return builder.create();
    }
}
