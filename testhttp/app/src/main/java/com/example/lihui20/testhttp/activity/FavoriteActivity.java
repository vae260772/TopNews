package com.example.lihui20.testhttp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.lihui20.testhttp.R;
import com.example.lihui20.testhttp.adapter.NewsAdapter;
import com.example.lihui20.testhttp.database.DBUtils;
import com.example.lihui20.testhttp.interface2.CustomOnclick;
import com.example.lihui20.testhttp.model.Data;
import com.example.lihui20.testhttp.tools.ToastUtils;
import com.example.lihui20.testhttp.tools.Utils;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends Activity {
    private static Handler mHandler;
    PullToRefreshListView favorite_listview;
    ListView listview;
    TextView empty;
    NewsAdapter newsAdapter;
    List<Data> queryAllList;
    List<Data> less4list = new ArrayList();
    Context mContext;
    List<Data> more4list;
    List currentlist = null;
    List listBeforeQuery = new ArrayList();
    List<Data> keyList;
    LinearLayout favoritell;
    boolean isRefreshing = false;
    private int pageNumber;
    private android.app.ActionBar mActionBar;
    private SearchView searchView;
    private MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        mActionBar = getActionBar();
        mContext = this;

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d("lihui", "FavoriteActivity handleMessage()");
                switch (msg.what) {
                    case 0:
                        ToastUtils.showToast("未收藏过新闻哦亲...");

                        break;
                    case 1:
                        ToastUtils.showToast("没有更多的收藏了亲...");

                        break;
                    case 2:
                        currentlist = (List) msg.obj;
                        List list = DBUtils.getInstance(mContext).queryAll();
                        Log.d("lihui", "queryAllList.toString()---" + queryAllList.toString());
                        if (currentlist != null && (currentlist.size() == list.size())) {
                            ToastUtils.showToast("已加载所有收藏的新闻...");
                        } else {
                            ToastUtils.showToast("已添加一条收藏的新闻...");
                        }
                        break;

                    default:
                        break;
                }
                isRefreshing = false;
                favorite_listview.onRefreshComplete();
                favorite_listview.setMode(PullToRefreshBase.Mode.BOTH);

            }
        };
        initView();
    }

    private void initView() {
        favorite_listview = (PullToRefreshListView) findViewById(R.id.favorite_listView);
        favorite_listview.setScrollingWhileRefreshingEnabled(true);
        //刷新分页
        favorite_listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview = favorite_listview.getRefreshableView();
        listview.setVerticalScrollBarEnabled(false);
        favoritell = (LinearLayout) findViewById(R.id.favoritell);

        favorite_listview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("FavoriteActivity", "115 initView onClick()");
                if (!mActionBar.isShowing()) {
                    mActionBar.show();
                }
            }
        });


        //头部
        ILoadingLayout topLoading = favorite_listview.getLoadingLayoutProxy(true,
                false);
        topLoading.setPullLabel("下拉加载更多...");
        topLoading.setRefreshingLabel("玩命加载中...");
        //底部
        ILoadingLayout endLoading = favorite_listview.getLoadingLayoutProxy(false,
                true);
        endLoading.setPullLabel("上拉加载更多...");
        endLoading.setRefreshingLabel("玩命加载中...");
        favorite_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (!mActionBar.isShowing()) {
                    mActionBar.show();
                }
                //截止上拉
                favorite_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                doRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                favorite_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

                doRefresh();
            }
        });

        //
        empty = (TextView) findViewById(R.id.empty_favorite);
        if (DBUtils.getInstance(mContext).queryAll().size() == 0) {
            //没有收藏过，禁止刷新
            favorite_listview.setMode(PullToRefreshBase.Mode.DISABLED);
        }
        //
        com.example.lihui20.testhttp.animation.AnimationUtils.alphaAnimation(empty);
        //
        queryAllList = DBUtils.getInstance(mContext).queryAll();//首次加载获取所有收藏的条数
        Log.d("FavoriteActivity", "DBUtils.getInstance(this).queryAll()---" + queryAllList);
        pageNumber = queryAllList.size();//初始化分界线：4
        Log.d("Favotite", "pageNumber---" + pageNumber);
        final CustomOnclick onclick = new CustomOnclick() {
            @Override
            public void onMyItemClick(Data data) {
                if (DBUtils.getInstance(mContext).queryAll().size() == 0) {
                    empty.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                    //刷新分页
                    favorite_listview.setMode(PullToRefreshBase.Mode.DISABLED);
                }
                //更新
                if (pageNumber > 4) {
                    more4list.remove(data);
                } else {
                    less4list.remove(data);
                }
                newsAdapter.notifyDataSetChanged();
            }
        };
        if (queryAllList != null && queryAllList.size() > 0) {

            empty.setVisibility(View.GONE);
            //初始化加载4条
            if (pageNumber > 4) {
                more4list = queryAllList.subList(0, 4);
                Log.d("Favotite", "list.size---" + queryAllList.size());

                Log.d("Favotite", "list.subList---" + queryAllList);

                newsAdapter = new NewsAdapter(this, more4list, onclick);
            } else {
                Utils.resetList(less4list, queryAllList);//<=4
                newsAdapter = new NewsAdapter(this, less4list, onclick);
            }
            listview.setAdapter(newsAdapter);

        }
        Log.d("FavoriteActivity", "FavoriteActivity---listview---" + listview.getCount());
    }


    private void doRefresh() {
        Log.d("FavoriteActivity", "FavoriteActivity doRefresh---listview---" + listview.getCount());
        if (listview.getCount() > 0) {
            for (int i = 0; i < listview.getCount(); i++) {
                Log.d("FavoriteActivity", "FavoriteActivity---listview---" + listview.getChildAt(i).toString());
            }
        }
        if (!isRefreshing) {

            isRefreshing = true;
        } else {
            return;
        }
        //刷新中
        //没有收藏
        List favotiteList = DBUtils.getInstance(mContext).queryAll();
        if (favotiteList.size() == 0) {
            favorite_listview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Message msg = mHandler.obtainMessage();
                    msg.what = 0;
                    msg.obj = queryAllList;
                    mHandler.sendMessage(msg);
                }
            }, 3000);
            return;
        }
        //大于4条时候，刷新才会有效果,每次加载1条
        if (pageNumber > 4) {
            refresh();//more4list
        } else {
            //<=4,首次初始化时候已经加载完了
            favorite_listview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    msg.obj = queryAllList;
                    mHandler.sendMessage(msg);
                }
            }, 3000);
        }
    }

    //news > 4每次刷新加载1条记录
    private void refresh() {
        favorite_listview.postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新之后，多添加1个
                currentlist = DBUtils.getInstance(mContext).queryPage(0, more4list.size() + 1);
                Log.d("list", "list---" + currentlist);
                Utils.resetListUpdate(more4list, currentlist, newsAdapter);//修改list值

                Message msg = mHandler.obtainMessage();
                msg.what = 2;
                msg.obj = currentlist;
                mHandler.sendMessage(msg);
            }
        }, 3000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        // 获得menu中指定的菜单项
        searchItem = menu.findItem(R.id.search);
        // 获得菜单项中的SearchView
        searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            try {        //--拿到字节码
                Class<?> searchViewClass = searchView.getClass();
                //指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field searchPlateField = searchViewClass.getDeclaredField("mSearchPlate");
                //暴力反射,只有暴力反射才能拿到私有属性
                searchPlateField.setAccessible(true);
                View view = (View) searchPlateField.get(searchView);
                //设置背景
                view.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (searchView == null) {
            ToastUtils.showToast("searchView is null");
            return true;
        }
        // 当SearchView获得焦点时弹出软键盘的类型，就是设置输入类型
        searchView.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        // 设置回车键表示查询操作
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d("FavoriteActivity", "355onClose");
                Log.d("FavoriteActivity", "onMenuItemActionCollapse");
                Log.d("FavoriteActivity", "more4list---" + more4list);
                Log.d("FavoriteActivity", "less4list---" + less4list);
                Log.d("FavoriteActivity", "listBeforeQuery---" + listBeforeQuery);
                favorite_listview.setMode(PullToRefreshBase.Mode.BOTH);
                //关闭搜索，还原到原来的列表数据
                if (listBeforeQuery != null && listBeforeQuery.size() > 0) {
                    Log.d("FavoriteActivity", "onMenuItemActionCollapse list.size()---" + queryAllList.size());
                    Log.d("FavoriteActivity", "onMenuItemActionCollapse list---" + queryAllList);

                    if (pageNumber > 4) {
                        Utils.resetListUpdate(more4list, listBeforeQuery, newsAdapter);
                        Log.d("FavoriteActivity", "270 more4list---" + more4list);
                    } else {
                        Utils.resetListUpdate(less4list, listBeforeQuery, newsAdapter);
                        Log.d("FavoriteActivity", "274 less4list---" + less4list);
                    }
                }
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("FavoriteActivity", "query---" + query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText) || isRefreshing) {
                    return true;
                }
                if (listBeforeQuery != null && listBeforeQuery.size() > 0) {
                    if (pageNumber > 4) {
                        Utils.resetList(more4list, listBeforeQuery);
                    } else {
                        Utils.resetList(less4list, listBeforeQuery);
                    }
                }
                Log.d("FavoriteActivity", "newText---" + newText);
                //禁止刷新
                favorite_listview.setMode(PullToRefreshBase.Mode.DISABLED);
                keyList = DBUtils.getInstance(mContext).queryBykey(newText);

                Log.d("FavoriteActivity", "236 keyList---" + keyList);
                Log.d("FavoriteActivity", "237 list.size()---" + queryAllList.size());
                Log.d("FavoriteActivity", "237 list---" + queryAllList);

                Log.d("FavoriteActivity", "238 pageNumber---" + pageNumber);


                if (pageNumber > 4) {
                    Utils.resetList(listBeforeQuery, more4list);
                    Log.d("FavoriteActivity", "221 listBeforeQuery---" + listBeforeQuery);

                    //当前显示的列表中查询是否有包含关键字的 item
                    Utils.resetListUpdate(more4list, keyList, newsAdapter);

                } else {
                    Utils.resetList(listBeforeQuery, less4list);
                    Log.d("FavoriteActivity", "226 listBeforeQuery---" + listBeforeQuery);
                    Utils.resetListUpdate(less4list, keyList, newsAdapter);
                }
                return true;
            }
        });
        //hideMenu
        MenuItem hideMenu = menu.findItem(R.id.action_hide);
        hideMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mActionBar.hide();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(getCurrentViewbgColor())) {
            return;
        }
        updateBgColor(getCurrentViewbgColor());
    }

    private void updateBgColor(String viewbgcolor_key) {
        switch (viewbgcolor_key) {
            case "brown":
                favoritell.setBackgroundResource(R.color.setbrown);
                break;
            case "black":
                favoritell.setBackgroundResource(android.R.color.black);
                break;
            case "gary":
                favoritell.setBackgroundResource(R.color.gary);
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
    }

}

