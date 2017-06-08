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
public class TypesDBUtils {
    static TypesDBUtils dbUtils;
    NewsTypeOpenHelper helper;
    Context mContext;
    List<String> dataList = null;

    public TypesDBUtils(Context context) {
        mContext = context;
        helper = new NewsTypeOpenHelper(context);
    }

    public static TypesDBUtils getInstance(Context context) {
        if (dbUtils == null) {
            dbUtils = new TypesDBUtils(context);
        }
        return dbUtils;
    }

    //增加
    public void insert(String type) {
        if (queryOne(type)) {//数据库存在，不需要添加
            return;
        }
        SQLiteDatabase database = helper.getWritableDatabase();
        //String table, String nullColumnHack, ContentValues values
        ContentValues values = new ContentValues();
        values.put("type", type);
        long rowid = database.insert(NewsTypeOpenHelper.DATABASE_TABLE, "_id", values);
        if (rowid > 0) {
            Log.d("TypesDBUtils", "insert type---" + type);
        }
    }

    //批量增加
    public void insertTypesList(List<String> typeList) {
        SQLiteDatabase dataBase = helper.getWritableDatabase();
        dataBase.beginTransaction();        //手动设置开始事务
        //数据插入操作循环
        for (String type : typeList) {
            ContentValues values = new ContentValues();
            values.put("type", type);
            long rowid = dataBase.insert(NewsTypeOpenHelper.DATABASE_TABLE, "_id", values);
            if (rowid > 0) {
                Log.d("TypesDBUtils", "insertTypesList type---" + type);
            }
        }
        dataBase.setTransactionSuccessful();        //设置事务处理成功，不设置会自动回滚不提交
        dataBase.endTransaction();        //处理完成
        dataBase.close();


    }

    //批量删除
    public void deleteTypesList(List<String> typeList) {
        SQLiteDatabase dataBase = helper.getWritableDatabase();
        dataBase.beginTransaction();
        //数据插入操作循环
        for (String type : typeList) {
            int row = dataBase.delete(NewsTypeOpenHelper.DATABASE_TABLE, "type=?", new String[]{type});
            if (row == 1) {
                Log.d("TypesDBUtils", "deleteTypesList type---" + type);
            }
        }
        dataBase.setTransactionSuccessful();        //设置事务处理成功，不设置会自动回滚不提交
        dataBase.endTransaction();        //处理完成
        dataBase.close();
    }

    //delete
    public void delete(String type) {
        SQLiteDatabase database = helper.getWritableDatabase();
        int row = database.delete(NewsTypeOpenHelper.DATABASE_TABLE, "type=?", new String[]{type});
        if (row == 1) {
            Log.d("TypesDBUtils", "delete type---" + type);
        }
    }

    //query
    public boolean queryOne(String type) {
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor c = database.rawQuery("select * from " + NewsTypeOpenHelper.DATABASE_TABLE + " where type=?", new String[]{type});

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
//    public List queryBykey(String key) {
//        List keyList = new ArrayList<>();
//
//        List<Data> list = queryAll();
//        if (list != null && list.size() > 0) {
//            for (int i = 0; i < list.size(); i++) {
//                Data data = list.get(i);
//                if (data.getTitle().contains(key)) {
//                    keyList.add(data);
//                }
//            }
//        }
//
//        return keyList;
//    }
//
    public List queryAll() {
        dataList = new ArrayList<>();
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor c = database.rawQuery("select * from " + NewsTypeOpenHelper.DATABASE_TABLE, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                dataList.add(c.getString(1));
            }
        }

        c.close();
        Log.d("dbutils", "dataList---" + dataList);
        return dataList;
    }
//
//    //每次查询5条
//    public List queryPage(int position, int count) {
//        dataList = new ArrayList<>();
//        SQLiteDatabase database = helper.getWritableDatabase();
//        Cursor c = database.rawQuery("select * from " + NewsTypeOpenHelper.DATABASE_TABLE + " limit ?,?", new String[]{"" + position, "" + count});
//        if (c != null && c.getCount() > 0) {
//            while (c.moveToNext()) {
//                dataList.add(new Data(c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5)));
//            }
//        }c.close();
//        Log.d("dbutils", "queryPage---" + dataList);
//        return dataList;
//    }


}
