package com.example.lihui20.testhttp.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.lihui20.testhttp.adapter.MusicAdapter;
import com.example.lihui20.testhttp.interface2.CustomOnclick;
import com.example.lihui20.testhttp.model.Music;
import com.example.lihui20.testhttp.service.PlayerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lihui20 on 2017/3/30.
 */

public class MediaUtils {
    private static Music playingMusic;
    private static Music pauseMusic;

    public static void playEnd() {
        MediaUtils.setPlayState(false);//单曲播放，播完了就结束
        MediaUtils.setPlaying(null);
        MediaUtils.setPauseMusic(null);
        MediaUtils.setPausePosition(0);
    }

    public static void playAuto(Music music) {
        MediaUtils.setPlayState(true);
        MediaUtils.setPlaying(music);
        MediaUtils.setPauseMusic(null);
        MediaUtils.setPausePosition(0);
    }

    public static int getPlayType() {
        return playType;
    }

    public static void setPlayType(int playType) {
        MediaUtils.playType = playType;
    }

    public static int playType = 0;

//    public static Music getSelectMusic() {
//        return selectMusic;
//    }
//
//    public static void setSelectMusic(Music selectMusic) {
//        MediaUtils.selectMusic = selectMusic;
//    }

    //  private static Music selectMusic = null;

    private static int pausePosition;

    public static int getListPosition() {
        return listPosition;
    }

    public static void setListPosition(int listPosition) {
        MediaUtils.listPosition = listPosition;
    }

    private static int listPosition = -1000;
    private static boolean play = false;

    public static boolean getPlayState() {
        return play;
    }

    public static void setPlayState(boolean play) {
        MediaUtils.play = play;

    }

    public static Music getPauseMusic() {
        return pauseMusic;
    }

    public static void setPauseMusic(Music music) {
        pauseMusic = music;
    }

    public static int getPausePosition() {
        return pausePosition;
    }

    public static void setPausePosition(int pausePosition) {
        MediaUtils.pausePosition = pausePosition;
    }

    public static void setPlaying(Music music) {
        playingMusic = music;
    }

    public static Music getPlaying() {
        return playingMusic;
    }

    public static void pause(Music data, Context context) {
        Intent intent = new Intent();
        // intent.putExtra("position", (int) position);
        intent.putExtra("url", data.getDATA());
        intent.putExtra("MSG", Utils.PAUSE_MSG);
        intent.setClass(context, PlayerService.class);
        context.startService(intent);        //启动服务
    }

    //
    public static void continuePlay(Music data, Context context, int position) {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.putExtra("url", data.getDATA());
        intent.putExtra("MSG", Utils.PLAY_MSG);
        intent.setClass(context, PlayerService.class);
        context.startService(intent);        //启动服务
    }

    //切换歌曲播放
    public static void playOtherMusic(Utils.PlayMusicInter playMusicInter, Music data, Context context, int position) {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.putExtra("music", data);

        intent.putExtra("url", data.getDATA());
        intent.putExtra("MSG", Utils.PLAY_MSG);
        //   intent.putExtra("adapter", musicAdapter);
        //  intent.putExtra("playMusicInter", playMusicInter);

        intent.setClass(context, PlayerService.class);
        context.startService(intent);        //启动服务
    }


    //弹窗
    public static void showPlayTypeDialog(final Context context, final MusicAdapter musicAdapter, final Music data, final CustomOnclick onclick, final Utils.PlayMusicInter playMusicInter) {
        Log.d("129type", "getPlayType()---" + playType);
        String[] playTypeItems = {"单曲播放", "顺序播放", "随机播放", "单曲循环", "列表循环"};
        switch (MediaUtils.getPlayType()) {
            case 0://设置为 单曲播放
                playTypeItems[0] = "当前为：单曲播放";
                break;
            case 1:
//顺序播放
                playTypeItems[1] = "当前为：顺序播放";
                break;
            case 2:
//随机播放
                playTypeItems[2] = "当前为：随机播放";
                break;
            case 3:
                //单曲循环
                playTypeItems[3] = "当前为：单曲循环";
                break;
            case 4:
                //循环播放
                playTypeItems[4] = "当前为：列表循环";
                break;

        }
        final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < playTypeItems.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("title", playTypeItems[i]);
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
                Log.d("195onItemClick", "i---" + i);
                switch (i) {
                    case 0://设置为 顺序播放
                        MediaUtils.setPlayType(0);
                        break;
                    case 1:
//随机播放
                        MediaUtils.setPlayType(1);
                        break;
                    case 2:
//单曲播放
                        MediaUtils.setPlayType(2);
                        break;
                    case 3:
                        //单曲循环
                        MediaUtils.setPlayType(3);
                        break;
                    case 4:
                        //循环播放
                        MediaUtils.setPlayType(4);
                        break;
                }
                dialog.cancel();

            }
        });
    }

}
