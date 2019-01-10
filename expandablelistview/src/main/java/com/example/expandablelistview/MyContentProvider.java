package com.example.expandablelistview;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.example.expandablelistview.Beans.Chapter;
import com.example.expandablelistview.Beans.ChapterItem;

public class MyContentProvider extends ContentProvider {

    private SQLiteDatabase database;
    private final static String TAG = "MyProvider";
    private UriMatcher uriMatcher;

    // URL的解析
    // UriMatcher: 在contentProvider创建时，制定好匹配规则，当调用了contentProvider中的操作方法时
    // 利用匹配规则类去匹配传的Uri,根据不同的Uri给出不同的处理
    // Uri自带的解析方法
    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        return database.delete(Chapter.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        long insert = 0;
//        int match = uriMatcher.match(uri);
//        switch (match){
//            case 1000:
//                Log.e(TAG, "insert: "+"匹配的路径是helloworld路径" );
//                break;
//            default:
//                Log.e(TAG, "insert: "+"匹配的路径不是helloworld路径" );
//                break;
//        }
        if (values.size() > 0){
            insert = database.insert(Chapter.TABLE_NAME, null, values);
        }else {
            String authority = uri.getAuthority();
            String path      = uri.getPath();
            String query     = uri.getQuery();
            String name      = uri.getQueryParameter("name");
            String _id       = uri.getQueryParameter("_id");
            values.put("name",name);
            Log.e(TAG, "insert: 主机名"+ authority + "路径地址为" + path + "查询语句为" +query + "查询的名字为" + name);
            insert = database.insert(Chapter.TABLE_NAME,null,values);
        }
        return ContentUris.withAppendedId(uri,insert);
    }
//   在ContentProvider 创建时候调用
    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        SQLiteOpenHelper helper = new SQLiteOpenHelper(getContext(),"chat.db",null,2) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS " + Chapter.TABLE_NAME
                        + " ( "
                        + Chapter.COL_ID  + " INTEGER PRIMARY KEY, "
                        + Chapter.COL_NAME+ " VARCHAR " +
                        " ) "
                );
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS " + ChapterItem.TABLE_NAME
                        + " ( "
                        + ChapterItem.COL_ID +  " INTEGER PRIMARY KEY, "
                        + ChapterItem.COL_NAME +" VARCHAR, "
                        + ChapterItem.COL_PID + " INTEGER " +
                        " ) "
                );
            }
        };
        database = helper.getReadableDatabase();
        // 参数 ：代表无法匹配
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // 传入 content，传入输入路径，传入匹配码
        uriMatcher.addURI("com.example.expandablelistview","helloWorld",1000);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        return database.query(Chapter.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        return database.update(Chapter.TABLE_NAME, values, selection, selectionArgs);
    }
}
