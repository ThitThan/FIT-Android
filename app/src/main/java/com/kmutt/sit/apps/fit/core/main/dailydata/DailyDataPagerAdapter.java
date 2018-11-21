package com.kmutt.sit.apps.fit.core.main.dailydata;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.kmutt.sit.apps.fit.core.main.dailydata.DailyDataFragmentV2;
import com.kmutt.sit.apps.fit.core.main.dailydata.realm.DailyData;
import com.kmutt.sit.apps.fit.utils.DateTimeUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmResults;

/**
 *   FRAGMENT PAGER ADAPTER
 */
public class DailyDataPagerAdapter extends FragmentStatePagerAdapter {

    DailyDataFragmentV2.OnKCalsChangedListener mKCalChangedListener;

    /**
     *  List of DailyDatas
     */
    List<DailyData> dailyDataList;
    public void setDailyDataList(List<DailyData> dailyDataList) {
        this.dailyDataList = dailyDataList;
        Log.d(getClass().getSimpleName(), "setDailyDataList()");
        Log.d(getClass().getSimpleName(), "dailyDatas count = " + dailyDataList.size());
        notifyDataSetChanged();
    }

    public DailyDataPagerAdapter(FragmentManager fm, DailyDataFragmentV2.OnKCalsChangedListener listener) {
        super(fm);
        this.mKCalChangedListener = listener;
    }

    // Hold the active fragments
    private HashMap<Integer, DailyDataFragmentV2> mPageReferenceMap;

    // Create a fragment @ provided position
    @Override
    public Fragment getItem(int position) {
        Date today = DateTimeUtil.getTodaysDateAt0AM();

        Date itemDate = dailyDataList.get(position).getDate();
        boolean shouldShowButtonBar = today.compareTo(itemDate) == 0;

        DailyDataFragmentV2 fragment = DailyDataFragmentV2.newInstance(itemDate, shouldShowButtonBar);
        fragment.setOnKCalsChangedListener(mKCalChangedListener);
        return fragment;
    }

    // Destroy fragment @ provided position
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        DailyDataFragmentV2 fragment = (DailyDataFragmentV2) super.instantiateItem(container, position);

        if (mPageReferenceMap == null)
            mPageReferenceMap = new HashMap<>();
        mPageReferenceMap.put(position, fragment);

        return fragment;
    }

    // Get active fragment @ provided position
    public DailyDataFragmentV2 getActiveFragmentAt(int position) {
        if (mPageReferenceMap == null)
            return null;
        return mPageReferenceMap.get(position);
    }

    @Override
    public int getCount() {
        if (dailyDataList == null)
            return 0;
        else
            return dailyDataList.size();
    }

    public DailyData getDailyDataAt(int position) {
        return dailyDataList.get(position);
    }
}
