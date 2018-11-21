package com.kmutt.sit.apps.fit.core.main.dailydata;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kmutt.sit.apps.fit.R;
import com.kmutt.sit.apps.fit.utils.UiUtil;

/**
 * [ AUTO-GENERATED ]
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class DailyDataFragment extends Fragment {

    // Arguments KEYS
    private static final String ARG_PROGRESSBAR_SIZE_PX = "progressbar_size_px";
    private static final String ARG_LONG_DATE = "long_date";
    private static final String ARG_SHOULD_SHOW_NEW_ITEM_ROW = "should_show_new_item_row";

    // Factory Method(s)
    public static DailyDataFragment newInstance(float progressBarSizePx, long date, boolean shouldShowNewItemRow) {
        DailyDataFragment fragment = new DailyDataFragment();
        Bundle args = new Bundle();
        args.putFloat(ARG_PROGRESSBAR_SIZE_PX, progressBarSizePx);
        args.putLong(ARG_LONG_DATE, date);
        args.putBoolean(ARG_SHOULD_SHOW_NEW_ITEM_ROW, shouldShowNewItemRow);
        fragment.setArguments(args);
        return fragment;
    }

    // Arguments
    long date;
    float progressBarSizePx;
    boolean shouldShowNewItemRow;

    // Views
    View caloriesProgressBarFrame;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBarLayout;

    // RecyclerViews
    RecyclerView recyclerView;

    // Button Bar
    View btnNewItemFood, btnNewItemWorkout;


    /**
     *  LEAVE THIS ALONE!
     *  parameters should be handled in onCreate() instead
     */
    public DailyDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.date = getArguments().getLong(ARG_LONG_DATE);
            this.progressBarSizePx = getArguments().getFloat(ARG_PROGRESSBAR_SIZE_PX);
            this.shouldShowNewItemRow = getArguments().getBoolean(ARG_SHOULD_SHOW_NEW_ITEM_ROW);

//            Log.d(this.getClass().getSimpleName(), "new DailyDateFragment()");
//            Log.d(this.getClass().getSimpleName(), "date = " + this.date);
//            Log.d(this.getClass().getSimpleName(), "progressBarSizePx = " + this.progressBarSizePx);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_daily_data, container, false);

        // Init views
        caloriesProgressBarFrame = rootView.findViewById(R.id.caloriesProgressBarFrame);
        collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsingToolbarLayout);
        appBarLayout = (AppBarLayout) rootView.findViewById(R.id.appBarLayout);

        initCollapsingAnimation();
        initProgressBarSize();

        // 'NewItem' Row containing 2 buttons
        if (shouldShowNewItemRow) {
            btnNewItemFood = rootView.findViewById(R.id.btnNewItemFood);
            btnNewItemWorkout = rootView.findViewById(R.id.btnNewItemWorkout);

            btnNewItemFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            btnNewItemWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        else {
            View newItemButtonBar = rootView.findViewById(R.id.newItemButtonBar);
            newItemButtonBar.setVisibility(View.GONE);
        }

        // init RecyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    // CollapsingToolbarLayout custom scrolling effect
    private void initCollapsingAnimation() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                float offset = 1 + ((float) verticalOffset / (float) totalScrollRange);

                caloriesProgressBarFrame.setAlpha(offset);

                float minScale = defaultProgressBarScale * 0.9f;
                float maxScale = defaultProgressBarScale;
                float scrollingScale = minScale + ((maxScale - minScale) * offset);
//                float scrollingScale = defaultProgressBarScale * offset;

                caloriesProgressBarFrame.setScaleX(scrollingScale);
                caloriesProgressBarFrame.setScaleY(scrollingScale);

                float additionalTranslationY = -verticalOffset / 2f;
                caloriesProgressBarFrame.setTranslationY(defaultProgressBarTranslationY + additionalTranslationY);

                Log.d("AppBarLayout", "offset = " + offset + "\t scale = " + scrollingScale);
            }
        });
    }

    // init the Circular ProgressBar's size
    float defaultProgressBarScale;
    float defaultProgressBarTranslationY;
    private void initProgressBarSize() {
        // scale the ProgressBar to the appropriate size
        defaultProgressBarScale = progressBarSizePx / 278f;
        caloriesProgressBarFrame.setScaleX(defaultProgressBarScale);
        caloriesProgressBarFrame.setScaleY(defaultProgressBarScale);

        // translation Y
        defaultProgressBarTranslationY = (defaultProgressBarScale - 1) * 278f;
        caloriesProgressBarFrame.setTranslationY(defaultProgressBarTranslationY);

        // fix the CollapsingToolbarLayout height
        AppBarLayout.LayoutParams newLayoutParams = new AppBarLayout.LayoutParams(collapsingToolbarLayout.getLayoutParams());
        int bottomMargin = getResources().getDimensionPixelOffset(R.dimen.padding_xlarge);
        newLayoutParams.height = (int) UiUtil.dpToPx(getActivity(), progressBarSizePx) + bottomMargin;
//        newLayoutParams.bottomMargin = bottomMargin;
        collapsingToolbarLayout.setLayoutParams(newLayoutParams);
    }
}
