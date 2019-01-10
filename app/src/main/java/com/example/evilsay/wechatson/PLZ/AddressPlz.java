package com.example.evilsay.wechatson.PLZ;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.evilsay.wechatson.Beans.ChapterChat;
import com.example.evilsay.wechatson.Beans.ChapterChatItem;
import com.example.evilsay.wechatson.DAO.ChapterChatDao;
import com.example.evilsay.wechatson.Model.AddressWeChat;
import com.example.evilsay.wechatson.Utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class AddressPlz {
    private final String TAG = "AddressPlz" ;
    private ChapterChatDao chatDao = new ChapterChatDao();
    public void loadFromDb(final Context context, final Callback callback, boolean useCache){
        @SuppressLint("StaticFieldLeak") AsyncTask<Boolean,Void,List<ChapterChat>> asyncTask = new AsyncTask<Boolean, Void, List<ChapterChat>>() {
            private Exception ex;
            @Override
            protected List<ChapterChat> doInBackground(Boolean... booleans) {
                boolean checkNet = booleans[0];
                List<ChapterChat> chapterChats = new ArrayList<>();
                try {
                    if (checkNet) {
                        List<ChapterChat>loadFromDb = chatDao.loadfromDb(context);
                        chapterChats.addAll(loadFromDb);
                    }
                    if (chapterChats.isEmpty()){
                        List<ChapterChat> chapterChats1 = OkGet(context);
                        chatDao.inster2D(context,chapterChats1);
                        chapterChats.addAll(chapterChats1);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    this.ex =ex;
                }
                return chapterChats;
            }

            @Override
            protected void onPostExecute(List<ChapterChat> chapterChats) {
                if (ex !=null){
                    callback.onFailed(ex);
                }
                callback.onSuccess(chapterChats);
            }
        };
        asyncTask.execute(useCache);
    }

    private List<ChapterChat> OkGet(Context context) throws IOException {
        List<ChapterChat> chapterChats = new ArrayList<>();
        String content = HttpUtils.httpUtilsList("http://123.207.85.214/chat/member.php");
        if (content != null){
            chapterChats = JsonKiller(content,context);
        }
        return chapterChats;
    }

    private List<ChapterChat> JsonKiller(String data,Context context){
        List<ChapterChat> chapterChats = new ArrayList<>();
        List<AddressWeChat> addressWeChats = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        ChapterChat chapterChat = new ChapterChat(1,"群成员");
        ChapterChat chat        = new ChapterChat(2,"手机联系人");
        JsonArray array         = new JsonParser().parse(data).getAsJsonArray();
        Gson        gson        = new Gson();
//        解析JSON
        for (JsonElement element: array) {
            AddressWeChat addressWeChat = gson.fromJson(element,AddressWeChat.class);
            addressWeChats.add(addressWeChat);
        }
//        JSON数据赋值
        for (int i = 0; i < addressWeChats.size(); i++) {
            if (!addressWeChats.get(i).getName().isEmpty()) {
                Log.e(TAG, "JsonKiller: " + addressWeChats.get(i).getName());
                ChapterChatItem chapterChatItem = new ChapterChatItem(i, addressWeChats.get(i).getName());
                chapterChat.addChild(chapterChatItem);
            }
        }
        Cursor query1 = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,null);
        while (query1.moveToNext()) {
            String name = query1.getString(query1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String _id = query1.getString(query1.getColumnIndex(ContactsContract.Contacts._ID));
            String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?" ;
            Cursor query = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, selection, new String[]{_id}, null);
            while (query.moveToNext()) {
                String phone = query.getString(query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                int tid = Integer.parseInt(_id);
                ChapterChatItem chapterChatItem = new ChapterChatItem(tid,name);
                chat.addChild(chapterChatItem);
            }
        }
//        获取手机联系人

        chapterChats.add(chapterChat);
        chapterChats.add(chat);
//        try {
//            JSONArray jsonArray = new JSONArray(data);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                AddressWeChat addressWeChat = new AddressWeChat();
//                JSONObject jsonObject       = (JSONObject) jsonArray.get(i);
//                addressWeChat.setName(jsonObject.optString("name"));
//                addressWeChat.setUser(jsonObject.optString("user"));
//                addressWeChats.add(addressWeChat);
//            }


//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return chapterChats;
    }

    public  interface Callback{
        void onSuccess(List<ChapterChat> chapterChatList);
        void onFailed(Exception ex);
    }
}
