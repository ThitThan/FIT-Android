package com.kmutt.sit.apps.fit.core.main;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kmutt.sit.apps.fit.R;
import com.kmutt.sit.apps.fit.core.main.dailydata.DailyDataFragmentV2;
import com.kmutt.sit.apps.fit.core.main.dailydata.DailyDataPagerAdapter;
import com.kmutt.sit.apps.fit.utils.UiUtil;

import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollUpdateListener;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import ru.noties.scrollable.CanScrollVerticallyDelegate;
import ru.noties.scrollable.OnScrollChangedListener;
import ru.noties.scrollable.OverScrollListenerBase;
import ru.noties.scrollable.ScrollableLayout;

/**
 * Created by Freshy on 7/17/2017 AD.
 */

public class UiEffectHelper {

    public static void initVerticalScrollEffect(final ViewPager viewPager, final DailyDataPagerAdapter pagerAdapter, final MainHeaderViewController controller) {
        /**
         *  Allow Vertical Scrolling
         */
        controller.scrollableLayout.setCanScrollVerticallyDelegate(new CanScrollVerticallyDelegate() {
            @Override
            public boolean canScrollVertically(int direction) {
                int currentPosition = viewPager.getCurrentItem();
                DailyDataFragmentV2 fragment = pagerAdapter.getActiveFragmentAt(currentPosition);
                if (fragment != null)
                    return fragment.getRecyclerView().canScrollVertically(direction);
                else
                    return false;
            }
        });
//        scrollableLayout.setOnFlingOverListener(new OnFlingOverListener() {
//            @Override
//            public void onFlingOver(int y, long duration) {
//                int currentPosition = viewPager.getCurrentItem();
//                mPagerAdapter.getActiveFragmentAt(currentPosition).getRecyclerView().smoothScrollBy(0, y);
//            }
//        });
        controller.scrollableLayout.addOnScrollChangedListener(new OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int y, int oldY, int maxY) {
                float offset = 1 - ((float) y / maxY);

                float minScale = 0.9f;
                float maxScale = 1f;
                float scrollingScale = minScale + ((maxScale - minScale) * offset);

                controller.headerFrame.setAlpha(offset);
                controller.headerFrame.setScaleX(scrollingScale);
                controller.headerFrame.setScaleY(scrollingScale);
                controller.headerFrame.setTranslationY(y);

//                Log.d("AppBarLayout", "offset = " + offset + "\t scale = " + scrollingScale);
            }
        });
        controller.scrollableLayout.setOverScrollListener(new OverScrollListenerBase() {
            @Override
            protected void onRatioChanged(ScrollableLayout layout, float ratio) {
                final float headerHeight = controller.headerFrame.getHeight();

                float minScale = 1f;
                float maxScale = 1.05f;

                float headerScaleRatio = minScale + ((maxScale - minScale) * ratio);
                controller.headerFrame.setScaleX(headerScaleRatio);
                controller.headerFrame.setScaleY(headerScaleRatio);

                float minAlpha = 0.75f;
                float maxAlpha = 1f;
                float headerAlpha = maxAlpha - ((maxAlpha - minAlpha) * ratio);
                controller.headerFrame.setAlpha(headerAlpha);

                float translationRatio = 1.F + (.33F * ratio);
                float translationY = ((headerHeight * translationRatio) - headerHeight) / 2.F;
                viewPager.setTranslationY(translationY);
//                mHeaderFrame.setTranslationY(translationY / 2f);
            }
        });
    }

    public static void initViewPagerScrollEffect(final Activity context, ViewPager viewPager, final MainHeaderViewController controller, final TextView tvTitle) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /**
                 *   Header effectOffest
                 */
                if (controller.scrollableLayout.getScrollY() < controller.scrollableLayout.getMaxScrollY()) {

                    float effectOffset;
                    if (positionOffset < 0.5)
                        effectOffset = (0.5f - positionOffset) * 2f;
                    else
                        effectOffset = (positionOffset - 0.5f) * 2f;

                    controller.headerFrame.setAlpha(effectOffset);
                    tvTitle.setAlpha(effectOffset);

                    float minScale = 1f;
                    float maxScale = 1.15f;
                    float scale = minScale + ((maxScale - minScale) * (1 - effectOffset));
                    controller.headerFrame.setScaleX(scale);
                    controller.headerFrame.setScaleY(scale);
                }
            }

            @Override
            public void onPageSelected(int position) { }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                /**
                 *   Page alpha
                 */
                float minAlpha = 0.3f;
                float maxAlpha = 1f;

                if (position < -1) {
                    page.setAlpha(minAlpha);
                }
                else if (position < 0) {
                    float alpha = maxAlpha + ((maxAlpha - minAlpha) * position);
                    page.setAlpha(alpha);
                }
                else if (position < 1) {
                    float alpha = maxAlpha - ((maxAlpha - minAlpha) * position);
                    page.setAlpha(alpha);
                }
                else {
                    page.setAlpha(minAlpha);
                }
            }
        });

        // ViewPager Elastic overscroll effect
        OverScrollDecoratorHelper.setUpOverScroll(viewPager)
                .setOverScrollUpdateListener(new IOverScrollUpdateListener() {
                    float screenWidth = -1;

                    @Override
                    public void onOverScrollUpdate(IOverScrollDecor decor, int state, float offset) {
                        if (screenWidth == -1)
                            screenWidth = UiUtil.getDisplayWidthDp(context);
                        float effectRatio = Math.abs(offset) / screenWidth;

                        if (controller.scrollableLayout.getScrollY() < controller.scrollableLayout.getMaxScrollY())
                            controller.headerFrame.setAlpha(1 - (effectRatio * 2));
                        Log.d("VP OverScrollDecorator", "effectRatio = " + effectRatio);
                    }
                });
    }

    // init the Circular ProgressBar's size
    public static void initProgressBarSize(Activity context, View headerFrame, View caloriesProgressBarFrame) {
        // progressBarSize == 3/4 screenWidth
        float progressBarSizePx = UiUtil.getDisplayWidthPx(context) / 4 * 3;

        float defaultProgressBarSizePx = context.getResources().getDimensionPixelSize(R.dimen.calories_progressbar_size_padded);

        // ProgressBar Scale
        float scale = progressBarSizePx / defaultProgressBarSizePx;

        caloriesProgressBarFrame.setScaleX(scale);
        caloriesProgressBarFrame.setScaleY(scale);
        Log.d("initProgressBarSize", "setting the progressbar size & pos");
        Log.d("initProgressBarSize", "scale = " + scale);

        // ProgressBar TranslationY
        float scaledSizeDiff = (scale - 1) * defaultProgressBarSizePx;
        float translationY = scaledSizeDiff / 2;
        caloriesProgressBarFrame.setTranslationY(translationY);
        Log.d("initProgressBarSize", "translationY = " + translationY);


        // fix the CollapsingToolbarLayout height
        FrameLayout.LayoutParams newLayoutParams = new FrameLayout.LayoutParams(headerFrame.getLayoutParams());
        float topPadding = context.getResources().getDimensionPixelOffset(R.dimen.titleBarSize);
        float bottomMargin = context.getResources().getDimensionPixelOffset(R.dimen.padding_xlarge);
        if (scale > 1) {
            topPadding  *= scale;
            bottomMargin *= scale;
            newLayoutParams.height = (int) (topPadding + progressBarSizePx + bottomMargin);
            headerFrame.setPadding(0, (int) topPadding, 0, 0);
        }
        else {
            newLayoutParams.height = (int) (topPadding + progressBarSizePx + bottomMargin);
            headerFrame.setPadding(0, (int) (topPadding * scale), 0, 0);

            /**
             *   Workaround for screen size smaller than Galaxy Nexus (360*640dp)
             */
            caloriesProgressBarFrame.setTranslationY(translationY + ((1 - scale) * topPadding));
        }
        headerFrame.setLayoutParams(newLayoutParams);
    }
}
