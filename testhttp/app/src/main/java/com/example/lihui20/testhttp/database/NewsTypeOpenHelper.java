package com.example.lihui20.testhttp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lihui20 on 2016/12/26.
 */
public class NewsTypeOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "type.db";
    public static final String DATABASE_TABLE = "type_table";
    public static final int DATABASE_VERSION = 1;

    // 建表语句，大小写不敏感
    private static final String CREATETABLE = "create table "
            + DATABASE_TABLE
            + " (_id integer PRIMARY KEY AUTOINCREMENT,type string);";
    public NewsTypeOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    public NewsTypeOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATETABLE);

    }

    // 删除表
    private void deleteDB(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + DATABASE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.deleteDB(db);
        this.onCreate(db);
    }
}
