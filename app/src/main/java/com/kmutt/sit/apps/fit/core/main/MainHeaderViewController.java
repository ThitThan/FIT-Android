package com.kmutt.sit.apps.fit.core.main;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kmutt.sit.apps.fit.R;
import com.kmutt.sit.apps.fit.utils.UiUtil;

import ru.noties.scrollable.ScrollableLayout;

/**
 * Created by Freshy on 7/17/2017 AD.
 */

public class MainHeaderViewController {

    private int progressBarBgRes = R.drawable.progressbar_progress_bg;
    private int progressBarBgDangerRes = R.drawable.progressbar_progress_bg_danger;

    private int minMarkerViewLayoutId = R.layout.layout_progressbar_min_marker_view;
    private int maxMarkerViewLayoutId = R.layout.layout_progressbar_max_marker_view;

    // STATES
    public static int STATE_NORMAL = 0;
    public static int STATE_MIN_REACHED = 1;
    public static int STATE_MAX_REACHED = 2;
    public static int STATE_DANGER = 3;

    Activity mContext;

    // Views
    View headerFrame;
    FrameLayout caloriesProgressBarFrame;
    TextView tvCaloriesCount;
    ProgressBar caloriesProgressBar;
    View progressBarBgFrame;

    // Marker Views
    View minMarker;
    View maxMarker;

    // Scrollable
    ScrollableLayout scrollableLayout;

    // Params
    private double minRequiredCalories;
    private double maxRecommendedCalories;
    private double realMaxCalories;

    private double currentCalories;
    private int state = -1;

    // Event Listeners
    private OnStateChangedListener mStateChangedListener;
    public void setOnStateChangedListener(OnStateChangedListener listener) {
        this.mStateChangedListener = listener;
    }

    public MainHeaderViewController(Activity context, int scrollableLayoutId, int headerFrameId, int caloriesProgressBarFrameId, int tvCaloriesCountId, int caloriesProgressBarId, int progressBarBgFrameRes) {
        mContext = context;

        // Assign Views
        headerFrame = mContext.findViewById(headerFrameId);
        caloriesProgressBarFrame = (FrameLayout) mContext.findViewById(caloriesProgressBarFrameId);
        tvCaloriesCount = (TextView) mContext.findViewById(tvCaloriesCountId);
        caloriesProgressBar = (ProgressBar) mContext.findViewById(caloriesProgressBarId);
        progressBarBgFrame = mContext.findViewById(progressBarBgFrameRes);

        scrollableLayout = (ScrollableLayout) mContext.findViewById(scrollableLayoutId);
    }

    public void setParams(double minRequiredCalories, double maxRecommendedCalories, double realMaxCalories, double currentCalories) {
        this.minRequiredCalories = minRequiredCalories;
        this.maxRecommendedCalories = maxRecommendedCalories;
        this.realMaxCalories = realMaxCalories;

        setCurrentCalories(currentCalories);
//        updateViewToParams();
        updateMarkersToParams();
    }

    public void setCurrentCalories(double currentCalories) {
        // calories --> state
        int newState;
        if (currentCalories >= realMaxCalories)
            newState = STATE_DANGER;
        else if (currentCalories >= maxRecommendedCalories)
            newState = STATE_MAX_REACHED;
        else if (currentCalories >= minRequiredCalories)
            newState = STATE_MIN_REACHED;
        else
            newState = STATE_NORMAL;

        // call the event listener if neccessary
        if (this.state != newState && mStateChangedListener != null) {
            mStateChangedListener.onStateChanged(this.state, newState);
        }

        this.state = newState;
        this.currentCalories = currentCalories;

        updateViewToParams();
    }

    public int getState() {
        return this.state;
    }

    private void updateViewToParams() {
        caloriesProgressBar.setMax((int) realMaxCalories);

        /**
         *   if state is DANGER --> show a different style of ProgressBar
         */
        int size;
        if (this.state == STATE_DANGER) {
            progressBarBgFrame.setBackgroundResource(progressBarBgDangerRes);
            size = mContext.getResources().getDimensionPixelSize(R.dimen.calories_progressbar_size_danger);
            caloriesProgressBar.setProgress((int) (currentCalories - realMaxCalories));
        }
        else {
            progressBarBgFrame.setBackgroundResource(progressBarBgRes);
            size = mContext.getResources().getDimensionPixelSize(R.dimen.calories_progressbar_size);
            caloriesProgressBar.setProgress((int) currentCalories);
        }
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) progressBarBgFrame.getLayoutParams();
        layoutParams.height = size;
        layoutParams.width = size;

        tvCaloriesCount.setText((int) currentCalories + "");
    }

    /**
     *   Min & Max Marker
     */
    void updateMarkersToParams() {
        // Create markers if they don't exist
        if (minMarker == null)
            minMarker = addMarkerView(minMarkerViewLayoutId);
        if (maxMarker == null)
            maxMarker = addMarkerView(maxMarkerViewLayoutId);

        // Calculate the angles
        float minAngle = calculateAngle((float) minRequiredCalories, (float) realMaxCalories);
        float maxAngle = calculateAngle((float) maxRecommendedCalories, (float) realMaxCalories);

        // Marker Rotation
        float minMarkerAngle = toMarkerAngle(minAngle);
        float maxMarkerAngle = toMarkerAngle(maxAngle);
        minMarker.setRotation(minMarkerAngle);
        maxMarker.setRotation(maxMarkerAngle);

        int padding = mContext.getResources().getDimensionPixelOffset(R.dimen.padding_default);

        // Marker Translate X & Y
        float radius = (caloriesProgressBar.getMeasuredWidth() / 2f) - padding;
        float minMarkerRadius = radius + (minMarker.getMeasuredWidth() / 2f);
        float maxMarkerRadius = radius + (maxMarker.getMeasuredWidth() / 2f);
        setMarkerTranslation(minMarker, minMarkerRadius, minAngle);
        setMarkerTranslation(maxMarker, maxMarkerRadius, maxAngle);
    }

    private View addMarkerView(int layoutResId) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // LayoutParams
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        // Inflate marker view
        View marker = inflater.inflate(layoutResId, null);
        marker.setLayoutParams(layoutParams);

        // Add marker view; then return
        caloriesProgressBarFrame.addView(marker, 0);
        return marker;
    }

    private float calculateAngle(float value, float maxValue) {
        return value / maxValue * 360f;
    }

    private float toMarkerAngle(float angle) {
        if (angle < 180f)    // marker's on the right side
            return angle - 90;
        else                // marker's on the left side
            return angle + 90;
    }

    private float toMathAngle(float computerAngle) {
        return computerAngle - 90;
    }

    private float calculateTranslationY(float radius, float angle) {
        float mathAngle = toMathAngle(angle);
        double radianAngle = Math.toRadians(mathAngle);
        double translationY = Math.sin(radianAngle) * radius;
        Log.d("calculateTranslationX", "angle = " + radianAngle + "\t y = " + Math.sin(radianAngle) + " * " + radius + " = " + (int)translationY);
        return (float) translationY;
    }
    private float calculateTranslationX(float radius, float angle) {
        float mathAngle = toMathAngle(angle);
        double radianAngle = Math.toRadians(mathAngle);
        double translationX = Math.cos(radianAngle) * radius;
        Log.d("calculateTranslationX", "angle = " + radianAngle + "\t x = " + Math.cos(radianAngle) + " * " + radius + " = " + (int)translationX);
        return (float) translationX;
    }

    private void setMarkerTranslation(View marker, float radius, float angle) {
        Log.d("setMarkerTranslation", "setting a marker translation");
        float translationX = calculateTranslationX(radius, angle);
        float translationY = calculateTranslationY(radius, angle);
        marker.setTranslationX(translationX);
        marker.setTranslationY(translationY);
    }

    public interface OnStateChangedListener {
        void onStateChanged(int oldState, int newState);
    }
}
