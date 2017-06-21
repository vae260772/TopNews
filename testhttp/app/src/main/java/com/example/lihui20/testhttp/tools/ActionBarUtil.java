package com.example.lihui20.testhttp.tools;

import android.app.ActionBar;

/**
 * Created by lihui20 on 2017/1/4.
 */
public class ActionBarUtil {

    public static void showCustomActionBar(ActionBar actionBar, boolean customview, boolean showtitle, boolean backicon,boolean logo) {
        actionBar.setDisplayShowCustomEnabled(customview);//自定义view
        actionBar.setDisplayShowTitleEnabled(showtitle);//应用名称
        actionBar.setDisplayHomeAsUpEnabled(backicon);//返回箭头HomeAsUp显示
        actionBar.setDisplayShowHomeEnabled(logo);//应用的logo图标
    }
}

