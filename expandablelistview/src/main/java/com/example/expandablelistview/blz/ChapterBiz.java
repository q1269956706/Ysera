package com.example.expandablelistview.blz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.expandablelistview.Beans.Chapter;
import com.example.expandablelistview.Beans.ChapterItem;
import com.example.expandablelistview.Dao.ChapterDao;
import com.example.expandablelistview.MainActivity;
import com.example.expandablelistview.Utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ChapterBiz {
    private ChapterDao chapterDao = new ChapterDao();
    private static final String TAG  = "ChapterBiz";

    public void loadData(final Context context, final CallBack callBack, boolean useCache){

        @SuppressLint("StaticFieldLeak") AsyncTask<Boolean,Void,List<Chapter>> asyncTask = new AsyncTask<Boolean, Void, List<Chapter>>() {
            private Exception ex;
            @Override
            protected List<Chapter> doInBackground(Boolean... booleans) {
//                取参
                boolean isUseCache = booleans[0];
                List<Chapter> chapterList = new ArrayList<>();
                try {

                    if (isUseCache){
//                        从缓存中取出数据
                        List<Chapter>chapterList1 = chapterDao.loadFromDb(context);
                        Log.e(MainActivity.TAG, "doInBackground: "+ chapterList1);
                        chapterList.addAll(chapterList1);
                    }
//                    当缓存为空时
                    if (chapterList.isEmpty()){
//                        从网络得到数据
                        List<Chapter> chapterListFromNet = loadFromNet(context);
                        //缓存到数据库
                        chapterDao.insert2D(context,chapterListFromNet);
                        chapterList.addAll(chapterListFromNet);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    this.ex = e;
                }
                return chapterList;
            }

            @Override
            protected void onPostExecute(List<Chapter> chapters) {
                if (ex != null){
                    callBack.onFailed(ex);
                }
                callBack.onSuccess(chapters);
            }
        };
        asyncTask.execute(useCache);
    }

    private List<Chapter> loadFromNet(Context context) {
        List<Chapter> chapterList = new ArrayList<>();
        String url = "http://www.imooc.com/api/expandablelistView";
        String content = HttpUtils.doGet(url);
        Log.e(MainActivity.TAG, "loadFromNet: "+content);
        if(content != null){
            chapterList = parseContent(content);
        }
        return chapterList;
    }

    private List<Chapter> parseContent(String content) {
        List<Chapter> chapterList = new ArrayList<>();
//        先转JsonObject
        JsonObject jsonObject = new JsonParser().parse(content).getAsJsonObject();
//        获取响应吗
        int asInt = jsonObject.get("errorCode").getAsInt();
        Gson    gson = new Gson();
        if (asInt == 0){
//            转换成JsonArray数组
            JsonArray array = jsonObject.getAsJsonArray("data");
//            第一层循环，循环父类
//            JsonElement能把JsonArray中的每一个元素转成 JsonObject,甚至说它本身就是JsonObject.
            for (JsonElement element: array) {
//                它将泛型T转成.class,比如上面的TypeToken经过getType()后就是Chapter.class.
                Chapter chapter = gson.fromJson(element,new TypeToken<Chapter>(){}.getType());
                chapterList.add(chapter);
                JsonObject ChatItem  = element.getAsJsonObject();
                JsonArray  arrayItem = ChatItem.getAsJsonArray("children");
//                第二层循环,循环子类
                for (JsonElement elementItem : arrayItem) {
                    ChapterItem chapterItem = gson.fromJson(elementItem,new TypeToken<ChapterItem>(){}.getType());
                    chapter.addChild(chapterItem);
                }
            }
        }
//        try {
//            JSONObject jsonObject = new JSONObject(content);
//            int errorCode = jsonObject.optInt("errorCode");
//            if (errorCode == 0){
//                JSONArray data = jsonObject.optJSONArray("data");
//                for (int i = 0; i < data.length(); i++) {   // 遍历JSON数组第一层
//                    JSONObject chapterObj = data.getJSONObject(i);
//                    int id = chapterObj.optInt("id"); //取值不正确会进行转换
//                    String name = chapterObj.optString("name");
//                    Chapter chapter = new Chapter(id,name);
//                    chapterList.add(chapter);
//                    JSONArray children = chapterObj.getJSONArray("children");
//                    if (children != null){          // 遍历JSON数组第二层
//                        for (int j = 0; j < children.length(); j++) {
//                            JSONObject chapterItemObj = children.getJSONObject(j);
//                            int cid = chapterItemObj.optInt("id");
//                            String cname = chapterItemObj.optString("name");
//                            ChapterItem chapterItem = new ChapterItem(cid,cname);
//                            chapter.addChild(chapterItem);
//                        }
//                    }
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return chapterList;
    }

    public static interface CallBack{
        void onSuccess(List<Chapter> chapterList);

        void onFailed(Exception ex);
    }
}
