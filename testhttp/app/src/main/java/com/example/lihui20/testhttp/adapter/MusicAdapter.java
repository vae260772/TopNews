package com.example.lihui20.testhttp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lihui20.testhttp.R;
import com.example.lihui20.testhttp.animation.AnimationUtils;
import com.example.lihui20.testhttp.interface2.CustomOnclick;
import com.example.lihui20.testhttp.model.Data;
import com.example.lihui20.testhttp.model.Music;
import com.example.lihui20.testhttp.tools.MediaUtils;
import com.example.lihui20.testhttp.tools.Utils;

import java.util.List;

/**
 * Created by lihui20 on 2016/12/15.
 */
public class MusicAdapter extends BaseAdapter {
    List<Music> list;
    Context context;
    String regEx = "[.]*\\s-\\s[.]*";//xxx - xxx
    private Music selectMusic = null;// 选中的位置

    public MusicAdapter(Context context, List list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item3, parent, false);
            viewholder = new ViewHolder();
            viewholder.ll = (LinearLayout) convertView.findViewById(R.id.list_item);
            viewholder.marquee = (TextView) convertView.findViewById(R.id.playing);
            viewholder.title = (TextView) convertView.findViewById(R.id.title);
            viewholder.album = (TextView) convertView.findViewById(R.id.album);
            viewholder.singer = (TextView) convertView.findViewById(R.id.singer);
            viewholder.time = (TextView) convertView.findViewById(R.id.time);
            viewholder.image = (ImageView) convertView.findViewById(R.id.image);//放图片
            convertView.setTag(viewholder);
        }
        viewholder = (ViewHolder) convertView.getTag();
        Music data = list.get(position);
        String[] title = null;
        Log.d("100res", "selectMusic---" + selectMusic + ",data---" + data);
        boolean b = selectMusic != null && data.equals(selectMusic);
        Log.d("100res", "b---" + b);
        // if (data.equals(MediaUtils.getPlaying())) {
//选中
        if (position == MediaUtils.getListPosition()) {
            if (MediaUtils.getPlayState()) {
                viewholder.marquee.setText("正在播放中...");
                //加个动画
            } else {
                viewholder.marquee.setText("暂停中...");
            }
            AnimationUtils.alphaAnimation(viewholder.marquee);
            //  viewholder.marquee.setSelected(true);
            viewholder.marquee.setVisibility(View.VISIBLE);
            viewholder.ll.setBackgroundResource(R.drawable.select_roundangle);
        } else {
            // viewholder.marquee.setSelected(false);
            viewholder.marquee.setVisibility(View.GONE);
            viewholder.ll.setBackgroundResource(R.drawable.roundangle);
        }
        viewholder.title.setText("歌曲：" + data.getTITLE());
        viewholder.album.setText("来自专辑：" + data.getALBUM());
        viewholder.singer.setText("歌手：" + data.getARTIST());
        Log.d("131time", "data.getDURATION()---" + data.getDURATION());
        viewholder.time.setText("时长：" + data.getDURATION());
        return convertView;
    }

    public static class ViewHolder {
        public LinearLayout ll;

        public TextView title, marquee;
        public TextView album;
        public TextView singer,
                time;
        public ImageView image;

    }

    //
    public class CustomListener implements View.OnLongClickListener {
        Data data;
        CustomOnclick onclick;
        public CustomListener(Data data, CustomOnclick onclick) {
            this.data = data;
            this.onclick = onclick;
        }

        @Override
        public boolean onLongClick(View v) {
            Utils.showOperateDialog(context, data, onclick);
            return false;
        }
    }

}
