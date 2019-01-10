package com.example.evilsay.wechatson.Utils;

import android.util.Log;


import com.example.evilsay.wechatson.Beans.ChapterChat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    private static final String ADDRESS = "http://123.207.85.214/chat/login.php";
    private static final String LOG_ADDRESS = "http://123.207.85.214/chat/register.php";
    private static final String WeChat_Address = "http://123.207.85.214/chat/chat1.php";
    private static final String MAIL_LIST ="http:/=/123.207.85.214/chat/member.php";
    private static final String TestAddress = "http://evilsay.natapp1.cc/sell/buyer/product/list";
    /**
     * 构造一个OkHttp GET方法的请求
     * @param callback 异步回调
     */
    public static void httpUtils(Callback callback){
        //声明一个OkHttpClient并调用它的方法
        OkHttpClient client = new OkHttpClient();
        //访问请求
        Request request     = new Request.Builder()
                //URL地址
                .url(MAIL_LIST)
                .build();
//        调用HttpClient中的newCall中的网络异步回调
        client.newCall(request).enqueue(callback);
    }
//    <同步线程>返回值时请求<--可以放在AsyncTask线程中-->
    public static String httpUtilsList(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request     = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()){
            return response.body().string();
        }
        throw new IOException("<--Please Check You Url-->");
    }
    public static String WeChatList(String user,String chat) throws IOException{
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("user",user)
                .add("chat", chat)
                .build();
        Request request = new Request.Builder()
                .url(WeChat_Address)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()){
            return response.body().string();
        }
        throw new IOException("<--Please Check You NETWORK-->");
    }
//    Post方法的传值
    public static void httpPostUtils_Reg(String name,String user,String password,Callback callback){
        OkHttpClient client = new OkHttpClient();
        //PSOT 表单创建
        RequestBody body = new FormBody.Builder()
                .add("name",name)
                .add("user",user)
                .add("password",password)
                .build();
        //访问请求
        Request request     = new Request.Builder()
                .url(LOG_ADDRESS)
                //提交表单
                .post(body)
                .build();
//        网络异步回调
        client.newCall(request).enqueue(callback);
    }
    public static void httpPostUtils_Login(String user,String password,Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("user",user)
                .add("password",password)
                .build();
        Request request     = new Request.Builder()
                .url(ADDRESS)
                .post(body)
                .build();
//        调用HttpClient中的newCall网络异步回调
        client.newCall(request).enqueue(callback);
    }
//    网络超时检查<该方法需要另开一条activity中的线程>同步线程
    public static boolean HttpLostExecute(final String Address) {
        boolean success;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();
        Request.Builder builder = new Request.Builder().url(Address);
        Call call = client.newCall(builder.build());
        try {
//                              调用同步回调线程
            Response response = call.execute();
            success = true;
        } catch (IOException e) {
            Log.d("HttpUtils", "连接超时");
            success = false;
        }
        return success;
    }
//    网络超时检查<异步线程>
    public static void HttLostEnqueue(String Address,Callback callback){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();
        Request.Builder builder = new Request.Builder().url(Address);
        client.newCall(builder.build()).enqueue(callback);
    }
    public static void WeChatPost(String user,String chat,Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody builder = new FormBody.Builder()
                .add("user",user)
                .add("chat",chat)
                .build();
        Request builder1 = new Request.Builder()
                .url(WeChat_Address)
                .post(builder)
                .build();
        client.newCall(builder1).enqueue(callback);
    }
    public static String doGet(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[512];
                while ((len = inputStream.read(buf)) != -1) {
                    byteArrayOutputStream.write(buf, 0, len);
                }
                byteArrayOutputStream.flush();
                return byteArrayOutputStream.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }
}

