package com.rocketboys100.playfuzhou;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryDBOpenHelper extends SQLiteOpenHelper {

    public HistoryDBOpenHelper(Context context) {
        super(context, "history.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String str = "create table history(picPath varchar(128), text varchar(128))";
        db.execSQL(str);

//        //用于测试，正式使用时删去
//        str = "insert into history(picPath,text) values('图片路径1','测试文本1')";
//        db.execSQL(str);
//        str = "insert into history(picPath,text) values('图片路径2','测试文本2')";
//        db.execSQL(str);
//        str = "insert into history(picPath,text) values('图片路径3','测试文本3')";
//        db.execSQL(str);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
