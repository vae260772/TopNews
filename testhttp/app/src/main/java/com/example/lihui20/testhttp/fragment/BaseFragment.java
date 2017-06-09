package com.example.lihui20.testhttp.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lihui20.testhttp.R;
import com.example.lihui20.testhttp.activity.MainActivity;
import com.example.lihui20.testhttp.adapter.CustomRecyclerViewAdapter;
import com.example.lihui20.testhttp.customview.PullToRefreshRecyclerView;
import com.example.lihui20.testhttp.customview.SpacesItemDecoration;
import com.example.lihui20.testhttp.database.NewsCacheUtils;
import com.example.lihui20.testhttp.model.Data;
import com.example.lihui20.testhttp.tools.ToastUtils;
import com.example.lihui20.testhttp.tools.Utils;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;


public class BaseFragment extends Fragment {
    boolean isFirstLoad = true;
    PullToRefreshRecyclerView pullToRefreshrecycleview;
    RecyclerView recycleview;
    CustomRecyclerViewAdapter customRecyclerViewAdapter;
    TextView empty, title;
    List resultList = new ArrayList<>();
    boolean isFirstResume = true;
    android.os.Handler mHndler;
    List<Data> mList;
    Bundle bundle;
    String type;
    boolean isRefreshing = false;
    private BGABanner mBanner;
    SettingInterface settingInterface;

    public interface SettingInterface {
        void setOnSetClick(boolean show);
    }

    public BaseFragment() {
        super();
        Log.d("lihui", "test BaseFragment");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("BaseFragment", "BaseFragment onCreateOptionsMenu");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseFragment", "BaseFragment onCreate");
        bundle = getArguments();
        type = Utils.getKey(bundle.getString("type"));
        Log.d("BaseFragment", "中文 bundle.getString(\"type\"---" + bundle.getString("type"));
        Log.d("BaseFragment", "英文 key type---" + type);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        //返回布局
        Log.d("BaseFragment", "BaseFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment, container, false);
        empty = (TextView) view.findViewById(R.id.empty);
        title = (TextView) view.findViewById(R.id.title);
        title.setText(bundle.getString("type"));
        pullToRefreshrecycleview = (PullToRefreshRecyclerView) view.findViewById(R.id.listView);
        recycleview = pullToRefreshrecycleview.getRefreshableView();
        //
        pullToRefreshrecycleview.setMode(PullToRefreshBase.Mode.BOTH);
        ILoadingLayout endLoading = pullToRefreshrecycleview.getLoadingLayoutProxy(false,
                true);
        endLoading.setPullLabel("上拉刷新...");
        //  recycleview = pullToRefreshrecycleview.getRefreshableView();
        pullToRefreshrecycleview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // Do work to refresh the list here.
                Log.d("lihui", "102 isRefreshing---" + isRefreshing);
                if (!isRefreshing) {
                    isRefreshing = true;
                    Log.d("lihui", "105 isRefreshing---" + isRefreshing);
                    refresh();
                }
            }

            private void refresh() {
                pullToRefreshrecycleview.postDelayed(new Runnable() {
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
        return view;
    }

    private List<Data> getResult() {
//        resultList = Utils.getResult(getActivity(), type,
//                pullToRefreshrecycleview, resultList, empty, mHndler);
//        //getResult异步的
//        Log.d("BaseFragment", "pullRefresh resultList---" + resultList);

        return resultList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    public void onResume() {
        super.onResume();
        Log.d("BaseFragment", "BaseFragment onResume");

        mHndler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d("lihui", "(List<Data>) msg.obj---" + msg.obj);
                if (msg.obj != null) {
                    switch (msg.what) {
                        case 0:
                            mList = (List<Data>) msg.obj;
                            customRecyclerViewAdapter.notifyDataSetChanged();
                            loadBannerData(mList);
                            pullToRefreshrecycleview.onRefreshComplete();
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
                                Utils.resetList(resultList, list);
                                customRecyclerViewAdapter.notifyDataSetChanged();
                                loadBannerData(resultList);
                                empty.setVisibility(View.GONE);
                            }
                            //    settingInterface.setOnSetClick(true);
                            ((MainActivity) getActivity()).isShowSetting(true);
                            ToastUtils.showToast(getActivity(), "网络异常，请稍后重试");

                            //   }
                            pullToRefreshrecycleview.onRefreshComplete();
                            isRefreshing = false;
                            break;
                        default:
                            pullToRefreshrecycleview.onRefreshComplete();
                            isRefreshing = false;
                            break;
                    }
                }
            }
        };

        if (isFirstResume) {
            initAdapter();
            List list = getResult();
            Log.d("BaseFragment", "isFirstResume list---" + list);
            isFirstResume = false;
        }

    }

    private void initAdapter() {
        //Adapter绑定数据源resultList
        customRecyclerViewAdapter = new CustomRecyclerViewAdapter(getActivity(), resultList);
        //绑定适配器
        recycleview.setAdapter(customRecyclerViewAdapter);
        //间距
        recycleview.addItemDecoration(new SpacesItemDecoration((int) Utils.dip2px(8)));
        Log.d("Utils", "empty.setVisibility(View.GONE)");
        isFirstLoad = false;
        Log.d("Utils", "BaseFragment.isFirstLoad = false");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("BaseFragment", "BaseFragment onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("BaseFragment", "BaseFragment onStop");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("BaseFragment", "BaseFragment onDestroy");

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("BaseFragment", "BaseFragment onSaveInstanceState");
        //发送广播，让各个fragment将数据保存起来
        HashMap<String, List<Data>> map = Utils.cacheMap;
        Log.d("BaseFragment", "map ---" + map);
        Log.d("BaseFragment", "246");

        NewsCacheUtils utils = NewsCacheUtils.getInstance(getActivity());
        utils.insert(map);
        Log.d("BaseFragment", "BaseFragment onDestroyView");
    }


    @Override
    public void onDetach() {
        super.onDetach();
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
