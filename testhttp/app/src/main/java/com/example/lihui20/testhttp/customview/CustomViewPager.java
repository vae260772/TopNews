package com.example.lihui20.testhttp.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by lihui20 on 2017/2/6.
 */
public class CustomViewPager extends ViewPager {
    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, false);
    }
}

