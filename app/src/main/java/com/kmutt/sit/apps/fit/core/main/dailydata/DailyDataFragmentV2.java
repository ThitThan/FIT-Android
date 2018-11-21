package com.kmutt.sit.apps.fit.core.main.dailydata;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kmutt.sit.apps.fit.FITApplication;
import com.kmutt.sit.apps.fit.R;
import com.kmutt.sit.apps.fit.core.food.FoodChooserActivity;
import com.kmutt.sit.apps.fit.core.food.realm.Food;
import com.kmutt.sit.apps.fit.core.main.dailydata.calories.realm.CaloriesEntry;
import com.kmutt.sit.apps.fit.core.main.dailydata.realm.DailyData;
import com.kmutt.sit.apps.fit.core.main.users.User;
import com.kmutt.sit.apps.fit.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * [ AUTO-GENERATED ]
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyDataFragmentV2#newInstance} factory method to
 * create an instance of this fragment.
 */

public class DailyDataFragmentV2 extends Fragment {

    // Intent REQUEST_CODES
    private static final int REQUEST_CODE_NEW_ITEM_FOOD = 616;

    // Arguments KEYS
    private static final String ARG_LONG_DATE = "long_date";
    private static final String ARG_SHOULD_SHOW_NEW_ITEM_ROW = "should_show_new_item_row";

    // Factory Method(s)
    public static DailyDataFragmentV2 newInstance(Date date, boolean shouldShowNewItemRow) {
        DailyDataFragmentV2 fragment = new DailyDataFragmentV2();
        Bundle args = new Bundle();
        args.putLong(ARG_LONG_DATE, date.getTime());
        args.putBoolean(ARG_SHOULD_SHOW_NEW_ITEM_ROW, shouldShowNewItemRow);
        fragment.setArguments(args);
        return fragment;
    }

    // Arguments
    Date date;
    boolean shouldShowNewItemRow;

//    AppBarLayout appBarLayout;

    // RecyclerViews
    RecyclerView recyclerView;
    GFitCaloriesEntryAdapter mCaloriesEntriesAdapter;

    // Button Bar
    View newItemButtonBar;
    View btnNewItemFood, btnNewItemWorkout;

    public void setOnKCalsChangedListener(OnKCalsChangedListener mKCalsChangedListener) {
        this.mKCalsChangedListener = mKCalsChangedListener;
    }

    // KCals
    OnKCalsChangedListener mKCalsChangedListener;

    /**
     *  LEAVE THIS ALONE!
     *  parameters should be handled in onCreate() instead
     */
    public DailyDataFragmentV2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.date = new Date(getArguments().getLong(ARG_LONG_DATE));
            this.shouldShowNewItemRow = getArguments().getBoolean(ARG_SHOULD_SHOW_NEW_ITEM_ROW);

//            Log.d(this.getClass().getSimpleName(), "new DailyDateFragment()");
//            Log.d(this.getClass().getSimpleName(), "date = " + this.date);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_daily_data_v2, container, false);

        // 'NewItem' Row containing 2 buttons
        if (shouldShowNewItemRow) {
            btnNewItemFood = rootView.findViewById(R.id.btnNewItemFood);
            btnNewItemWorkout = rootView.findViewById(R.id.btnNewItemWorkout);

            btnNewItemFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /**
                     *  Intent to FoodChooserActivity
                     */
                    Intent intent = new Intent(getActivity(), FoodChooserActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_NEW_ITEM_FOOD);
                }
            });
            btnNewItemWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        else {
            newItemButtonBar = rootView.findViewById(R.id.newItemButtonBar);
            newItemButtonBar.setVisibility(View.GONE);
        }

        // init RecyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshAsync();
//        recyclerView.setAdapter(new RecyclerView.Adapter() {
//            LayoutInflater mInflater;
//
//            @Override
//            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                if (mInflater == null)
//                    mInflater = LayoutInflater.from(getActivity());
//                View row = mInflater.inflate(R.layout.listrow_item_food, null, false);
//                RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(row) {
//
//                };
//                return holder;
//            }
//
//            @Override
//            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//            }
//
//            @Override
//            public int getItemCount() {
//                return 20;
//            }
//        });
//        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        return rootView;
    }

    public RecyclerView getRecyclerView() {
        return this.recyclerView;
    }

    public View getNewItemButtonBar() {
        return this.newItemButtonBar;
    }

//    public boolean canScrollVertically(int direction) {
//        return recyclerView.canScrollVertically(direction);
//    }

    double totalKCal = -1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_NEW_ITEM_FOOD && resultCode == Activity.RESULT_OK) {
            final float kCals = data.getLongExtra(FoodChooserActivity.RESULT_ARG_FOOD_KCAL, -1);
            final String foodName = data.getStringExtra(FoodChooserActivity.RESULT_ARG_FOOD_NAME);

            if (kCals != -1 && foodName != null) {
//                Toast.makeText(getActivity(), foodName + "\n(" + kCals + " kCals)", Toast.LENGTH_SHORT).show();
                FITApplication.getUserDataRealm().executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        /**
                         *  CaloriesEntry
                         */
                        CaloriesEntry newEntry = realm.createObject(CaloriesEntry.class);
                        newEntry.setKcals(kCals);
                        newEntry.setTitle(foodName);
                        newEntry.setTime(date);
                        newEntry.setType(CaloriesEntry.TYPE_FOOD);

                        realm.copyToRealm(newEntry);

                        Log.d("User", "new food entry added!! (id = " + newEntry.getId() + ", title = " + newEntry.getTitle() + ", kcals = " + newEntry.getKcals() + ")");


                        /**
                         *  Recalculate DailyData's total kcals
                         */
                        DailyData dailyData = realm.where(DailyData.class).equalTo("date", date).findFirst();
                        totalKCal = dailyData.getCurrentKCal() + kCals;
                        dailyData.setCurrentKCal(totalKCal);

                        realm.copyToRealm(dailyData);

                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        refreshAsync();
                        mCaloriesEntriesAdapter.notifyItemInserted(0);

                        /**
                         *  Update the Header
                         */
                        if (mKCalsChangedListener != null)
                            mKCalsChangedListener.onKCalsChanged(totalKCal);
                    }
                });
            }
            else {
                Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void refreshAsync() {
        /**
         *  Load CaloriesEntries
         */
        Realm userDataRealm = FITApplication.getUserDataRealm();
        userDataRealm.where(CaloriesEntry.class).equalTo("time", date).findAllAsync().addChangeListener(new RealmChangeListener<RealmResults<CaloriesEntry>>() {
            @Override
            public void onChange(RealmResults<CaloriesEntry> caloriesEntries) {
                ArrayList<CaloriesEntry> list = new ArrayList<>(caloriesEntries);
                int halfTheList = list.size() / 2;
                for (int i = 0; i < halfTheList; i++) {
                    int i2 = list.size() - 1 - i;

                    CaloriesEntry front = list.get(i);
                    CaloriesEntry back = list.get(i2);
                    list.set(i, back);
                    list.set(i2, front);
                }

                mCaloriesEntriesAdapter = new GFitCaloriesEntryAdapter(getActivity(), list);
                mCaloriesEntriesAdapter.setOnItemLongPressedListener(new GFitCaloriesEntryAdapter.OnItemLongPressedListener() {
                    @Override
                    public void onItemLongPressed(final GFitCaloriesEntryAdapter.ViewHolder viewHolder) {
                        // TODO: show pop up menu for item removing
                        PopupMenu popupMenu = new PopupMenu(getActivity(), viewHolder.itemView);
                        popupMenu.inflate(R.menu.action_menu_remove);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.action_remove:
                                        final double itemKCals = viewHolder.foodData.getKcals();

                                        mCaloriesEntriesAdapter.showDeleteDialog(getActivity(), viewHolder,
                                                new GFitCaloriesEntryAdapter.DeleteDialogCallback() {
                                            @Override
                                            public void onItemDeleted() {
                                                /**
                                                 *  Recalculate DailyData's total kcals
                                                 */
                                                FITApplication.getUserDataRealm().executeTransaction(new Realm.Transaction() {
                                                    @Override
                                                    public void execute(Realm realm) {
                                                        DailyData dailyData = realm.where(DailyData.class).equalTo("date", date).findFirst();
                                                        totalKCal = dailyData.getCurrentKCal() - itemKCals;
                                                        dailyData.setCurrentKCal(totalKCal);

                                                        realm.copyToRealm(dailyData);

                                                        if (mKCalsChangedListener != null)
                                                            mKCalsChangedListener.onKCalsChanged(totalKCal);
                                                    }
                                                });
                                            }
                                        });
                                        break;
                                }
                                return false;
                            }
                        });
                        popupMenu.setGravity(Gravity.RIGHT);
                        popupMenu.show();

                        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(15);
                    }
                });

                recyclerView.setAdapter(mCaloriesEntriesAdapter);
//                mPagerAdapter.setDailyDataList(dailyDataArray);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public interface OnKCalsChangedListener {
        void onKCalsChanged(double newKCals);
    }
}
