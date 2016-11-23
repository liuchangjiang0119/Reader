package com.shanbay.reader.view.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by windfall on 16-11-21.
 */

public abstract class BaseFragment extends Fragment {
    public boolean isVisble;
    public boolean hasPrepared = false;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){

            isVisble = true;
            onVisible();

        }else {
            isVisble = false;

        }
    }
    public void onVisible(){
        loadLessonList();
    }

    public abstract void loadLessonList();
}
