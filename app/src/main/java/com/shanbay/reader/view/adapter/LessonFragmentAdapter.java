package com.shanbay.reader.view.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.List;

/**
 * Created by windfall on 16-11-20.
 */

public class LessonFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mList;
    public LessonFragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        mList = list;

    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }


}
