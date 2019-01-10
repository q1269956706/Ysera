package com.example.evilsay.wechatson.PLZ;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.evilsay.wechatson.DAO.ChatDao;
import com.example.evilsay.wechatson.Model.ChatResult;
import com.example.evilsay.wechatson.Utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatBlz {
    private ChatDao chatDao = new ChatDao();
    public void loadFromDb(final Context context,boolean userCache, final String user, final String chat ,final Callback callback){
        @SuppressLint("StaticFieldLeak") AsyncTask<Boolean,Void,List<ChatResult>> asyncTask = new AsyncTask<Boolean, Void, List<ChatResult>>() {
            private Exception ex;
            @Override
            protected List<ChatResult> doInBackground(Boolean... booleans) {
                boolean checkNormal = booleans[0];
//                    选择数据加载模式
                List<ChatResult> chatResults = new ArrayList<>();
                if (checkNormal){
                    List<ChatResult> chatResults1 = chatDao.LoadFromLocal(context);
                    chatResults.addAll(chatResults1);
                }
//                网络缓存数据
                if (chatResults.isEmpty()){
                    try {
                        List<ChatResult> chatResults1 = loadFromNetWork(context,user,chat);
                        chatDao.LoadFromNetWork(context,chatResults1);
                        chatResults.addAll(chatResults1);
                    } catch (IOException e) {
                        e.printStackTrace();
                        this.ex = e;
                    }
                }
                return chatResults;
            }
            @Override
            protected void onPostExecute(List<ChatResult> chatResults) {
                if (ex != null){
                    callback.UnSuccess(ex);
                }
                callback.OnSuccess(chatResults);
            }
        };
        asyncTask.execute(userCache);
    }
    private List<ChatResult> loadFromNetWork(Context context, String user, String chat) throws IOException {
        List<ChatResult> chatResults = new ArrayList<>();
        String content = HttpUtils.WeChatList(user,chat);
        if (content != null){
            chatResults = JsonAnalysisOther(content);
        }
        return chatResults;
    }
    private List<ChatResult> JsonAnalysis(String data){
        JsonArray array = new JsonParser().parse(data).getAsJsonArray();
        List<ChatResult > chatResults = new ArrayList<>();
        Gson gson = new Gson();
        for (JsonElement element : array) {
             ChatResult chatResult = gson.fromJson(element,ChatResult.class);
             chatResults.add(chatResult);
        }
        return chatResults;
    }
    private List<ChatResult> JsonAnalysisOther(String data){
        //ChatResult chatResults1 = new Gson().fromJson(data,ChatResult.class);
        // 泛型可以使集合能够记住集合内元素各类型
        /**
         * fromJson()第一个参数：将一个Json数据转换为Json对象
         * 第二个参数：要转换成对象的类型<Object>
         * GSON 提供了 TypeToken 这个类来帮助我们捕获像 List 这样的泛型信息。
         * Java编译器会把捕获到的泛型信息编译到这个匿名内部类里，
         * 然后在运行时就可以被 getType() 方法用反射的 API 提取到。
         * 它将自定义泛型 T 转成 xxx.Class 。比如 TypeToken 经过 getType() 后就是 List<ChatResult> 。
         * 小写class表示是一个类类型，大写Class表示这个类的名称
         *
         */
        return new Gson().fromJson(data, new TypeToken<List<ChatResult>>() {}.getType());
    }
    public  interface Callback{
        void OnSuccess(List<ChatResult> results);
        void UnSuccess(Exception ex);
    }
}
