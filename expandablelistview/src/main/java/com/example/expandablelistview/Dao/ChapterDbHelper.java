package com.example.expandablelistview.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.expandablelistview.Beans.Chapter;
import com.example.expandablelistview.Beans.ChapterItem;

public class ChapterDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "db_chapter.db";
    private static final Integer VERSION = 1;


    private static ChapterDbHelper sInstance;
//    单例模式
    public static synchronized ChapterDbHelper getsInstance(Context context){
//    线程锁
        if (sInstance == null){
            sInstance = new ChapterDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private ChapterDbHelper(Context context) {

        super(context, DB_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS " + Chapter.TABLE_NAME
                + " ( "
                + Chapter.COL_ID  + " INTEGER PRIMARY KEY, "
                + Chapter.COL_NAME+ " VARCHAR " +
                " ) "
        );
        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS " + ChapterItem.TABLE_NAME
                + " ( "
                + ChapterItem.COL_ID +  " INTEGER PRIMARY KEY, "
                + ChapterItem.COL_NAME +" VARCHAR, "
                + ChapterItem.COL_PID + " INTEGER " +
                " ) "
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
