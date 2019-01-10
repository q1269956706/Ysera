package com.example.evilsay.wechatson.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.evilsay.wechatson.Model.ChatResult;

import java.util.ArrayList;
import java.util.List;

public class ChatDao {
    public List<ChatResult> LoadFromLocal(Context context){
        ChatDBHelper chatDBHelper   = ChatDBHelper.getsInstance(context);
        SQLiteDatabase database     = chatDBHelper.getReadableDatabase();
        List<ChatResult> chatResults= new ArrayList<>();
        Cursor query = database.rawQuery("select * from " + ChatResult.TABLE_NAME, null);
        ChatResult chatResult  = null;
        while (query.moveToNext()){
            chatResult = new ChatResult();
            String name = query.getString(query.getColumnIndex(ChatResult.NAME));
            String chat = query.getString(query.getColumnIndex(ChatResult.CHAT));
            String time = query.getString(query.getColumnIndex(ChatResult.TIME));
            chatResult.setChat(chat);
            chatResult.setName(name);
            chatResult.setTime(time);
            chatResults.add(chatResult);
        }
        query.close();
        return chatResults;
    }

    public void LoadFromNetWork(Context context,List<ChatResult> chatResults){
        if(context == null){
            throw new IllegalArgumentException("context not be null");
        }
        if (chatResults == null || chatResults.isEmpty()){
            return ;
        }
        ChatDBHelper chatDBHelper = ChatDBHelper.getsInstance(context);
        SQLiteDatabase database   = chatDBHelper.getReadableDatabase();
        database.beginTransaction();
        ContentValues values = null;
        for (ChatResult result : chatResults) {
            values = new ContentValues();
            values.put(ChatResult.CHAT,result.getChat());
            values.put(ChatResult.NAME,result.getName());
            values.put(ChatResult.TIME,result.getTime());
            database.insertWithOnConflict(ChatResult.TABLE_NAME,null
                    ,values,SQLiteDatabase.CONFLICT_REPLACE);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }
    public static Cursor findDataBase(Context context){
        ChatDBHelper chatDBHelper = ChatDBHelper.getsInstance(context);
        SQLiteDatabase database   = chatDBHelper.getReadableDatabase();
        return database.rawQuery("select * from " + ChatResult.TABLE_NAME,null);
    }
}
