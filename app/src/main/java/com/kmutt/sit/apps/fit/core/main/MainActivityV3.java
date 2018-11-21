package com.kmutt.sit.apps.fit.core.main;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kmutt.sit.apps.fit.FITApplication;
import com.kmutt.sit.apps.fit.R;
import com.kmutt.sit.apps.fit.core.food.realm.Food;
import com.kmutt.sit.apps.fit.core.main.dailydata.DailyDataFragmentV2;
import com.kmutt.sit.apps.fit.core.main.dailydata.DailyDataPagerAdapter;
import com.kmutt.sit.apps.fit.core.main.dailydata.realm.DailyData;
import com.kmutt.sit.apps.fit.core.main.firstrun.FirstRunActivityV2;
import com.kmutt.sit.apps.fit.utils.DateTimeUtil;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivityV3 extends AppCompatActivity {

    View rootView;
    View btnUser;

    // Title
    TextView tvTitle;

    // ViewPager
    ViewPager viewPager;
    DailyDataPagerAdapter mPagerAdapter;

    // Header views
    MainHeaderViewController mHeaderController;

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

//    public int getNavigationBarHeight() {
//        int result = 0;
//        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v3);

        /**
         *  First Run
         */
        if (!Food.isFoodDataPreloaded()) {
            Intent i = new Intent(this, FirstRunActivityV2.class);
            startActivity(i);
        }

        /**
         *   Bind all views
         */
        rootView = findViewById(R.id.rootView);

        // Title
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        btnUser = findViewById(R.id.btnUser);

        // Header
        mHeaderController = new MainHeaderViewController(this, R.id.scrollableLayout, R.id.headerFrame, R.id.caloriesProgressBarFrame, R.id.tvCaloriesCount, R.id.caloriesProgressBar, R.id.progressBarBgFrame);
        mHeaderController.setOnStateChangedListener(new MainHeaderViewController.OnStateChangedListener() {
            @Override
            public void onStateChanged(int oldState, int newState) {
                int colors[] = new int[]{ 0xff9E9E9E, 0xff4CAF50, 0xffFF9100, 0xffFF5722 };

                fadeBackgroundToColor(colors[newState]);
                Log.d("ViewPager", "calorie state updated ( " + newState + " )");
            }
        });
        mHeaderController.setParams(640, 896, 1024, 30);

        // ViewPager
        mPagerAdapter = new DailyDataPagerAdapter(getSupportFragmentManager(), new DailyDataFragmentV2.OnKCalsChangedListener() {
            @Override
            public void onKCalsChanged(double newKCals) {
                mHeaderController.setCurrentCalories(newKCals);
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(mPagerAdapter);

        // Scrolling Effect
        UiEffectHelper.initVerticalScrollEffect(viewPager, mPagerAdapter, mHeaderController);
        UiEffectHelper.initViewPagerScrollEffect(this, viewPager, mHeaderController, tvTitle);
        UiEffectHelper.initProgressBarSize(this, mHeaderController.headerFrame, mHeaderController.caloriesProgressBarFrame);


        /**
         *   Fix the rootView's top padding on API >= LOLLIPOP
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            rootView.setPadding(0, getStatusBarHeight(), 0, 0);


        /**
         *   titleBar setup
         */
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivityV3.this, "Coming soon..", Toast.LENGTH_SHORT).show();
            }
        });


        /**
         *    ViewPager setup
          */
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = -1;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /**
                 *   check if the Header data should be updated
                 */
                int newPosition = position;
                if (positionOffset >= 0.5)
                    newPosition++;

                if (currentPosition != newPosition) {
                    currentPosition = newPosition;

                    /**
                     *   UPDATE HEADER DATA HERE
                     */
//                    mHeaderController.setCurrentCalories((newPosition + 1) * 164);

                    // CircularProgress
                    DailyData dailyData = mPagerAdapter.getDailyDataAt(newPosition);
                    mHeaderController.setParams(dailyData.getMinKCal(),
                            dailyData.getMaxKCal(),
                            dailyData.getLimitKCal(),
                            dailyData.getCurrentKCal());

                    // Title
                    String title;
                    Date today = DateTimeUtil.getTodaysDateAt0AM();
                    int dateCompared = today.compareTo(dailyData.getDate());
                    if (dateCompared == 0)
                        title = getString(R.string.text_today);
//                    else if (...)
//                        title = getString(R.string.text_yesterday);
                    else {
                        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
                        title = dateFormat.format(dailyData.getDate());
                    }
                    tvTitle.setText(title);
                    dailyData.getDate();
                }
            }

            @Override
            public void onPageSelected(int position) {
                // Setup Button Bar as a draggable view
                int currentPage = viewPager.getCurrentItem();
                DailyDataFragmentV2 fragment = mPagerAdapter.getActiveFragmentAt(currentPage);
                if (fragment != null)
                    mHeaderController.scrollableLayout.setDraggableView(fragment.getNewItemButtonBar());
            }

            @Override
            public void onPageScrollStateChanged(int newState) {

            }
        });

        /**
         *   show the left and right ViewPager item
         */
        final int titleBarSize = getResources().getDimensionPixelOffset(R.dimen.titleBarSize);
        final int paddingLargeDp = getResources().getDimensionPixelOffset(R.dimen.padding_large);
        final int paddingMediumDp = getResources().getDimensionPixelOffset(R.dimen.padding_medium);

        viewPager.setClipToPadding(false);
        viewPager.setPadding(paddingLargeDp, 0, paddingLargeDp, titleBarSize - getStatusBarHeight());
        viewPager.setPageMargin(paddingMediumDp);

        /**
         *   Header setup
         */
        mHeaderController.headerFrame.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {

                // ScrollableLayout manual MaxScroll
                int newMaxScrollY = mHeaderController.headerFrame.getHeight() - titleBarSize;
                mHeaderController.scrollableLayout.setMaxScrollY(newMaxScrollY);
//                Toast.makeText(MainActivityV3.this, "maxScrollY : " + newMaxScrollY, Toast.LENGTH_SHORT).show();

                // Marker position
                mHeaderController.updateMarkersToParams();
            }
        });
    }

    String appName;
    Bitmap appIcon;
    public void fadeBackgroundToColor(int newColor) {
        ObjectAnimator colorAnimator = ObjectAnimator.ofObject(rootView, "backgroundColor", new ArgbEvaluator(), ((ColorDrawable) rootView.getBackground()).getColor(), newColor);
        colorAnimator.setDuration(1200);
//        colorAnimator.setInterpolator(new DecelerateInterpolator());
        colorAnimator.start();

        // Overview Screen app title color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (appName == null)
                appName = getString(R.string.app_name);
            if (appIcon == null)
                appIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

            this.setTaskDescription(new ActivityManager.TaskDescription(appName, appIcon, newColor));

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        /**
         *  Load DailyDatas
         */
        Realm userDataRealm = FITApplication.getUserDataRealm();
        userDataRealm.where(DailyData.class).findAllSortedAsync("date", Sort.ASCENDING).addChangeListener(new RealmChangeListener<RealmResults<DailyData>>() {
            @Override
            public void onChange(RealmResults<DailyData> dailyDatas) {
                ArrayList<DailyData> dailyDataArray = new ArrayList<>(dailyDatas);

                // TODO: Use actual formulas instead
                /**
                 *   if 'first use'
                 */
                if (dailyDatas.size() == 0) {
                    startNewDay();
                    Log.d(getClass().getSimpleName(), "newDay !!");
                }
                else {
//                    Date newestDate = dailyDataArray.get(0).getDate();
                    Date newestDate = dailyDataArray.get(dailyDataArray.size() - 1).getDate();
                    Date today = DateTimeUtil.getTodaysDateAt0AM();
                    Log.d(getClass().getSimpleName(), "today.compareTo(newestDate) = " + today.compareTo(newestDate));
                    if (today.compareTo(newestDate) > 0) {
                        /**
                         *   if new day
                         */
                        startNewDay();
//                        dailyDataArray.add(newDayData);
                    }
                }
                mPagerAdapter.setDailyDataList(dailyDataArray);
                viewPager.setCurrentItem(mPagerAdapter.getCount() - 1, false);
            }
        });
    }

    private DailyData startNewDay() {
//        double weightKg =
//        double bmr = (10 * weightKg) + (6.25 * heightCm) - (5 * age) + 5;

        DailyData newDay = DailyData.startNewDay(1280, 1600, 2000);

        return newDay;
    }
}
