package com.kmutt.sit.apps.fit.core.main;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.kmutt.sit.apps.fit.R;
import com.kmutt.sit.apps.fit.core.main.dailydata.DailyDataFragment;
import com.kmutt.sit.apps.fit.utils.UiUtil;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class MainActivityV2 extends AppCompatActivity {

    View rootView;

    View btnUser;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v2);

        // init views
        rootView = findViewById(R.id.rootView);
        btnUser = findViewById(R.id.btnUser);

        // button
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivityV2.this, "Coming soon..", Toast.LENGTH_SHORT).show();
            }
        });

        // measure the size for the Circular ProgressBar
        final float threeForthScreenWidth = UiUtil.getDisplayWidthDp(this) / 4 * 3;

//        Toast.makeText(this, "3/4 screenWidth : " + threeForthScreenWidth, Toast.LENGTH_SHORT).show();

        // ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return DailyDataFragment.newInstance(threeForthScreenWidth, System.currentTimeMillis(), position == 0);
            }

            @Override
            public int getCount() {
                return 4;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                int colors[] = new int[] { 0xff9E9E9E, 0xff4CAF50, 0xffFF9100, 0xffFF5722 };
                int colors[] = new int[] { 0xff9E9E9E, 0xff4CAF50, 0xff4CAF50, 0xffFF9100};
                fadeBackgroundToColor(colors[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
//                Log.d("PageTransformer", "position : " + position);

                float minAlpha = 0.3f;
                float maxAlpha = 1f;

//                float minScale = 0.9f;
//                float maxScale = 1f;

                if (position < -1) {
                    page.setAlpha(minAlpha);
                }
                else if (position < 0) {
                    float alpha = maxAlpha + ((maxAlpha - minAlpha) * position);
                    page.setAlpha(alpha);

//                    float scale = maxScale + ((maxScale - minScale) * position);
//                    page.setScaleX(scale);
//                    page.setScaleY(scale);
                }
                else if (position < 1) {
                    float alpha = maxAlpha - ((maxAlpha - minAlpha) * position);
                    page.setAlpha(alpha);

//                    float scale = maxScale - ((maxScale - minScale) * position);
//                    page.setScaleX(scale);
//                    page.setScaleY(scale);
                }
                else {
                    page.setAlpha(minAlpha);
                }
            }
        });
        // ViewPager Elastic overscroll effect
        OverScrollDecoratorHelper.setUpOverScroll(viewPager);

        // show the left and right ViewPager item
        int paddingLargeDp = getResources().getDimensionPixelOffset(R.dimen.padding_large);
        int paddingMediumDp = getResources().getDimensionPixelOffset(R.dimen.padding_medium);

        // Disable clip to padding
        viewPager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        viewPager.setPadding(paddingLargeDp, 0, paddingLargeDp, 0);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        viewPager.setPageMargin(paddingMediumDp);
    }

    public void fadeBackgroundToColor(int newColor) {
        ObjectAnimator colorAnimator = ObjectAnimator.ofObject(rootView, "backgroundColor", new ArgbEvaluator(), ((ColorDrawable) rootView.getBackground()).getColor(), newColor);
        colorAnimator.setDuration(750);
        colorAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        colorAnimator.start();


    }
}
