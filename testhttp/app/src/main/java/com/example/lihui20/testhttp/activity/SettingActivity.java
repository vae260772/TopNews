package com.example.lihui20.testhttp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.lihui20.testhttp.R;

public class SettingActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    static final String TAG = "SettingActivity";
    SharedPreferences preference = null;
    CheckBoxPreference updateCheckBoxPreference = null;
    ListPreference lististPreference = null;
    ListPreference bglististPreference = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置显示Preferences
        addPreferencesFromResource(R.xml.preference);
        //获得SharedPreferences
        preference = PreferenceManager.getDefaultSharedPreferences(this);

        //找到preference对应的Key标签并转化
        updateCheckBoxPreference = (CheckBoxPreference) findPreference(getString(R.string.update_key));
        lististPreference = (ListPreference) findPreference(getString(R.string.auto_update_frequency_key));
        //
        bglististPreference = (ListPreference) findPreference(getString(R.string.viewbgcolor_key));

        //为Preference注册监听
        updateCheckBoxPreference.setOnPreferenceChangeListener(this);
        updateCheckBoxPreference.setOnPreferenceClickListener(this);

        lististPreference.setOnPreferenceClickListener(this);//点击
        lististPreference.setOnPreferenceChangeListener(this);//修改列表选项

        bglististPreference.setOnPreferenceClickListener(this);//点击
        bglististPreference.setOnPreferenceChangeListener(this);//修改列表选项
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        //判断是哪个Preference改变了
        if (preference.getKey().equals(getString(R.string.update_key))) {
            Log.d(TAG, getString(R.string.update_key));
        } else if (preference.getKey().equals(getString(R.string.isneilflag_key))) {
            Log.d(TAG, getString(R.string.isneilflag_key));
        }
        //返回true表示允许改变
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        //判断是哪个Preference改变了
        if (preference.getKey().equals(getString(R.string.update_key))) {
            //更新开关
            Log.d(TAG, getString(R.string.update_key));
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            boolean update_key = settings.getBoolean(getString(R.string.update_key), false);
            Log.d(TAG, "update_key---" + update_key);


        } else if (preference.getKey().equals(getString(R.string.auto_update_frequency_key))) {
            Log.d(TAG, getString(R.string.auto_update_frequency_key));
            //选择的值
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            String updatetime = settings.getString(getString(R.string.auto_update_frequency_key), "");
            Log.d(TAG, "updatetime---" + updatetime);

        } else if (preference.getKey().equals(getString(R.string.viewbgcolor_key))) {
            //选择背景
            //选择的值
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            String viewbgcolor_key = settings.getString(getString(R.string.viewbgcolor_key), "");
            Log.d(TAG, "viewbgcolor_key---" + viewbgcolor_key);
        }
        //返回true表示允许改变
        return true;
    }
}