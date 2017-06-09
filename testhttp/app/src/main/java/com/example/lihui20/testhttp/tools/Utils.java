package com.example.lihui20.testhttp.tools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.lihui20.testhttp.R;
import com.example.lihui20.testhttp.activity.WebViewActivity;
import com.example.lihui20.testhttp.adapter.MusicAdapter;
import com.example.lihui20.testhttp.adapter.NewsAdapter;
import com.example.lihui20.testhttp.database.DBUtils;
import com.example.lihui20.testhttp.interface2.CustomOnclick;
import com.example.lihui20.testhttp.model.Data;
import com.example.lihui20.testhttp.model.Music;
import com.example.lihui20.testhttp.service.HttpService;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Converter;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by lihui20 on 2016/12/20.
 */
public class Utils implements Serializable {
    public static String[] TYPEARRAY = {"top", "shehui", "guonei", "guoji", "yule", "tiyu", "junshi"
            , "keji", "caijing", "shishang"};
    public static String[] TYPEARRAY2 = {"头条", "社会", "国内", "国际", "娱乐", "体育", "军事"
            , "科技", "财经", "时尚"};
    public static int MAX_PAGE = 10;
    public static int MIN_PAGE = 1;
    public static int PLAY_MSG = 0;
    public static int PAUSE_MSG = 1;
    public static int STOP_MSG = 2;
    public static HashMap<String, List<Data>> cacheMap = new HashMap<>();

    public interface INotifyUpdate {
        void update(boolean update);
    }

    public static INotifyUpdate notify;

    public static int getPosition(List<String> list, String title) {
        if (list != null && list.size() > 0) {
            for (int pos = 0; pos < list.size(); pos++) {
                if (list.get(pos).equals(title)) {
                    Log.d("pos", "getPosition  pos---" + pos);
                    return pos;
                }
            }
        }
        return 0;
    }

    public static void setNotify(INotifyUpdate notify) {
        Utils.notify = notify;
    }

    public static class MusicBroadcastReceiver extends BroadcastReceiver {
        public MusicBroadcastReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            //做一些操作
            if (intent.getAction().equals("com.example.lihui20.testhttp.MusicBroadcastReceiver")) {
                boolean state = intent.getExtras().getBoolean("state", false);
                Log.d("Recevier1", "接收到:" + state);
                //还原
                //MediaUtils.setListPosition(-1000);
//                MediaUtils.setPlayState(false);//单曲播放，播完了就结束
//                //
//                MediaUtils.setPlaying(null);
//                MediaUtils.setPauseMusic(null);
                MediaUtils.playEnd();
                // MediaUtils.setSelectMusic(null);
                //
                notify.update(true);
            }
        }
    }


    public static interface PlayMusicInter {
        public void setPlay(Music music, boolean play, boolean update, int position);
    }

    public static DeleteMusicInter deleteMusicInter;

    public static interface DeleteMusicInter {
        public void delete(Music music);
    }

    public static void setDeleteMusicInter(DeleteMusicInter deleteMusicInter) {
        Utils.deleteMusicInter = deleteMusicInter;
    }

    //
    public static void setDeleteMusicSucessInter(DeleteMusicSucessInter deleteMusicSucessInter) {
        Utils.deleteMusicSucessInter = deleteMusicSucessInter;
    }

    public static DeleteMusicSucessInter deleteMusicSucessInter;

    public static interface DeleteMusicSucessInter {
        void deleteSucess();
    }

    //dialog
    static AlertDialog.Builder builder;

    public static String[] typesArray(List<Fragment> fragments) {
        List<String> typeList = new ArrayList<>();
        for (Fragment fragment : fragments) {
            String type = fragment.getArguments().getString("type");
            typeList.add(type);
        }

        return typeList.toArray(new String[typeList.size()]);
    }

    //private AlertDialog.Builder cancelBuilder;
    public static String getKey(String chinese) {
        int position = 0;
        for (int i = 0; i < TYPEARRAY2.length; i++) {
            if (TYPEARRAY2[i].equals(chinese)) {
                position = i;
                break;
            }
        }
        return TYPEARRAY[position];

    }

    public static String[] getAddableTypes(List<String> titles) {
        List<String> list = Arrays.asList(TYPEARRAY2);
        List<String> addList = new ArrayList(list);
        for (int i = 0; i < titles.size(); i++) {
            if (addList.contains(titles.get(i))) {
                addList.remove(titles.get(i));
                Log.d("Utils", "addList---" + addList.toString());
            }
        }
        return addList.toArray(new String[addList.size()]);
    }

    public static int px2dip(int pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (dipValue * scale + 0.5f);
    }

    static class CustomConverterFactory extends Converter.Factory {
        static CustomConverterFactory factory;

        public Converter<ResponseBody, ?> fromResponseBody(Type type, Annotation[] annotations) {
            Log.d("CustomConverterFactory", "type---" + type);
            return new UserResponseConverter(type);
        }

        public Converter<?, RequestBody> toRequestBody(Type type, Annotation[] annotations) {
            return null;
        }

        public static CustomConverterFactory create() {
            if (factory == null) {
                factory = new CustomConverterFactory();
            }
            return factory;
        }
    }

    public static class UserResponseConverter<T> implements Converter<ResponseBody, T> {
        private Type type;

        public UserResponseConverter(Type type) {
            this.type = type;
        }

        @Override
        public T convert(ResponseBody responseBody) throws IOException {
            List<Data> list = new ArrayList<>();
            //原始数据下手
            String result = responseBody.string();
            //构造1
            try {
                org.json.JSONObject jsonObject = null;
                jsonObject = new org.json.JSONObject(result);
                String reason = jsonObject.getString("reason");
                //  ToastUtils.setToastText(context, reason);
                String result2 = jsonObject.getString("result");
                //构造2
                org.json.JSONObject jsonObject2 = new org.json.JSONObject(result2);
                String stat = jsonObject2.getString("stat");
                String data = jsonObject2.getString("data");
                //构造3
                org.json.JSONArray jsonArray2 = new org.json.JSONArray(data);
                for (int i = 0; i < jsonArray2.length(); i++) {
                    //获取每一个JsonObject对象
                    org.json.JSONObject myjObject = jsonArray2.getJSONObject(i);
                    if (myjObject != null) {
                        Data data1 = new Data(myjObject);
                        Log.d("lihui", "Fragment onResponse getUniquekey---" + data1.getUniquekey());
                        Log.d("lihui", "Fragment onResponse data---" + data1);
                        list.add(data1);
                    }
                }
                Log.d("CustomConverterFactory", "list---" + list);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return (T) list;
        }
    }

    //PullToRefreshRecyclerView
//    public static List<Data> getResult(final Context context, final String type,
//                                       final PullToRefreshRecyclerView pullToRefreshRecyclerView,
//                                       final List<Data> list,
//                                       final TextView empty, final Handler mHandler) {
//        Log.d("lihui", "List<Data> list---" + list);
//        //1
//        Retrofit retrofit = new Retrofit.Builder().
//                baseUrl("http://v.juhe.cn/").
//                addConverterFactory(new CustomConverterFactory()).
//                build();
//        //2
//        HttpService myService = retrofit.create(HttpService.class);
//        //3
//        retrofit.Call<List<Data>> call = myService.getData(type, "9f3097f4cbe47e8abb01ca3b92e49cda");
//        //4
//        call.enqueue(new Callback<List<Data>>() {
//
//            @Override
//            public void onResponse(Response<List<Data>> response, Retrofit retrofit) {
//                Log.d("lihui", "123onResponse");
//                try {
//                    List<Data> dataList = response.body();
//                    Utils.resetList(list, dataList);//交换数据
//                    Log.d("CustomConverterFactory", "  Utils.resetList(list, dataList)---" + list);
//
//                    if (list != null && list.size() > 0 && mHandler != null) {
//                        Message msg = mHandler.obtainMessage();
//                        msg.what = 0;
//                        msg.obj = list;
//                        mHandler.sendMessage(msg);
//                        empty.setVisibility(View.GONE);
//                        cacheMap.put(type, list);
//                    }
//                    Log.d("lihui", "159 list---" + list);
//
//                } catch (Exception e) {
//                    Log.d("lihui", "114e---" + e.getMessage());
//                    pullToRefreshRecyclerView.onRefreshComplete();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.d("lihui", "165t:" + t.getMessage());
//                t.printStackTrace();
//                Message msg = mHandler.obtainMessage();
//                msg.what = 1;
//                msg.obj = type;
//                mHandler.sendMessage(msg);
//            }
//
//        });
//        return list;
//    }

    //PullToRefreshGridView
    public static List<Data> getResult(final Context context, final String type,
                                       final PullToRefreshGridView pullToRefreshGridView,
                                       final List<Data> list,
                                       final TextView empty, final Handler mHandler) {
        //1

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("http://v.juhe.cn/").
                addConverterFactory(CustomConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();

        //2
        HttpService myService = retrofit.create(HttpService.class);
        //3
        //  retrofit.Call<List<Data>> call = myService.getData(type, "9f3097f4cbe47e8abb01ca3b92e49cda");
        //4
//        call.enqueue(new Callback<List<Data>>() {
//
//            @Override
//            public void onResponse(Response<List<Data>> response, Retrofit retrofit) {
//                Log.d("lihui", "123onResponse");
//                Log.d("CustomConverterFactory","currentThread---"+Thread.currentThread().toString());
//                try {
//                    List<Data> dataList = response.body();
//                    Utils.resetList(list, dataList);//交换数据
//                    Log.d("CustomConverterFactory", "  Utils.resetList(list, dataList)---" + list);
//                    if (list != null && list.size() > 0 && mHandler != null) {
//                        Message msg = mHandler.obtainMessage();
//                        msg.what = 0;
//                        msg.obj = list;
//                        mHandler.sendMessage(msg);
//                        empty.setVisibility(View.GONE);
//                        cacheMap.put(type, list);
//                    }
//                    Log.d("lihui", "159 list---" + list);
//
//                } catch (Exception e) {
//                    Log.d("lihui", "114e---" + e.getMessage());
//                    pullToRefreshGridView.onRefreshComplete();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.d("CustomConverterFactory","currentThread---"+Thread.currentThread().toString());
//                Log.d("lihui", "165t:" + t.getMessage());
//                t.printStackTrace();
//                Message msg = mHandler.obtainMessage();
//                msg.what = 1;
//                msg.obj = type;
//                mHandler.sendMessage(msg);
//            }
//
//        });
        //4
        Observable observable = myService.getData(type, "9f3097f4cbe47e8abb01ca3b92e49cda");              //获取Observable对象
        observable.subscribeOn(Schedulers.io())  // 网络请求切换在io线程中调用
                .unsubscribeOn(Schedulers.io())// 取消网络请求放在io线程
                .observeOn(AndroidSchedulers.mainThread())// 观察后放在主线程调用
                .doOnNext(new Action1<List<Data>>() {//1
                    @Override
                    public void call(List<Data> dataList) {
                        //    saveUserInfo(userInfo);//保存用户信息到本地
                        Log.d("CustomConverterFactory", "doOnNext call dataList---" + dataList);
                        Log.d("CustomConverterFactory", "doOnNext call currentThread---" + Thread.currentThread().getName());
                    }
                }).doOnCompleted(new Action0() {
            @Override
            public void call() {
                Log.d("CustomConverterFactory", "doOnCompleted call currentThread---" + Thread.currentThread().getName());
            }
        })
                .subscribe(
                        new Subscriber<List<Data>>() {//subscribe 子线程
                            @Override
                            public void onCompleted() {
                                Log.d("CustomConverterFactory", "onCompleted currentThread---" + Thread.currentThread().getName());
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                Log.d("CustomConverterFactory", "165t:" + e.getMessage());
                                Message msg = mHandler.obtainMessage();
                                msg.what = 1;
                                msg.obj = type;
                                mHandler.sendMessage(msg);
                                //请求失败
                                Log.d("CustomConverterFactory", "onError currentThread---" + Thread.currentThread().getName());
                            }

                            @Override
                            public void onNext(List<Data> dataList) {
                                //请求成功
                                Log.d("CustomConverterFactory", "onNext currentThread---" + Thread.currentThread().getName());
                                Utils.resetList(list, dataList);//交换数据
                                try {
                                    Log.d("CustomConverterFactory", "  Utils.resetList(list, dataList)---" + list);
                                    if (list != null && list.size() > 0 && mHandler != null) {
                                        Message msg = mHandler.obtainMessage();
                                        msg.what = 0;
                                        msg.obj = list;
                                        mHandler.sendMessage(msg);
                                        empty.setVisibility(View.GONE);
                                        cacheMap.put(type, list);
                                    }
                                    Log.d("lihui", "159 list---" + list);

                                } catch (Exception e) {
                                    Log.d("lihui", "114e---" + e.getMessage());
                                    pullToRefreshGridView.onRefreshComplete();
                                }
                            }
                        });


//        call.enqueue(new Callback<List<Data>>() {//retrofit2.xxx
//            @Override
//            public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {
//                Log.d("lihui", "123onResponse");
//                try {
//                    List<Data> dataList = response.body();
//                    Log.d("CustomConverterFactory", "  Utils.resetList(list, dataList)---" + list);
//                    Utils.resetList(list, dataList);//交换数据
//
//                    if (list != null && list.size() > 0 && mHandler != null) {
//                        Message msg = mHandler.obtainMessage();
//                        msg.what = 0;
//                        msg.obj = list;
//                        mHandler.sendMessage(msg);
//                        empty.setVisibility(View.GONE);
//                        cacheMap.put(type, list);
//                    }
//                    Log.d("lihui", "159 list---" + list);
//
//                } catch (Exception e) {
//                    Log.d("lihui", "114e---" + e.getMessage());
//                    pullToRefreshGridView.onRefreshComplete();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Data>> call, Throwable t) {
//                Log.d("lihui", "165t:" + t.getMessage());
//                t.printStackTrace();
//                Message msg = mHandler.obtainMessage();
//                msg.what = 1;
//                msg.obj = type;
//                mHandler.sendMessage(msg);
//            }
//        });
        return list;
    }

//
//    public static void showCancelDialog(final Context context, final Data data, final CustomOnclick onclick) {
//        //  if (cancelBuilder == null) {
//        AlertDialog.Builder cancelBuilder = new AlertDialog.Builder(context).setTitle("提示").setMessage("亲,确认取消收藏这条新闻?");
//        //}
//        cancelBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //从数据库删除
//                Log.d("lihui", "146 data---" + data.toString());
//                DBUtils db = new DBUtils(context);
//                db.delete(data);
//                onclick.onMyItemClick(data);
//            }
//        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        cancelBuilder.show();
//
//    }
//
//    public static void showDialog(final Context context, final Data data) {
//
//
//        if (builder == null) {
//            builder = new AlertDialog.Builder(context).setTitle("提示").setMessage("亲,确认收藏这条新闻?");
//        }
//        builder.setPositiveButton("收藏", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //保存数据库
//                Log.d("lihui", "140 data---" + data.toString());
//                DBUtils db = new DBUtils(context);
//                db.insert(data);
//            }
//        }).setNegativeButton("取消", null);
//
//        builder.show();
//
//    }


    public static void resetList(List resultList, List newList) {

        if (newList != null && newList.size() > 0) {
            if (resultList != null) {
                resultList.clear();
            }
            for (int i = 0; i < newList.size(); i++) {
                resultList.add(newList.get(i));
            }
        }
    }

    public static void resetListUpdate(List oldList, List newList, NewsAdapter newsAdapter) {
        if (oldList != null) {
            oldList.clear();
        }
        if (newList != null) {
            for (int i = 0; i < newList.size(); i++) {
                oldList.add(newList.get(i));
            }
            if (newsAdapter != null) {
                newsAdapter.notifyDataSetChanged();
            }
        }
    }

    public static void resetListUpdate(List oldList, List newList, NewsAdapter newsAdapter, boolean isSearch) {
        if (oldList != null) {
            oldList.clear();
        }
        if (newList != null) {
            for (int i = 0; i < newList.size(); i++) {
                oldList.add(newList.get(i));
            }
            if (newsAdapter != null && !isSearch) {
                newsAdapter.notifyDataSetChanged();
            }
        }
    }

    public static List<Data> getKeyValueList(List currentList, List keyList) {
        List getKeyList = new ArrayList();
        for (int i = 0; i < keyList.size(); i++) {
            Data keyData = (Data) (keyList.get(i));
            for (int j = 0; j < currentList.size(); j++) {
                Data currentData = (Data) (currentList.get(j));
                if (keyData.getTitle().equals(currentData.getTitle())) {
                    getKeyList.add(keyData);
                }
            }
        }
        return getKeyList;

    }

    public static void openURL(Context context, String url) {

        //  Uri uri = Uri.parse(url);
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static String[] titles = {"收藏", "分享", "打开网页"};


    public static void showOperateDialog(final Context context, final Data data, final CustomOnclick onclick) {
        if (onclick != null) {
            titles[0] = "取消收藏";
        } else {
            titles[0] = "收藏";
        }

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < titles.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("title", titles[i]);
            list.add(item);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(context, list,
                android.R.layout.simple_list_item_1, new String[]{"title"}, new int[]{android.R.id.text1});
        //
        LinearLayout ll = new LinearLayout(context);//自定义一个布局文件
        ll.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //
        ListView listView = new ListView(context);//this为获取当前的上下文
        listView.setFadingEdgeLength(0);
        listView.setAdapter(simpleAdapter);
        //

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("选项").setView(listView)//在这里把写好的这个listview的布局加载dialog中
                .create();
        dialog.show();
//
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        if (titles[0].equals("取消收藏") && onclick != null) {
                            //从数据库删除
                            Log.d("lihui", "146 data---" + data.toString());
                            DBUtils db = new DBUtils(context);
                            db.delete(data);
                            onclick.onMyItemClick(data);
                        } else {
                            DBUtils db = new DBUtils(context);
                            db.insert(data);
                        }

                        break;
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                        intent.putExtra(Intent.EXTRA_TEXT,
                                "我正在浏览这条新闻,觉得真不错,推荐给你哦~地址点这里:\n" + data.getUrl());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(Intent.createChooser(intent, "share"));
                        //dialog.cancel();
                        break;
                    case 2:
                        Utils.openURL(context, data.getUrl());
                        //dialog.cancel();
                        break;
                }
                dialog.cancel();

            }
        });
    }

    public static List<Music> mlist;

    //查询系统音乐
    public static List<Music> getAllMusic(Cursor cursor, List<Music> musicList) {
        if (musicList != null && musicList.size() > 0) {
            musicList.clear();
        }
        if (cursor.getCount() == 0) {
            return null;
        }
        while (cursor.moveToNext()) {
                /*
                    String TITLE;// 标题
    String DURATION;// 持续时间
    String ARTIST;// 艺术家
    String _ID;// id
    String DISPLAY_NAME;// 显示名称
    String DATA;// 数据
    String ALBUM_ID;// 专辑名称ID
    String ALBUM;// 专辑
    String SIZE;
    //格式
    String MIME_TYPE;
    String YEAR;
                 */
            String TITLE = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));

            String DURATION = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            String ARTIST = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String _ID = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            String DISPLAY_NAME = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
            String DATA = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            String ALBUM_ID = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

            String ALBUM = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String SIZE = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
            //
            String MIME_TYPE = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
            String YEAR = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));



                /*
                   viewholder.title = (TextView) convertView.findViewById(R.id.title);
            viewholder.album = (TextView) convertView.findViewById(R.id.album);
            viewholder.singer = (TextView) convertView.findViewById(R.id.singer);
            viewholder.time = (TextView) convertView.findViewById(R.id.time);
            viewholder.image = (ImageView) convertView.findViewById(R.id.image);//放图片
                 */
            Music music = new Music(TITLE, DURATION, ARTIST, _ID, DISPLAY_NAME, DATA,
                    ALBUM_ID, ALBUM, SIZE, MIME_TYPE, YEAR);
            Log.d("test", "music---" + music.toString());
            formatMusicItem(music, musicList);
            //   musicList.add(music,musicList);
        }
        mlist = musicList;
        return mlist;
    }

    private static String regEx = "[.]*\\s-\\s[.]*";//xxx - xxx

    private static void formatMusicItem(Music data, List<Music> musicList) {
        String[] title = null;

        if (doRegEx(data.getTITLE())) {//  xxx - xxx
            //分割
            Log.d("title", "data.getTITLE()---" + data.getTITLE());
            title = data.getTITLE().split(regEx);
            Log.d("title", "title---" + title.length);
            if (title != null && title.length == 2) {
                data.setTITLE(title[1]);
            }
        }
        if (data.getALBUM().equals("<unknown>") || data.getALBUM().equals("download")) {
            if (title != null && title.length == 2) {
                data.setALBUM(title[1]);
            } else {
                data.setALBUM(data.getTITLE());
            }
        }
        if (data.getARTIST().equals("<unknown>")) {
            if (title != null && title.length == 2) {
                data.setARTIST(title[0]);
            } else {
                data.setARTIST("未知歌手");
            }
        }
        // >=5s
        formatDuration(data, musicList);
    }

    private static boolean doRegEx(String title) {
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(title);
        boolean rs = matcher.find();
        System.out.println("rs---" + rs);
        return rs;
    }

    private static void formatDuration(Music music, List<Music> musicList) {
        String duration = "";
        if (TextUtils.isEmpty(music.getDURATION())) {//无数据，通过文件获取
            duration = getTimeFromRes(music);
        } else {//有数据
            duration = getMusicTime(music.getDURATION());
        }
        if (!TextUtils.isEmpty(duration)) {
            music.setDURATION(duration);
            musicList.add(music);
        }
    }

//    //匹配时长 05:56 格式
//    private static boolean isFormatDuration(String DURATION) {
//        String regex = "[0-9]{2}:[0-9]{2}";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(DURATION);
//        return matcher.find();
//    }

    private static String getMusicTime(String DURATION) {
        return formatTime(Long.parseLong(DURATION));
    }

    public static String getTimeFromRes(Music music) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        String time;
        try {
            mediaPlayer.setDataSource(music.getDATA());
            mediaPlayer.prepare();
            time = formatTime(Long.valueOf(String.valueOf(mediaPlayer.getDuration())));
        } catch (Exception e) {
            time = formatTime(0);
        }
        return time;
    }

    /**
     * 格式化时间，将毫秒转换为分:秒格式
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        if (time < 5000) {
            return null;
        }
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }

    public static String[] musicitems = {"播放/暂停", "播放模式", "删除音乐", "查看详情"};
    private static PlayMusicInter playMusicInter;

    public static PlayMusicInter getPlayMusicInter() {
        return playMusicInter;
    }

    public static void setPlayMusicInter(PlayMusicInter playMusicInter) {
        Utils.playMusicInter = playMusicInter;
    }


    //弹窗
    public static void showMusicDialog(final Context context, final MusicAdapter musicAdapter, final Music data, final CustomOnclick onclick, final PlayMusicInter playMusicInter) {
        final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < musicitems.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("title", musicitems[i]);
            list.add(item);
        }
        final SimpleAdapter simpleAdapter = new SimpleAdapter(context, list,
                android.R.layout.simple_list_item_1, new String[]{"title"}, new int[]{android.R.id.text1});
        //
        LinearLayout ll = new LinearLayout(context);//自定义一个布局文件
        ll.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //
        ListView listView = new ListView(context);//this为获取当前的上下文
        listView.setFadingEdgeLength(0);
        listView.setAdapter(simpleAdapter);
        //

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("选项").setView(listView)//在这里把写好的这个listview的布局加载dialog中
                .create();
        dialog.show();
        //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        //1.点击同一首歌
                        //判断当前音乐是否播放中...
                        Music currentMusic = MediaUtils.getPlaying();
                        //暂停
                        if (currentMusic != null && currentMusic.equals(data)) {
                            //播放中 暂停
                            MediaUtils.pause(data, context);
                            playMusicInter.setPlay(data, false, false, 0);//回调,修改item状态
                            //设置 暂停的音乐
                            MediaUtils.setPauseMusic(data);
                            MediaUtils.setPlaying(null);
                            Log.d("music", "点击同一首歌 暂停" + data);
                        } else {

                            Music pauseMusic = MediaUtils.getPauseMusic();
                            if (pauseMusic != null && pauseMusic.equals(data)) {
                                //暂停,继续播放
                                playMusicInter.setPlay(data, true, false, 0);//回调,修改item状态
                                //
                                MediaUtils.setPlaying(data);
                                MediaUtils.setPauseMusic(null);

                                //继续播放
                                MediaUtils.continuePlay(data, context, MediaUtils.getPausePosition());
                                Log.d("music", "点击同一首歌 重新播放" + data);

                            } else {
                                //点击的是播放其他歌曲
                                MediaUtils.setPausePosition(0);
                                MediaUtils.setPauseMusic(null);
                                MediaUtils.setPlaying(data);
                                playMusicInter.setPlay(data, true, false, 0);//回调,修改item状态
                                Log.d("music", "点击其他歌曲 播放" + data);
                                MediaUtils.playOtherMusic(playMusicInter, data, context, 0);
                            }
                        }
                        break;
                    case 1:
                        //播放模式
                        //单曲、顺序、循环
                        MediaUtils.showPlayTypeDialog(context, null, null, null, null);
                        break;
                    case 2:
                        //删除音乐,从sdcard删除
                        String path = data.getDATA();
                        File file = new File(path);
                        Log.d("545delete", "musicdata---" + data);
                        Log.d("545delete", "MediaUtils.getPlaying()---" + MediaUtils.getPlaying());

                        if (file != null && file.isFile()) {
                            if ((MediaUtils.getPlaying() != null &&
                                    MediaUtils.getPlaying().getDATA().equals(path)) ||
                                    (MediaUtils.getPauseMusic() != null
                                            && MediaUtils.getPauseMusic().getDATA().equals(path))
                                    ) {
                                ToastUtils.showToast(context, path + "当前文件已被占用...");
                                Log.d("545delete", "当前音乐正在播放中,无法删除");
                                dialog.cancel();
                                return;
                            }
                            if (file.delete()) {
                                MediaScannerConnection.scanFile(context, new String[]{data.getDATA()}, null, null);
                                if (deleteMusicInter != null) {
                                    deleteMusicInter.delete(data);
                                }
                                deleteMusicSucessInter.deleteSucess();
                                //删除成功,更新
                                // mlist.remove(data);
                                // musicAdapter.notifyDataSetChanged();
                                Log.d("545delete", "删除成功---" + data);
                                ToastUtils.showToast(context, path + "删除成功...");


                            } else {
                                ToastUtils.showToast(context, path + "删除失败...");
                            }
                        } else {
                            ToastUtils.showToast(context, path + "文件不存在...");
                            //刷新
                            mlist.remove(data);
                            musicAdapter.notifyDataSetChanged();
                            MediaScannerConnection.scanFile(context, new String[]{data.getDATA()}, null, null);
                        }

                        break;
                    case 3:
                        //查看音乐详情
                        Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View ll = inflater.inflate(R.layout.musicdetails, null);
                        initMusicDetail(ll, data);
                        dialog.setContentView(ll);
                        Window dialogWindow = dialog.getWindow();
                        //动画
                        dialogWindow.setWindowAnimations(R.style.dialog_animation);
                        //去除边距
                        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
                        dialogWindow.setGravity(Gravity.BOTTOM);
                        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                        //屏幕宽度
                        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        //设置参数
                        dialogWindow.setAttributes(lp);
                        dialog.show();
                        break;
                }
                dialog.cancel();

            }
        });
    }

    private static void initMusicDetail(View ll, Music music) {
        ((TextView) ll.findViewById(R.id.title)).setText("歌曲：" + music.getTITLE());
        ((TextView) ll.findViewById(R.id.album)).setText("专辑：" + music.getALBUM());
        ((TextView) ll.findViewById(R.id.singer)).setText("歌手：" + music.getARTIST());
        ((TextView) ll.findViewById(R.id.time)).setText("时长：" + music.getDURATION());
        ((TextView) ll.findViewById(R.id.type)).setText("格式：" + music.getMIME_TYPE());
        ((TextView) ll.findViewById(R.id.size)).setText("大小：" + formetFileSize(music.getSIZE()));
        ((TextView) ll.findViewById(R.id.path)).setText("路径：" + music.getDATA());
    }

    public static String formetFileSize(String size) {
        if (TextUtils.isEmpty(size)) {
            return "未知大小";
        }
        long fileS = Long.parseLong(size);
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    //从arraylist中找到  music的下标
    public static int getPositionFromList(List<Music> musicList, Music music) {
        int pos = -10001;
        for (int i = 0; i < musicList.size(); i++) {
            if (musicList.get(i).getDATA().equals(music.getDATA())) {
                pos = i;
            }
        }
        return pos;
    }

    //从arraylist中找到  music的下标 加1
    public static int getNextPosition(List<Music> musicList, Music music) {
        int lastPos = musicList.size() - 1;//最后一条下标
        int pos = -10001;
        for (int i = 0; i < musicList.size(); i++) {
            if (musicList.get(i).getDATA().equals(music.getDATA())) {
                pos = i;
            }
        }
        if (lastPos == pos) {
            return lastPos;//已经处于最后一个
        }
        return pos + 1;
    }

    //从arraylist中找到  music的下标 加1
    public static Music getNextPositionFromList(List<Music> musicList, Music music) {
        int lastPos = musicList.size() - 1;//最后一条下标
        int pos = -10001;
        Log.d("659music", "music---" + music);

        for (int i = 0; i < musicList.size(); i++) {
            Log.d("660music", "musicList.get(i)---" + musicList.get(i));

            if (musicList.get(i).getDATA().equals(music.getDATA())) {
                pos = i;
                Log.d("662music", "music---" + music);
            }
        }
        if (lastPos == pos) {
            return null;//已经处于最后一个播放结束
        }
        Music music1 = musicList.get(pos + 1);//否则播放下一首
        return music1;
    }
}