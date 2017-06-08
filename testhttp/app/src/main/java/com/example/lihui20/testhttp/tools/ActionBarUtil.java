package com.example.lihui20.testhttp.tools;

import android.app.ActionBar;
import android.os.Build;
import android.text.AndroidCharacter;

import com.example.lihui20.testhttp.R;

/**
 * Created by lihui20 on 2017/1/4.
 */
public class ActionBarUtil {

    public static void showCustomActionBar(ActionBar actionBar, boolean customview, boolean showtitle, boolean backicon) {
     //   actionBar.setCustomView(R.layout.custom_actionbar);
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);//返回箭头HomeAsUp显示
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);//应用的logo图标
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            actionBar.setHomeAsUpIndicator(0);
//        }
    }

    public static void showSupportActionBar(android.support.v7.app.ActionBar actionBar, boolean customview, boolean showtitle, boolean backicon) {
      //  actionBar.setCustomView(R.layout.custom_actionbar);
      //  actionBar.setDisplayShowCustomEnabled(false);
          actionBar.setDisplayHomeAsUpEnabled(true);//返回箭头HomeAsUp显示
        actionBar.setDisplayShowHomeEnabled(false);//应用的logo图标显示
        //actionBar.setDisplayShowHomeEnabled(true);
        // actionBar.setLogo(R.drawable.logo);
        // actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//标题titile文字

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            actionBar.setHomeAsUpIndicator(0);
//        }
    }
}

