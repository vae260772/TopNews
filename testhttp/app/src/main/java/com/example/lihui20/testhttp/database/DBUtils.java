package com.example.lihui20.testhttp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lihui20.testhttp.model.Data;
import com.example.lihui20.testhttp.tools.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihui20 on 2016/12/26.
 */
public class DBUtils {
    static DBUtils dbUtils;
    CustomSqliteOpenHelper helper;
    Context mContext;
    List<Data> dataList = null;

    public DBUtils(Context context) {
        mContext = context;
        helper = new CustomSqliteOpenHelper(context);
    }

    public static DBUtils getInstance(Context context) {
        if (dbUtils == null) {
            dbUtils = new DBUtils(context);
        }
        return dbUtils;
    }

    //增加
    public void insert(Data data) {
        //查询是否收藏过了
        if (queryOne(data)) {
            ToastUtils.showToast( "新闻已收藏...");
            return;
        }
        SQLiteDatabase database = helper.getWritableDatabase();
        //String table, String nullColumnHack, ContentValues values
        ContentValues values = new ContentValues();
        values.put("image", data.getThumbnail_pic_s());
        values.put("title", data.getTitle());
        values.put("date", data.getDate());
        values.put("author", data.getAuthor_name());
        values.put("linkaddress", data.getUrl());

        long rowid = database.insert(CustomSqliteOpenHelper.DATABASE_TABLE, "_id", values);
        if (rowid > 0) {
            ToastUtils.showToast( "收藏成功...");
        }
    }

    //delete
    public void delete(Data data) {
        SQLiteDatabase database = helper.getWritableDatabase();
        int row = database.delete(CustomSqliteOpenHelper.DATABASE_TABLE, "title=?", new String[]{data.getTitle()});
        if (row == 1) {
            ToastUtils.showToast( "取消成功...");
        }
    }

    //update
    //query
    public boolean queryOne(Data data) {
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor c = database.rawQuery("select * from " + CustomSqliteOpenHelper.DATABASE_TABLE + " where title=?", new String[]{data.getTitle()});

        if (c.getCount() > 0) {
            c.close();
            return true;
        }
        c.close();
        return false;
    }

    //queryall
    /*
    (_id string, image string, title string, date string,author string,linkaddress string)
     */
    //关键字查询title
    public List queryBykey(String key) {
        List keyList = new ArrayList<>();

        List<Data> list = queryAll();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Data data = list.get(i);
                if (data.getTitle().contains(key)) {
                    keyList.add(data);
                }
            }
        }

        return keyList;
    }

    public List queryAll() {
        dataList = new ArrayList<>();
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor c = database.rawQuery("select * from " + CustomSqliteOpenHelper.DATABASE_TABLE, null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                dataList.add(new Data(c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5)));
            }
        }
        c.close();
        Log.d("dbutils", "dataList---" + dataList);
        return dataList;
    }

    //每次查询5条
    public List queryPage(int position, int count) {
        dataList = new ArrayList<>();
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor c = database.rawQuery("select * from " + CustomSqliteOpenHelper.DATABASE_TABLE + " limit ?,?", new String[]{"" + position, "" + count});
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                dataList.add(new Data(c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5)));
            }
        }c.close();
        Log.d("dbutils", "queryPage---" + dataList);
        return dataList;
    }


}
