package com.example.lihui20.testhttp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lihui20.testhttp.R;
import com.example.lihui20.testhttp.model.Data;
import com.example.lihui20.testhttp.tools.ImageUtils;
import com.example.lihui20.testhttp.tools.Utils;

import java.util.List;

/**
 * Created by lihui20 on 2016/12/20.
 */
public class CustomRecyclerViewAdapter extends RecyclerView.Adapter {
    Context context;
    List list;

    public CustomRecyclerViewAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewholder, int position) {
        //返回
        if (viewholder != null && viewholder instanceof CustomViewHolder) {
            Data data = (Data) list.get(position);
            ((CustomViewHolder) viewholder).title.setText(data.getTitle());
            ((CustomViewHolder) viewholder).date.setText(data.getDate());
            ((CustomViewHolder) viewholder).author_name.setText(data.getAuthor_name());
            ImageUtils.downloadImageFromURL(((CustomViewHolder) viewholder).thumbnail_pic_s, data.getThumbnail_pic_s());
            ((CustomViewHolder) viewholder).list_item.setOnLongClickListener(new CustomListener(data));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
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


    class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView date;
        public TextView author_name;
        public ImageView thumbnail_pic_s;
        public TextView url, share;
        private LinearLayout list_item;

        public CustomViewHolder(View convertView) {
            super(convertView);
            list_item = (LinearLayout) convertView.findViewById(R.id.list_item);
            title = (TextView) convertView.findViewById(R.id.title);
            date = (TextView) convertView.findViewById(R.id.date);
            author_name = (TextView) convertView.findViewById(R.id.author_name);
            thumbnail_pic_s = (ImageView) convertView.findViewById(R.id.thumbnail_pic_s);//放图片
            url = (TextView) convertView.findViewById(R.id.url);
        }

    }
}
