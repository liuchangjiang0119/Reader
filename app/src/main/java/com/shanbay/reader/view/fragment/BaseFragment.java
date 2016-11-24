package com.shanbay.reader.view.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by windfall on 16-11-21.
 */

public abstract class BaseFragment extends Fragment {
    public boolean isVisble;
    public boolean hasPrepared = false;

//重写方法，判断是否可见，可见则执行数据加载工作
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){

            isVisble = true;
            loadLessonList();

        }else {
            isVisble = false;

        }
    }


    public abstract void loadLessonList();
}
