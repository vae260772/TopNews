package com.example.lihui20.testhttp.interface2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
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
import com.example.lihui20.testhttp.R;
import com.example.lihui20.testhttp.adapter.CustomRecyclerViewAdapter;
import com.example.lihui20.testhttp.customview.PullToRefreshRecyclerView;
import com.example.lihui20.testhttp.customview.SpacesItemDecoration;
import com.example.lihui20.testhttp.model.Data;
import com.example.lihui20.testhttp.tools.Utils;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by lihui20 on 2016/12/5.
 */

public interface CustomOnclick {
   void onMyItemClick(Data data);
}
