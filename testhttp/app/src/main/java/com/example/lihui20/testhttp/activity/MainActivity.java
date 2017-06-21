package com.example.lihui20.testhttp.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lihui20.testhttp.R;
import com.example.lihui20.testhttp.customview.CustomViewPager;
import com.example.lihui20.testhttp.database.TypesDBUtils;
import com.example.lihui20.testhttp.fragment.BaseFragment2;
import com.example.lihui20.testhttp.tools.ActionBarUtil;
import com.example.lihui20.testhttp.tools.FragmentFactory;
import com.example.lihui20.testhttp.tools.NetWorkUtils;
import com.example.lihui20.testhttp.tools.ToastUtils;
import com.example.lihui20.testhttp.tools.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private final static String TAG = "MainActivity";
    FragmentManager fm;
    SlidingMenu menu;
    Context mContext;
    List<String> titleList;
    CustomAdapter mAdapter;
    TabPageIndicator mIndicator;
    CustomViewPager mPager;
    List<String> selectAddTypeList;
    List<String> selectRemoveTypeList;
    private final int ALLPAGE = 9;
    private TypesDBUtils instance;
    private static TextView set;
    CustomeNetBroadcastReceiver receiver;
    LinearLayout mainll;
    String old_viewbgcolor_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_tabs);
        //注册
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        receiver = new CustomeNetBroadcastReceiver();
        registerReceiver(receiver, filter);
        Log.d(TAG, "getActionBar()---" + getActionBar());
        android.app.ActionBar actionBar = getActionBar();
        ActionBarUtil.showCustomActionBar(actionBar, false, true, false, true);

        Log.d("MainActivity", "first onCreate");

        initViews();
        initSlidingMenu();
    }

    public static class CustomeNetBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            Log.d(TAG, "CONNECTIVITY_ACTION---" + intent.getAction());
//   打开、关闭数据连接或者断开和连接或者断开某一个wifi网络连接
            if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                if (activeNetwork != null) { // connected to the internet
                    isShowSetting(false);
                    Log.d(TAG, "info.getTypeName()" + activeNetwork.getTypeName());
                    Log.d(TAG, "getSubtypeName()" + activeNetwork.getSubtypeName());
                    Log.d(TAG, "getState()" + activeNetwork.getState());
                    Log.d(TAG, "getDetailedState()"
                            + activeNetwork.getDetailedState().name());
                    Log.d(TAG, "getDetailedState()" + activeNetwork.getExtraInfo());
                    Log.d(TAG, "getType()" + activeNetwork.getType());
                } else {
                    NetWorkUtils.setOpen(false);
                    Log.d(TAG, "当前没有网络连接，请确保你已经打开网络 ");
                    isShowSetting(true);
                }
            }
        }
    }

    private void initSlidingMenu() {
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeEnabled(false);

        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        menu.setMenu(R.layout.left_menu);
        (menu.findViewById(R.id.about)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AboutActivity.class);
                startActivity(intent);

            }
        });
        (menu.findViewById(R.id.setting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
            }
        });

        (menu.findViewById(R.id.favorite)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, FavoriteActivity.class);
                startActivity(intent);


            }
        });

        (menu.findViewById(R.id.music)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MusicActivity.class);
                startActivity(intent);

            }
        });

        (menu.findViewById(R.id.login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);


            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("lihui", "MainActivity onConfigurationChanged");
    }

    private void initViews() {
        mContext = this;
        fm = getSupportFragmentManager();
        titleList = new ArrayList<>();
        old_viewbgcolor_key = getCurrentViewbgColor();
        mainll = (LinearLayout) findViewById(R.id.mainll);
        set = (TextView) findViewById(R.id.set);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });

        instance = TypesDBUtils.getInstance(this);
        //tab 新闻类型保存
        List<String> typeList = instance.queryAll();
        mPager = (CustomViewPager) findViewById(R.id.pager);
        mAdapter = new CustomAdapter(fm);
        mAdapter.setList(typeList);

        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(ALLPAGE);
        mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();


        //需要更新背景
        if (TextUtils.isEmpty(getCurrentViewbgColor())) {
            return;
        }
        Log.d("main", "getCurrentViewbgColor()---" + getCurrentViewbgColor());
        updateBgColor(getCurrentViewbgColor());
    }

    private void updateBgColor(String viewbgcolor_key) {
        switch (viewbgcolor_key) {

            case "black":
                mainll.setBackgroundResource(android.R.color.black);
                break;
            case "brown":
                mainll.setBackgroundResource(R.color.setbrown);
                break;
            case "gary":
                mainll.setBackgroundResource(R.color.gary);
                break;
        }
    }

    private String getCurrentViewbgColor() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String viewbgcolor_key = settings.getString(getString(R.string.viewbgcolor_key), "");
        return viewbgcolor_key;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("lihui", " 162onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lihui", "MainActivity onDestroy");
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    public void onPause() {
        super.onStop();
        Log.d("lihui", "167onPause");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem add = menu.findItem(R.id.add);
        final MenuItem remove = menu.findItem(R.id.remove);
        add.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("2lihui", "add---" + mAdapter.getCount());
                if (mAdapter.getCount() == Utils.MAX_PAGE) {
                    ToastUtils.showToast("sorry,暂无分类可以添加了亲...");
                    return true;
                }
                selectAddTypeList = new ArrayList();//新的对象
                //显示所有支持的分类，多选dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final List<String> typeList = mAdapter.getTypeList();//已经存在的分类
                final String[] addableArrays = Utils.getAddableTypes(typeList);
                builder.setMultiChoiceItems(addableArrays, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            selectAddTypeList.add(addableArrays[which]);
                        } else {
                            selectAddTypeList.remove(addableArrays[which]);
                        }
                        Log.d("1lihui", "selectAddTypeList---" + selectAddTypeList.toString());

                    }
                }).setTitle("可添加的分类").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("lihui", "selectAddTypeList---" + selectAddTypeList);
                        mAdapter.addTypeList(selectAddTypeList);
                        instance.insertTypesList(selectAddTypeList);
                        mIndicator.notifyDataSetChanged();
                        Log.d("lihui", "mAdapter.getTypeList()---" + mAdapter.getTypeList());
                        //处于最后一页
                        mPager.setCurrentItem(mAdapter.getCount() - 1);
                        Log.d("lihui", "mAdapter.getCount()-1---" + (mAdapter.getCount() - 1));
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return false;
            }
        });

        remove.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("2lihui", "remove---" + mAdapter.getCount());
                if (mAdapter.getCount() == Utils.MIN_PAGE) {
                    ToastUtils.showToast("当前是最后一页了亲，请手下留情...");
                    return true;
                }
                selectRemoveTypeList = new ArrayList();//新的对象
                //显示所有支持的分类，多选dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                final List<String> typeList = mAdapter.getTypeList();
                List<String> removeList = new ArrayList(typeList);
                final String currentTitle = (String) mAdapter.getPageTitle(mPager.getCurrentItem());
                removeList.remove(currentTitle);//当前所处分类是不可移除
                final String[] removeArrays = removeList.toArray(new String[removeList.size()]);
                builder.setMultiChoiceItems(removeArrays, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            selectRemoveTypeList.add(removeArrays[which]);
                        } else {
                            selectRemoveTypeList.remove(removeArrays[which]);
                        }
                        Log.d("1lihui", "selectRemoveTypeList---" + selectRemoveTypeList.toString());

                    }
                }).setTitle("可移除的分类").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("lihui", "selectRemoveType---" + selectRemoveTypeList);
                        mAdapter.removeTypeList(selectRemoveTypeList);
                        instance.deleteTypesList(selectRemoveTypeList);
                        mIndicator.notifyDataSetChanged();
                        int newPos = Utils.getPosition(mAdapter.getTypeList(), currentTitle);//移除前面一个，会调转到下一个页面的bug
                        Log.d("newPos", "newPos---" + newPos);
                        mPager.setCurrentItem(newPos);
                        Log.d("lihui", "mAdapter.getTypeList()---" + mAdapter.getTypeList());
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return false;
            }
        });

        return true;
    }

    private static Fragment createFragmentByType(String type) {
        BaseFragment2 bf = (BaseFragment2) FragmentFactory.newInstance(type);
        bf.setOnSetClickListener(new BaseFragment2.SettingInterface() {
            @Override
            public void setOnSetClick(boolean show) {
                showSetting(show);
            }
        });
        return bf;
    }

    public static void showSetting(boolean show) {
        if (show) {
            set.setVisibility(View.VISIBLE);
            ToastUtils.showToast("网络异常，请稍后重试");
        } else {
            set.setVisibility(View.GONE);
        }
    }

    public static class CustomAdapter extends FragmentStatePagerAdapter {
        List<String> fragmentTitlesList;

        CustomAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setList(List<String> list) {
            fragmentTitlesList = new ArrayList<>(list);
            Log.d("lihui1555", "364 fragmentTitlesList---" + fragmentTitlesList);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Log.d("lihui1555", "123 getPageTitle position---" + position);

            return fragmentTitlesList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("lihui1555", "123 Fragment getItem---" + position);
            return createFragmentByType(fragmentTitlesList.get(position));
        }

        @Override
        public int getCount() {
            Log.d("lihui1555", "getCount---" + fragmentTitlesList.size());
            return fragmentTitlesList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            Log.d("lihui1555", "getItemPosition object---" + object);
            return POSITION_NONE;
        }

        public void removeTypeList(List<String> selectRemoveTypeList) {
            for (String selectType : selectRemoveTypeList) {
                fragmentTitlesList.remove(selectType);
            }
            notifyDataSetChanged();
        }

        public void addTypeList(List<String> selectAddTypeList) {
            for (String selectType : selectAddTypeList) {
                fragmentTitlesList.add(selectType);
            }
            notifyDataSetChanged();
        }

        public List<String> getTypeList() {
            return fragmentTitlesList;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //
            Log.d("lihui1555", "destroyItem position---" + position);
            super.destroyItem(container, position, object);
            Log.d("lihui1555", "destroyItem object---" + object);

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d("lihui1555", "instantiateItem object---" + position);
            return super.instantiateItem(container, position);
        }
    }

    public static void isShowSetting(boolean show) {
        showSetting(show);
    }
}