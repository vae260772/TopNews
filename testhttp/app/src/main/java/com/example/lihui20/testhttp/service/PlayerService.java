package com.example.lihui20.testhttp.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.example.lihui20.testhttp.model.Music;
import com.example.lihui20.testhttp.tools.MediaUtils;
import com.example.lihui20.testhttp.tools.ToastUtils;
import com.example.lihui20.testhttp.tools.Utils;

import java.util.List;
import java.util.Random;

/**
 * Created by lihui20 on 2016/12/6.
 */
public class PlayerService extends Service {
    private MediaPlayer mediaPlayer = new MediaPlayer();        //媒体播放器对象
    private String path;                        //音乐文件路径
    private boolean isPause;                    //暂停状态
    String TAG = "PlayerService";
    int lastPlayingPos1 = -1000;
    int lastPlayingPos2 = -1001;
    private Music music;
    //private MusicAdapter musicAdapter;
    //ListView listview;
    //Utils.PlayMusicInter playMusicInter;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        music = (Music) intent.getSerializableExtra("music");
        //  listview = (ListView) intent.getSerializableExtra("listview");
        // musicAdapter = (MusicAdapter) intent.getSerializableExtra("adapter");
        //  playMusicInter = (Utils.PlayMusicInter) intent.getSerializableExtra("playMusicInter");
        //  Log.d("music", "onStartCommand playMusicInter---" + playMusicInter);

        Log.d("music", "onStartCommand music---" + music);
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        //播放歌曲的下标
        int position = intent.getIntExtra("position", 0);
        // path = intent.getStringExtra("url");
        int msg = intent.getIntExtra("MSG", 0);
        if (msg == Utils.PLAY_MSG) {
            play(position, music);
        } else if (msg == Utils.PAUSE_MSG) {
            pause();
            MediaUtils.setPausePosition(mediaPlayer.getCurrentPosition());
            Log.d("music", "当前歌曲暂停位置---" + mediaPlayer.getCurrentPosition());
        } else if (msg == Utils.STOP_MSG) {
            stop();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 播放音乐
     *
     * @param position
     */
    private void play(int position, final Music music) {


        try {
            mediaPlayer.reset();//把各项参数恢复到初始状态
            mediaPlayer.setDataSource(music.getDATA());
            mediaPlayer.prepare();    //进行缓冲
            mediaPlayer.setOnPreparedListener(new PreparedListener(position));//注册一个监听器
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    switch (MediaUtils.getPlayType()) {
                        case 0://单曲播放
                            //发广播
                            Intent intent = new Intent("com.example.lihui20.testhttp.MusicBroadcastReceiver");
                            intent.putExtra("state", true);//播放完发广播
                            sendBroadcast(intent);//发广播
                            break;
                        case 1://顺序播放
                            //播放当前歌曲的下一首歌曲
                            playRecycle(music, false);
                            break;
                        case 2://随机播放
                            List list = Utils.mlist;
                            if (list != null && list.size() > 0) {
                                int randompos = new Random().nextInt(list.size());//0- list.size()
                                Music music2 = (Music) list.get(randompos);
                                playAnotherMusic(music2, randompos);
                            }
                            break;
                        case 3://单曲循环
                            play(0, music);
                            break;
                        case 4://循环播放
                            playRecycle(music, true);
                            break;
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playRecycle(Music music, boolean recycle) {
        //当前播放歌曲的下一首
        Music music1 = Utils.getNextPositionFromList(Utils.mlist, music);
        Log.d("138music1", "music1---" + music1);
        int pos = Utils.getNextPosition(Utils.mlist, music);
        Log.d("140music1", "pos---" + pos);
        //播放完最后一首歌曲
        if (music1 == null) {
            if (recycle) {
                //从头开始播放
                if (Utils.mlist != null && Utils.mlist.size() > 0) {
                    playAnotherMusic(Utils.mlist.get(0), 0);
                }
            } else {
                Intent endintent = new Intent("com.example.lihui20.testhttp.MusicBroadcastReceiver");
                endintent.putExtra("state", true);//播放完发广播
                sendBroadcast(endintent);//发广播
                ToastUtils.showToast( "已播放至最后一首歌...");
                return;
            }
            return;
        }
        Log.d("music", "播放当前歌曲的下一首歌曲");
        Log.d("music", "music1---" + music1);

        playAnotherMusic(music1, pos);
    }

    private void playAnotherMusic(Music music1, int pos) {
        MediaUtils.playAuto(music1);
        Log.d("music", "播放当前歌曲的下一首歌曲");
        Log.d("music", "music1---" + music1);
        play(0, music1);
        if (Utils.getPlayMusicInter() != null) {
            Utils.getPlayMusicInter().setPlay(music1, true, true, pos);
        }

    }


    //
    private void play(int position, int playType) {

        try {
            mediaPlayer.reset();//把各项参数恢复到初始状态
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();    //进行缓冲
            mediaPlayer.setOnPreparedListener(new PreparedListener(position));//注册一个监听器
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //发广播
                    Intent intent = new Intent("com.example.lihui20.testhttp.MusicBroadcastReceiver");
                    intent.putExtra("state", true);//播放完发广播
                    sendBroadcast(intent);//发广播
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 暂停音乐
     */
    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
        }
    }

    /**
     * 停止音乐
     */
    private void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    /**
     * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
     */
    private final class PreparedListener implements MediaPlayer.OnPreparedListener {
        private int positon;

        public PreparedListener(int positon) {
            this.positon = positon;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();    //开始播放
            if (positon > 0) {    //如果音乐不是从头播放
                mediaPlayer.seekTo(positon);
            }
        }
    }

}
