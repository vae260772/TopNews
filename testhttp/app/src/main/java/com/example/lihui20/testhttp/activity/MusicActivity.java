package com.example.lihui20.testhttp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lihui20.testhttp.R;
import com.example.lihui20.testhttp.adapter.MusicAdapter;
import com.example.lihui20.testhttp.model.Music;
import com.example.lihui20.testhttp.tools.MediaUtils;
import com.example.lihui20.testhttp.tools.ToastUtils;
import com.example.lihui20.testhttp.tools.Utils;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.lihui20.testhttp.tools.Utils.DeleteMusicInter;
import static com.example.lihui20.testhttp.tools.Utils.DeleteMusicSucessInter;
import static com.example.lihui20.testhttp.tools.Utils.INotifyUpdate;
import static com.example.lihui20.testhttp.tools.Utils.getAllMusic;
import static com.example.lihui20.testhttp.tools.Utils.setNotify;
import static com.example.lihui20.testhttp.tools.Utils.showMusicDialog;

public class MusicActivity extends Activity {
    PullToRefreshListView music_listview;
    LinearLayout mll;
    ListView listview;
    TextView empty;
    Context mContext;
    private static Handler mHandler;
    boolean isRefreshing = false;
    MusicAdapter musicAdapter;
    List<Music> musicList = new ArrayList<>();
    INotifyUpdate update;
    Cursor cursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        mContext = this;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d("lihui", "MusicActivity handleMessage()");
                switch (msg.what) {
                    case 0:
                        List<Music> musicList = (List<Music>) msg.obj;
                        Log.d("musicList", "65musicList---" + musicList);
                        musicAdapter.notifyDataSetChanged();
                        empty.setVisibility(GONE);
                        ToastUtils.showToast("已加载所有音乐文件...");
                        updateLabel(thistopLoading, thisendLoading);
                        break;
                    case 1:
                        empty.setVisibility(VISIBLE);
                        ToastUtils.showToast("未查找到音乐文件...");
                        updateLabel(thistopLoading, thisendLoading);
                        break;
                    case 2:
                        break;

                    default:
                        break;
                }
                isRefreshing = false;
                music_listview.onRefreshComplete();
                music_listview.setMode(PullToRefreshBase.Mode.BOTH);

            }
        };
        update = new INotifyUpdate() {
            @Override
            public void update(boolean update) {
                if (update) {
                    musicAdapter.notifyDataSetInvalidated();
                }
            }
        };
        setNotify(update);

        initView();

    }

    private void initView() {
        mll = (LinearLayout) findViewById(R.id.mll);
        music_listview = (PullToRefreshListView) findViewById(R.id.music_listView);
        music_listview.getLoadingLayoutProxy();
        music_listview.setScrollingWhileRefreshingEnabled(true);
        listview = music_listview.getRefreshableView();
        listview.setVerticalScrollBarEnabled(false);
        empty = (TextView) findViewById(R.id.empty_music);

        com.example.lihui20.testhttp.animation.AnimationUtils.alphaAnimation(empty);
        //加载系统音乐
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor.getCount() > 0) {
            empty.setVisibility(GONE);

        /*
        Context context,
        List<? extends Map<String, ?>> data,
        int resource,
        String[] from,
        int[] to
         */
            musicList = getAllMusic(cursor, new ArrayList<Music>());

        }
        musicAdapter = new MusicAdapter(mContext, musicList);
        Log.d("121test", "musicList---" + musicList.size());
        Log.d("121test", "musicAdapter---" + musicAdapter.getCount());

        //musicAdapter
        listview.setAdapter(musicAdapter);


        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int i, final long l) {
                view.setBackgroundColor(android.R.color.holo_red_dark);
                final Utils.PlayMusicInter musicInter = new Utils.PlayMusicInter() {
                    @Override
                    public void setPlay(Music data, boolean play, boolean autoupdate, int position) {
                        Log.d("music", "data---" + data);
                        Log.d("music", "play---" + play);

                        Log.d("141position", "i---" + i);//3
                        Log.d("141position", "l---" + l);//2
                        if (autoupdate) {
                            listview.setSelection(position + 1);
                            MediaUtils.setListPosition(position);
                        } else {
                            listview.setSelection(i);
                            MediaUtils.setListPosition(i - 1);
                        }
                        MediaUtils.setPlayState(play);
                        //
                        musicAdapter.notifyDataSetInvalidated();
                    }
                };

                Utils.DeleteMusicInter deleteMusicInter = new DeleteMusicInter() {
                    @Override
                    public void delete(Music music) {
                        musicList.remove(music);
                        musicAdapter.notifyDataSetChanged();
                        if (MediaUtils.getPlaying() != null) {
                            MediaUtils.setListPosition(Utils.getPositionFromList(musicList, MediaUtils.getPlaying()));
                        }
                    }
                };
                Utils.setDeleteMusicInter(deleteMusicInter);
                Utils.setPlayMusicInter(musicInter);
                showMusicDialog(mContext, musicAdapter, musicList.get((int) l), null, musicInter);
                //
                return true;
            }
        });


        //刷新
        music_listview.setMode(PullToRefreshBase.Mode.BOTH);
        //头部
        final ILoadingLayout topLoading = music_listview.getLoadingLayoutProxy(true,
                false);
        thistopLoading = topLoading;
        //底部
        final ILoadingLayout endLoading = music_listview.getLoadingLayoutProxy(false,
                true);
        thisendLoading = endLoading;

        topLoading.setRefreshingLabel("玩命加载中...");
        endLoading.setRefreshingLabel("玩命加载中...");
        topLoading.setReleaseLabel("放开以刷新...");
        endLoading.setReleaseLabel("放开以刷新...");
        updateLabel(topLoading, endLoading);
//监听
        DeleteMusicSucessInter inter = new DeleteMusicSucessInter() {
            @Override
            public void deleteSucess() {
                updateLabel(topLoading, endLoading);
            }
        };
        Utils.setDeleteMusicSucessInter(inter);

        music_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                music_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                doRefresh();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                music_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                doRefresh();
            }
        });

        //
        empty = (TextView) findViewById(R.id.empty_music);
    }

    private static ILoadingLayout thistopLoading, thisendLoading;

    private void updateLabel(ILoadingLayout topLoading, ILoadingLayout endLoading) {
        if (Utils.mlist != null && Utils.mlist.size() > 0) {
            topLoading.setPullLabel("当前共有" + Utils.mlist.size() + "首歌曲" + "...");
            endLoading.setPullLabel("当前共有" + Utils.mlist.size() + "首歌曲" + "...");
        } else {
            topLoading.setPullLabel("下拉加载更多音乐...");
            endLoading.setPullLabel("上拉加载更多音乐...");
        }
    }


    private void doRefresh() {
        if (!isRefreshing) {
            isRefreshing = true;//刷新中。。。
        } else {
            return;
        }
        //重新查询
        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor.getCount() <= 0) {
            empty.setVisibility(VISIBLE);
        } else {
            empty.setVisibility(GONE);
        }
        //重新查询一下

        refresh(musicList, cursor);
    }

    private void refresh(final List<Music> musicList, final Cursor cursor) {
        music_listview.postDelayed(new Runnable() {
            @Override
            public void run() {
                musicList.clear();
                getAllMusic(cursor, musicList);
                Message msg = mHandler.obtainMessage();
                if (musicList.size() > 0 && musicList != null) {
                    msg.what = 0;
                } else {
                    msg.what = 1;
                }
                msg.obj = musicList;
                mHandler.sendMessage(msg);
            }
        }, 3000);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int pos = MediaUtils.getListPosition();
        boolean state = MediaUtils.getPlayState();
        updateBgColor(getCurrentViewbgColor());

        if (pos == -1000 && state == false) {//首次打开
            return;
        }

        musicAdapter.notifyDataSetInvalidated();
        listview.setSelection(pos + 1);
    }

    private void updateBgColor(String viewbgcolor_key) {
        switch (viewbgcolor_key) {
            case "brown":
                mll.setBackgroundResource(R.color.setbrown);
                break;
            case "black":
                mll.setBackgroundResource(android.R.color.black);
                break;
            case "gary":
                mll.setBackgroundResource(R.color.gary);
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

