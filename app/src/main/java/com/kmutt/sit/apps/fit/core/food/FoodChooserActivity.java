package com.kmutt.sit.apps.fit.core.food;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.kmutt.sit.apps.fit.FITApplication;
import com.kmutt.sit.apps.fit.R;
import com.kmutt.sit.apps.fit.core.food.realm.Food;
import com.kmutt.sit.apps.fit.utils.Typefaces;
import com.kmutt.sit.apps.fit.utils.UiUtil;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class FoodChooserActivity extends AppCompatActivity {

    public static final String RESULT_ARG_FOOD_NAME = "food_name";
    public static final String RESULT_ARG_FOOD_KCAL = "food_kcal";

    Toolbar appBar;

    // RecyclerView
    RecyclerView recyclerView;
    FoodChooserAdapter mAdapter;

    // RecyclerView's FastScroller
    FastScroller fastScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_chooser);

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        /**
         *   RecyclerView setup
         */

        mAdapter = new FoodChooserAdapter(this, new ArrayList<Food>());
        mAdapter.setOnItemClickListener(new FoodChooserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FoodChooserAdapter.ViewHolder viewHolder) {
                Intent resultIntent = getIntent();
                resultIntent.putExtra(RESULT_ARG_FOOD_NAME, viewHolder.foodData.getFoodName());
                resultIntent.putExtra(RESULT_ARG_FOOD_KCAL, viewHolder.foodData.getKcal());
                setResult(RESULT_OK, resultIntent);

                finish();
            }
        });
        recyclerView.setAdapter(mAdapter);

        fastScroller = (FastScroller) findViewById(R.id.fastScroller);
        fastScroller.setRecyclerView(recyclerView);

        // FastScroller's Typeface
        int childCount = fastScroller.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = fastScroller.getChildAt(i);
            if (child instanceof TextView) {
                if (Typefaces.Cloud_Bold == null)
                    Typefaces.init(this);
                ((TextView) child).setTypeface(Typefaces.Cloud_Bold);
            }
        }

        /**
         *  Query all food and pass the Result List to the Adapter
         */

        FITApplication.getPreloadedDataRealm().where(Food.class).findAllAsync().addChangeListener(new RealmChangeListener<RealmResults<Food>>() {
            @Override
            public void onChange(RealmResults<Food> foods) {
                Log.d("RealmResult", "onChange() --> food count = " + foods.size());
                mAdapter.setFoodList(foods);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, calculateGridSpanCount()));
//        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        initAppBar();
    }

    void initAppBar() {
        appBar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    int calculateGridSpanCount() {
        float displayWidthDp = UiUtil.getDisplayWidthDp(this);
        float itemWidthDp = 164f;
        int result = (int) Math.floor(displayWidthDp / itemWidthDp);
        return (result >= 2) ? result:2;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
