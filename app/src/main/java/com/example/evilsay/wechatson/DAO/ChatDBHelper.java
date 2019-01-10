package com.example.evilsay.wechatson.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.evilsay.wechatson.Model.ChatResult;

public class ChatDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ChatBackUp.db";
    private static final Integer VERSION = 1;
    private static ChatDBHelper sInstance;
    private static SQLiteDatabase database;
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("  CREATE TABLE IF NOT EXISTS " + ChatResult.TABLE_NAME
                + " ( "
                +  ChatResult.NAME + " VARCHAR, "
                +  ChatResult.CHAT + " VARCHAR, "
                +  ChatResult.TIME + " VARCHAR "
                + " ) "
        );
        db.execSQL(" CREATE TABLE IF NOT EXISTS " + ChatResult.ID_NAME
                + " ( "
                + ChatResult.ID + " INTEGAR PRIMARY KEY "
                + " ) "
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private ChatDBHelper(Context context){ super(context,DB_NAME,null,VERSION); }

    public static synchronized ChatDBHelper getsInstance(Context context){
        if (sInstance == null){
            sInstance = new ChatDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }
}
