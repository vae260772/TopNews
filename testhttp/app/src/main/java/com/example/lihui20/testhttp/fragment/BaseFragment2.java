package com.example.lihui20.testhttp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lihui20.testhttp.R;
import com.example.lihui20.testhttp.activity.MainActivity;
import com.example.lihui20.testhttp.adapter.CustomGridViewAdapter;
import com.example.lihui20.testhttp.database.NewsCacheUtils;
import com.example.lihui20.testhttp.interface2.IFragmentVisible;
import com.example.lihui20.testhttp.model.Data;
import com.example.lihui20.testhttp.tools.Utils;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;


public class BaseFragment2 extends Fragment {
    boolean isFirstLoad = true;
    PullToRefreshGridView pullToRefreshGridView;
    GridView gridview;
    CustomGridViewAdapter customGridViewAdapter;
    TextView empty, title;
    List resultList = new ArrayList<>();
    boolean isFirstResume = true;
    Handler mHandler;
    List<Data> mList;
    Bundle bundle;
    String old_updateTime;
    UpdateRunnable r = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type;
    boolean isRefreshing = false;
    private BGABanner mBanner;
    SettingInterface settingInterface;
    String TAG1 = "test65";
    String typeFragment = "";
    boolean fragmentVisiable;

    public boolean isDeleteOrAdd() {
        return deleteOrAdd;
    }

    public void setDeleteOrAdd(boolean deleteOrAdd) {
        this.deleteOrAdd = deleteOrAdd;
    }

    private boolean deleteOrAdd;

    public interface SettingInterface {
        void setOnSetClick(boolean show);
    }

    public BaseFragment2() {
        super();
        Log.d("lihui", "test BaseFragment");

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("BaseFragment531", "typeFragment---" + typeFragment + "," + "BaseFragment onCreateOptionsMenu");

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = getArguments();
        type = Utils.getKey(bundle.getString("type"));
        typeFragment = type;
        Log.d("BaseFragment531", "typeFragment---" + typeFragment + "," + "BaseFragment onCreate");

        Log.d("BaseFragment531", "typeFragment---" + typeFragment + "," + "创建的BaseFragment type---" + type);

        Log.d("BaseFragment", "中文 bundle.getString(\"type\"---" + bundle.getString("type"));
        Log.d("BaseFragment", "英文 key type---" + type);
        old_updateTime = getUpdateTime();//第一次 oncreate时候
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        //返回布局
        Log.d("BaseFragment531", "typeFragment---" + typeFragment + "," + "BaseFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment, container, false);
        empty = (TextView) view.findViewById(R.id.empty);
        //
        com.example.lihui20.testhttp.animation.AnimationUtils.alphaAnimation(empty);
        //
        title = (TextView) view.findViewById(R.id.title);
        title.setText(bundle.getString("type"));
        pullToRefreshGridView = (PullToRefreshGridView) view.findViewById(R.id.listView);
        pullToRefreshGridView.setVerticalScrollBarEnabled(true);//
        //
        gridview = pullToRefreshGridView.getRefreshableView();
        gridview.setCacheColorHint(Color.WHITE);
        gridview.setVerticalScrollBarEnabled(false);//
        gridview.setFadingEdgeLength(0);// 删除黑边（上下）
        //
        pullToRefreshGridView.setMode(PullToRefreshBase.Mode.BOTH);
        // ILoadingLayout endLoading = pullToRefreshGridView.getLoadingLayoutProxy(false,
        //          true);
        //头部
        ILoadingLayout topLoading = pullToRefreshGridView.getLoadingLayoutProxy(true,
                false);
        //底部
        ILoadingLayout endLoading = pullToRefreshGridView.getLoadingLayoutProxy(false,
                true);
        topLoading.setRefreshingLabel("玩命加载中...");

        endLoading.setRefreshingLabel("玩命加载中...");
        topLoading.setReleaseLabel("放开以刷新...");
        endLoading.setReleaseLabel("放开以刷新...");
        //初始化
//        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
//                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//        // Update the LastUpdatedLabel
        //    pullToRefreshGridView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

        pullToRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
//                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
//                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                // Update the LastUpdatedLabel
//                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//                // Do work to refresh the list here.
                Log.d("lihui", "102 isRefreshing---" + isRefreshing);
                if (!isRefreshing) {
                    isRefreshing = true;
                    Log.d("lihui", "105 isRefreshing---" + isRefreshing);
                    refresh();
                }
            }

            private void refresh() {
                pullToRefreshGridView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resultList = getResult();
                        Log.d("list", "list---" + resultList);
                    }
                }, 3000);
            }
        });

        mBanner = (BGABanner) view.findViewById(R.id.banner);

        mBanner.setAdapter(new BGABanner.Adapter() {
            ImageView itemView;

            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                itemView = (ImageView) view;

                Glide.with(itemView.getContext())
                        .load(model)
                        .placeholder(R.drawable.holder)
                        .error(R.drawable.holder)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .fitCenter()
                        .into(itemView);
            }
        });
        //
        return view;
    }

    private List<Data> getResult() {
        resultList = Utils.getResult(getActivity(), type,
                pullToRefreshGridView, resultList, empty, mHandler);
        //getResult异步的
        Log.d("BaseFragment", "pullRefresh resultList---" + resultList);

        return resultList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("BaseFragment531", "typeFragment---" + typeFragment + "," + "BaseFragment onActivityCreated");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("BaseFragment531", "typeFragment---" + typeFragment + "," + "BaseFragment onAttach");

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    public void onResume() {
        super.onResume();
        Log.d("BaseFragment531", "typeFragment---" + typeFragment + "," + "BaseFragment onResume");
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d("lihui", "(List<Data>) msg.obj---" + msg.obj);
                if (msg.obj != null) {
                    switch (msg.what) {
                        case 0:
                            mList = (List<Data>) msg.obj;
                            customGridViewAdapter.notifyDataSetChanged();
                            loadBannerData(mList);
                            pullToRefreshGridView.onRefreshComplete();
                            isRefreshing = false;
                            settingInterface.setOnSetClick(false);
                            //    ((MainActivity) getActivity()).isShowSetting(false);

                            break;
                        case 1:
                            // Throwable t = (Throwable) msg.obj;
                            String type = (String) msg.obj;
                            Log.d("lihui", "184 type---" + type);
                            //  if (t instanceof UnknownHostException) {
                            //加载缓存
                            List list = NewsCacheUtils.getInstance(getActivity()).queryAllByType(type);
                            if (list != null && list.size() > 0) {
                                Log.d("cache", "list---" + list);
                                Utils.resetList(resultList, list);//返回 handler，更新adapter
                                customGridViewAdapter.notifyDataSetChanged();
                                loadBannerData(resultList);
                                empty.setVisibility(View.GONE);
                            }
                            //    settingInterface.setOnSetClick(true);
                            if (getActivity() != null) {
                                ((MainActivity) getActivity()).isShowSetting(true);
                            }
                            //      ToastUtils.showToast(getActivity(), "网络异常，请稍后重试");
                            //   }
                            pullToRefreshGridView.onRefreshComplete();
                            isRefreshing = false;
                            break;
                        default:
                            pullToRefreshGridView.onRefreshComplete();
                            isRefreshing = false;
                            break;
                    }
                }
            }
        };
//回调
        initAdapter();
        //针对第一页
        if (isFirstResume) {
            if (fragmentVisiable) {
                //   if (iFragmentVisible != null) {
                //  iFragmentVisible.onFragmentVisible(true);
                List list = getResult();
                Log.d("BaseFragment", "111 isFirstResume list---" + list);
                isFirstResume = false;
            }
        }
        //其他页加载isFirstResume=true
        iFragmentVisible = new IFragmentVisible() {
            @Override
            public void onFragmentVisible(boolean visible) {
                if (visible && isFirstResume) {
                    List list = getResult();
                    Log.d("BaseFragment", "222 isFirstResume list---" + list);
                }
                isFirstResume = false;
            }
        };
        setFragmentVisible(iFragmentVisible);
        //
        //默认更新设置
        //是否打开开关---更新频率时间
        Log.d("update66", "openUpdate---" + openUpdate());
        Log.d("update66", "old_updateTime---" + old_updateTime);
        Log.d("update66", "getUpdateTime()---" + getUpdateTime());

        if (openUpdate()) {//打开开关
            if (!TextUtils.isEmpty(old_updateTime)
                    && !old_updateTime.equals(getUpdateTime())) {
                Log.d("update66","fragment---"+type+",开启自动更新"+",每过"+getUpdateTime()+"s进行更新...");
                Log.d("update66", "1old_updateTime---" + old_updateTime);
                old_updateTime = getUpdateTime();//修改时间
                Log.d("update66", "2old_updateTime---" + old_updateTime);
                //关闭之前的计时器
                if (r != null) {
                    mHandler.removeCallbacks(r);
                }

                r = new UpdateRunnable(old_updateTime);
//重新设置了更新时间
                mHandler.postDelayed(r, Long.parseLong(old_updateTime) * 60 * 1000);//过time执行一次
            }
        } else {//关闭开关
            if (r != null) {
                mHandler.removeCallbacks(r);
            }
        }
    }

    private class UpdateRunnable implements Runnable {
        String time;

        UpdateRunnable(String time) {
            this.time = time;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
            //
            List list = getResult();
            Log.d("update66", "getResult()---" + list.toString());
            customGridViewAdapter.notifyDataSetChanged();
            //
            mHandler.postDelayed(this, Long.parseLong(time) * 60 * 1000);//每隔time执行一次
        }
    }

    private boolean openUpdate() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean update_key = settings.getBoolean(getString(R.string.update_key), false);//默认关闭
        Log.d("update66","openUpdate() update_key---"+update_key);
        return update_key;//false  true
    }

    private String getUpdateTime() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String updatetime = settings.getString(getString(R.string.auto_update_frequency_key), "0");
        Log.d("update66","getUpdateTime() updatetime---"+updatetime);
        return updatetime;
    }


    IFragmentVisible iFragmentVisible;

    private void setFragmentVisible(IFragmentVisible iFragmentVisible) {
        this.iFragmentVisible = iFragmentVisible;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("update66","setUserVisibleHint type---"+type);
        Log.d("update66","setUserVisibleHint isVisibleToUser---"+isVisibleToUser);

        //界面可见
        if (isVisibleToUser) {
            fragmentVisiable = true;//开始默认第一页
            if (iFragmentVisible != null) {//其他页加载用回调
                iFragmentVisible.onFragmentVisible(true);
            }
        } else {
            fragmentVisiable = false;
        }
    }


    private void initAdapter() {
        //Adapter绑定数据源resultList
        customGridViewAdapter = new CustomGridViewAdapter(getActivity(), resultList);
        //绑定适配器
        gridview.setAdapter(customGridViewAdapter);
        //间距
        //  gridview.addItemDecoration(new SpacesItemDecoration((int) Utils.dip2px(8)));
        Log.d("Utils", "empty.setVisibility(View.GONE)");
        isFirstLoad = false;
        Log.d("Utils", "BaseFragment.isFirstLoad = false");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("BaseFragment531", "typeFragment---" + typeFragment + "," + "BaseFragment onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("BaseFragment531", "typeFragment---" + typeFragment + "," + "BaseFragment onStop");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (r != null) {
            mHandler.removeCallbacks(r);
        }
        //
        Log.d("BaseFragment531", "typeFragment---" + typeFragment + "," + "BaseFragment onDestroyView");
        //按返回键，正常流程，也需要保存
        HashMap<String, List<Data>> map = Utils.cacheMap;
        Log.d("BaseFragment", "map ---" + map);
        Log.d("BaseFragment", "246");
        //先清空老的数据，保存最新的数据

        NewsCacheUtils utils = NewsCacheUtils.getInstance(getActivity());
        if (map != null && map.size() > 0) {
            utils.clearAll();
            utils.insert(map);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("BaseFragment531", "typeFragment---" + typeFragment + "," + "BaseFragment onDestroy");
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("BaseFragment", "BaseFragment onSaveInstanceState");
        //发送广播，让各个fragment将数据保存起来
        HashMap<String, List<Data>> map = Utils.cacheMap;
        Log.d("BaseFragment", "map ---" + map);
        Log.d("BaseFragment", "246");
        //先清空老的数据，保存最新的数据


        NewsCacheUtils utils = NewsCacheUtils.getInstance(getActivity());
        if (map != null && map.size() > 0) {
            utils.clearAll();
            utils.insert(map);
        }
        Log.d("BaseFragment", "BaseFragment onDestroyView");
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("BaseFragment531", "typeFragment---" + typeFragment + "," + "BaseFragment onDetach");
    }

    private void loadBannerData(final List<Data> list) {
        List<String> imageUrl = new ArrayList<>();
        List<String> title = new ArrayList<>();
        Log.d("lihui", "209 list---" + list.toString());
        if (list != null && list.size() > 4) {
            for (int i = 0; i < 5; i++) {
                imageUrl.add(list.get(i).getThumbnail_pic_s());
                title.add(list.get(i).getTitle());
            }
            mBanner.setData(imageUrl, title);
            mBanner.setOnItemClickListener(new BGABanner.OnItemClickListener() {
                @Override
                public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {
//                    Intent intent = new Intent();
//                    intent.setAction("android.intent.action.VIEW");
//                    Uri content_url = Uri.parse(list.get(position).getUrl());
//                    intent.setData(content_url);
//                    startActivity(intent);
                    String url = list.get(position).getUrl();
                    Utils.openURL(getContext(), url);

                }
            });

        }
    }

    public void setOnSetClickListener(SettingInterface settingInterface) {
        this.settingInterface = settingInterface;
    }
}
