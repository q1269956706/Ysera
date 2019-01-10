package com.example.expandablelistview.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.expandablelistview.Beans.Chapter;
import com.example.expandablelistview.Beans.ChapterItem;

import java.util.ArrayList;
import java.util.List;

public class ChapterDao {
    public List<Chapter>  loadFromDb(Context context){
        ChapterDbHelper chapterDbHelper = ChapterDbHelper.getsInstance(context);
//        新建数据库查询
        SQLiteDatabase readableDatabase = chapterDbHelper.getReadableDatabase();
        List<Chapter> chapterList = new ArrayList<>();
//        查询数据
        Cursor cursor = readableDatabase.rawQuery("select * from " + Chapter.TABLE_NAME, null);
        Chapter chapter = null;
//        取出缓存数据
        while (cursor.moveToNext()){
            chapter = new Chapter();
            int id  = cursor.getInt(cursor.getColumnIndex(Chapter.COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(Chapter.COL_NAME));
            chapter.setId(id);
            chapter.setName(name);
            chapterList.add(chapter);
        }
        ChapterItem chapterItem = null;
        for (Chapter parent : chapterList){
            int pid = parent.getId();
//            手工处理转义
            cursor  = readableDatabase.rawQuery("select * from "
                            +ChapterItem.TABLE_NAME + " where "
                            + ChapterItem.COL_PID + " =? ",
                            new String[]{pid + ""});
//            select * from table_name where pid = pid
            while (cursor.moveToNext()){
                chapterItem = new ChapterItem();
                int id  = cursor.getInt(cursor.getColumnIndex(ChapterItem.COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(ChapterItem.COL_NAME));
                chapterItem.setId(id);
                chapterItem.setName(name);
                parent.addChild(chapterItem);
            }

        }
        cursor.close();
        return chapterList;
    }
//    缓存数据
    public void insert2D (Context context,List<Chapter> chapterList){
        if (context == null){
            throw new IllegalArgumentException("context can not be null");
        }
        if (chapterList == null || chapterList.isEmpty()){
            return;
        }
//        父类列表缓存
        ChapterDbHelper dbHelper = ChapterDbHelper.getsInstance(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        ContentValues contentValues = null;
        for(Chapter chapter : chapterList){
            contentValues = new ContentValues();
            contentValues.put(Chapter.COL_ID,chapter.getId());
            contentValues.put(Chapter.COL_NAME,chapter.getName());
//            防止数据库缓存key报异常,执行数据库更新
            db.insertWithOnConflict(Chapter.TABLE_NAME,
                    null,contentValues,
                    SQLiteDatabase.CONFLICT_REPLACE);
//            子类列表缓存
            List<ChapterItem> children = chapter.getChapterItems();
            for (ChapterItem chapterItem: children) {
                contentValues = new ContentValues();
                contentValues.put(ChapterItem.COL_ID,chapterItem.getId());
                contentValues.put(ChapterItem.COL_NAME,chapterItem.getName());
                contentValues.put(ChapterItem.COL_PID,chapter.getId());

                db.insertWithOnConflict(ChapterItem.TABLE_NAME,
                        null,
                        contentValues,
                        SQLiteDatabase.CONFLICT_REPLACE);
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }
}
