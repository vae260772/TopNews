package com.example.lihui20.testhttp.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.smssdk.SMSSDK;

/**
 * Created by lihui20 on 2017/6/19.
 */

public class CustomNewsApplication extends Application {
    private static Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
        SMSSDK.initSDK(mContext, "1bc697ea4141c", "6733d8701dcd74df18cd836d710a9e73");
        Log.d("CustomNewsApplication","first onCreate");
    }
    public static Context getInstance() {
        if (mContext == null) {
            CustomNewsApplication application = new CustomNewsApplication();
            mContext = application.getApplicationContext();
        }
        return mContext;
    }
}
