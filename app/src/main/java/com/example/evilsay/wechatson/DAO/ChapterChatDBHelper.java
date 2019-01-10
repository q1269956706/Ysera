package com.example.evilsay.wechatson.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.evilsay.wechatson.Beans.ChapterChat;
import com.example.evilsay.wechatson.Beans.ChapterChatItem;

public class ChapterChatDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "chapterChat.db";
    private static final Integer VERSION = 1;
    private static ChapterChatDBHelper sInstance;
    private ChapterChatDBHelper(Context context){ super(context,DB_NAME,null,VERSION); }
    private static SQLiteDatabase database;
    public static synchronized ChapterChatDBHelper getsInstance(Context context){
//        单例模式下的线程锁,并实现了构造方法
        if (sInstance == null){
            sInstance = new ChapterChatDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        database = db;
        db.execSQL("  CREATE TABLE IF NOT EXISTS " + ChapterChat.TABLE_NAME
                + " ( "
                +  ChapterChat.COD_ID + " INTEGAR PRIMARY KEY, "
                +  ChapterChat.COD_NAME + " VARCHAR "
                + " ) "
        );
        db.execSQL("  CREATE TABLE IF NOT EXISTS " + ChapterChatItem.TABLE_NAME
                + " ( "
                +  ChapterChatItem.COD_ID + " INTEGAR PRIMARY KEY, "
                +  ChapterChatItem.COD_NAME + " VARCHAR, "
                +  ChapterChatItem.COD_PID  +" INTEGAR "
                + " ) "
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
