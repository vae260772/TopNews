package com.example.lihui20.testhttp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lihui20.testhttp.R;
import com.example.lihui20.testhttp.interface2.CustomOnclick;
import com.example.lihui20.testhttp.model.Data;
import com.example.lihui20.testhttp.tools.ImageUtils;
import com.example.lihui20.testhttp.tools.Utils;

import java.util.List;

/**
 * Created by lihui20 on 2016/12/15.
 */
public class CustomGridViewAdapter extends BaseAdapter {
    List list;
    Context context;

    public CustomGridViewAdapter(Context context, List list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
            viewholder = new ViewHolder();
            viewholder.list_item = (LinearLayout) convertView.findViewById(R.id.list_item);
            viewholder.title = (TextView) convertView.findViewById(R.id.title);
            viewholder.date = (TextView) convertView.findViewById(R.id.date);
            viewholder.author_name = (TextView) convertView.findViewById(R.id.author_name);
            viewholder.thumbnail_pic_s = (ImageView) convertView.findViewById(R.id.thumbnail_pic_s);//放图片
            convertView.setTag(viewholder);
        }
        viewholder = (ViewHolder) convertView.getTag();
        Data data = (Data) list.get(position);
        viewholder.title.setText(data.getTitle());
        viewholder.date.setText(data.getDate());
        viewholder.author_name.setText(data.getAuthor_name());
        ImageUtils.downloadImageFromURL(viewholder.thumbnail_pic_s, data.getThumbnail_pic_s());
        viewholder.list_item.setOnLongClickListener(new CustomListener(data));

        return convertView;
    }

    class CustomListener implements View.OnLongClickListener {
        Data data;

        public CustomListener(Data data) {
            this.data = data;
        }

        @Override
        public boolean onLongClick(View v) {
            Utils.showOperateDialog(context, data, null);
            return true;
        }
    }

    public static class ViewHolder {
        public LinearLayout list_item;
        public TextView title;
        public TextView date;
        public TextView author_name, share;
        public ImageView thumbnail_pic_s,url;

    }

}
