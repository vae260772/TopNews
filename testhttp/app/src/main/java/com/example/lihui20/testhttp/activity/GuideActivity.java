package com.example.lihui20.testhttp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lihui20.testhttp.R;
import com.example.lihui20.testhttp.database.TypesDBUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGABannerUtil;

public class GuideActivity extends Activity implements View.OnClickListener {
    private static final String TAG = GuideActivity.class.getSimpleName();
    private TextView mSkipTv;
    private Button mEnterBtn;
    private BGABanner mBackgroundBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = this.getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (sp.getBoolean("showGuideActivity", true) != true) {
            startActivity(new Intent(this,MainActivity.class));
            GuideActivity.this.finish();
            return;
        }
        editor.putBoolean("showGuideActivity", false);
        editor.commit();

        initView();
        setListener();
        processLogic();
    }

    private void initView() {
        setContentView(R.layout.activity_guide);
        mSkipTv = (TextView) findViewById(R.id.tv_guide_skip);
        mEnterBtn = (Button) findViewById(R.id.btn_guide_enter);
        mBackgroundBanner = (BGABanner) findViewById(R.id.banner_guide_background);
    }

    private void setListener() {
        mSkipTv.setOnClickListener(this);
        mEnterBtn.setOnClickListener(this);
        // 监听页码切换事件，控制「跳过按钮」和「进入主界面的显示与隐藏」
        mBackgroundBanner.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == mBackgroundBanner.getItemCount() - 2) {
                    ViewCompat.setAlpha(mEnterBtn, positionOffset);
                    ViewCompat.setAlpha(mSkipTv, 1.0f - positionOffset);

                    if (positionOffset > 0.5f) {
                        mEnterBtn.setVisibility(View.VISIBLE);
                        mSkipTv.setVisibility(View.GONE);
                    } else {
                        mEnterBtn.setVisibility(View.GONE);
                        mSkipTv.setVisibility(View.VISIBLE);
                    }
                } else if (position == mBackgroundBanner.getItemCount() - 1) {
                    mSkipTv.setVisibility(View.GONE);
                    mEnterBtn.setVisibility(View.VISIBLE);
                    ViewCompat.setAlpha(mEnterBtn, 1.0f);
                } else {
                    mSkipTv.setVisibility(View.VISIBLE);
                    ViewCompat.setAlpha(mSkipTv, 1.0f);
                    mEnterBtn.setVisibility(View.GONE);
                }
            }
        });
    }

    private void processLogic() {
        mBackgroundBanner.setOverScrollMode(View.OVER_SCROLL_NEVER);
        List<View> views = new ArrayList<>();
        views.add(BGABannerUtil.getItemImageView(this, R.drawable.uoko_guide_background_1));
        views.add(BGABannerUtil.getItemImageView(this, R.drawable.uoko_guide_background_2));
        views.add(BGABannerUtil.getItemImageView(this, R.drawable.uoko_guide_background_3));
        mBackgroundBanner.setData(views);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_guide_enter || view.getId() == R.id.tv_guide_skip) {
            //首次打开,数据库初始化4个标签分类
            Intent intent=new Intent(GuideActivity.this, MainActivity.class);
            TypesDBUtils instance=TypesDBUtils.getInstance(this);
            List<String> typeList = new ArrayList<>(Arrays.asList(new String[]{"头条", "社会", "国内", "国际"}));
            instance.insertTypesList(typeList);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在界面可见时给背景Banner设置一个白色背景，避免滑动过程中两个Banner都设置透明度后能看到Launcher
        mBackgroundBanner.setBackgroundResource(android.R.color.white);
    }
}