package com.example.lihui20.testhttp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lihui20.testhttp.model.Data;
import com.example.lihui20.testhttp.tools.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lihui20 on 2016/12/26.
 */
public class NewsCacheUtils {
    static NewsCacheUtils dbUtils;
    NewsCacheOpenHelper helper;
    Context mContext;
    List<Data> dataList = null;

    public NewsCacheUtils(Context context) {
        mContext = context;
        helper = new NewsCacheOpenHelper(context);
    }

    public static NewsCacheUtils getInstance(Context context) {
        if (dbUtils == null) {
            dbUtils = new NewsCacheUtils(context);
        }
        return dbUtils;
    }

    public void clearAll() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.beginTransaction();
        int number = database.delete(NewsCacheOpenHelper.DATABASE_TABLE, null, null);
        Log.d("clearAll", "number---" + number);
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    //保存
    public void insert(HashMap<String, List<Data>> map) {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.beginTransaction();
        List<String> keyList = new ArrayList<>();
        if (map != null && map.size() > 0) {
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                java.util.Map.Entry entry = (Map.Entry) iterator.next();
                keyList.add((String) entry.getKey());

            }
            Log.d("cache", "keyList---" + keyList);
            for (int i = 0; i < keyList.size(); i++) {//分类
                List<Data> list = map.get(keyList.get(i));
                Log.d("cache", "list---" + list);
                String key = keyList.get(i);
                for (int j = 0; j < list.size(); j++) {//数据聚合
                    Data data = list.get(j);
                    if (queryOne(key, data))//存在了
                        return;

                    ContentValues values = new ContentValues();
                    /*
                    _id integer PRIMARY KEY AUTOINCREMENT,type string, image string, title string, " +
            "date string,author string,linkaddress string
                     */
                    values.put("type", key);
                    values.put("image", data.getThumbnail_pic_s());
                    values.put("title", data.getTitle());
                    values.put("date", data.getDate());
                    values.put("author", data.getAuthor_name());
                    values.put("linkaddress", data.getUrl());
                    long rowid = database.insert(NewsCacheOpenHelper.DATABASE_TABLE, "_id", values);

                }
            }

        }
        database.setTransactionSuccessful();
        database.endTransaction();

    }


    //queryall
    /*
    (_id string, image string, title string, date string,author string,linkaddress string)
     */


    public List queryAllByType(String key) {
        dataList = new ArrayList<>();
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor c = database.rawQuery("select * from " +
                NewsCacheOpenHelper.DATABASE_TABLE + " where type=?", new String[]{key});
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                dataList.add(new Data(c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6)));
            }
        }
        c.close();
        Log.d("dbutils", "dataList---" + dataList);
        return dataList;
    }

    //type + title一样重复
    public boolean queryOne(String type, Data data) {
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor c = database.rawQuery("select * from " + NewsCacheOpenHelper.DATABASE_TABLE + " where type=? and" +
                " title=?", new String[]{type, data.getTitle()});

        if (c.getCount() > 0) {
            c.close();
            return true;
        }
        c.close();
        return false;
    }
}
