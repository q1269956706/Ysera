package com.example.evilsay.wechatson.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.evilsay.wechatson.Beans.ChapterChat;
import com.example.evilsay.wechatson.Beans.ChapterChatItem;

import java.util.ArrayList;
import java.util.List;

public class ChapterChatDao {
    public List<ChapterChat> loadfromDb(Context context){
        ChapterChatDBHelper helper = ChapterChatDBHelper.getsInstance(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        List<ChapterChat> chapterChats = new ArrayList<>();
        Cursor query = database.rawQuery("select * from " + ChapterChat.TABLE_NAME, null,null);
        ChapterChat chapterChat = null;
        while (query.moveToNext()){
            chapterChat = new ChapterChat();
            int id      = query.getInt(query.getColumnIndex(ChapterChat.COD_ID));
            String name = query.getString(query.getColumnIndex(ChapterChat.COD_NAME));
            chapterChat.setId(id);
            chapterChat.setName(name);
            chapterChats.add(chapterChat);
        }
        ChapterChatItem chapterChatItem = null;
        for (ChapterChat chat: chapterChats) {
            int pid = chat.getId();
            Cursor rawQuery = database.rawQuery("select * from " + ChapterChatItem.TABLE_NAME + " where " + ChapterChatItem.COD_PID + " =? "
                    , new String[]{pid + ""});
            while (rawQuery.moveToNext()){
                chapterChatItem = new ChapterChatItem();
                int anInt = rawQuery.getInt(rawQuery.getColumnIndex(ChapterChatItem.COD_ID));
                String name = rawQuery.getString(rawQuery.getColumnIndex(ChapterChatItem.COD_NAME));
                chapterChatItem.setId(anInt);
                chapterChatItem.setName(name);
                chat.addChild(chapterChatItem);
            }
            rawQuery.close();
        }
        query.close();
        return chapterChats;

    }
    public void inster2D(Context context,List<ChapterChat>list) {
//        防御试编程
        if (context == null){
            throw new IllegalArgumentException("context can not be null");
        }
        if (list == null || list.isEmpty()){
            return ;
        }
        ChapterChatDBHelper helper = ChapterChatDBHelper.getsInstance(context);
        SQLiteDatabase  database                = helper.getReadableDatabase();
        database.beginTransaction();
        ContentValues values = null;
//        父列表更新缓存
        for (ChapterChat chapterChat : list) {
            values = new ContentValues();
            values.put(ChapterChat.COD_ID,chapterChat.getId());
            values.put(ChapterChat.COD_NAME,chapterChat.getName());
            // 删除不必要的更新，防止数据库缓存key报异常,并执行数据库更新
            database.insertWithOnConflict(ChapterChat.TABLE_NAME
                    ,null,values
                    ,SQLiteDatabase.CONFLICT_REPLACE);
//            子类表更新缓存
            List<ChapterChatItem> chapterChatItems = chapterChat.getChapterChatItems();
            for (ChapterChatItem item : chapterChatItems) {
                values = new ContentValues();
                values.put(ChapterChatItem.COD_ID,item.getId());
                values.put(ChapterChatItem.COD_NAME,item.getName());
                values.put(ChapterChatItem.COD_PID,chapterChat.getId());

                database.insertWithOnConflict(ChapterChatItem.TABLE_NAME
                        ,null,values
                        ,SQLiteDatabase.CONFLICT_REPLACE);
            }

        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }
}
