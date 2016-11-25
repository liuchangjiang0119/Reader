package com.shanbay.reader.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by windfall on 16-11-23.
 */

public class FabBehavior extends CoordinatorLayout.Behavior {
/**
 * 第三方FloatingActionsMenu和CoordinatorLayout配合不起作用，
 * 仍然会被SnackBar覆盖掉，需要自定义一个Behavior并在xml文件中引用
 * 需要重写layoutDependon方法和onDependentViewChanged
* */



    public FabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }
}
