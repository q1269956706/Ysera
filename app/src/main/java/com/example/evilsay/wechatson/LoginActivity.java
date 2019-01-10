package com.example.evilsay.wechatson;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.evilsay.wechatson.Model.LessonResult;
import com.example.evilsay.wechatson.Utils.HttpUtils;
import com.example.evilsay.wechatson.Utils.PowerManage;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String ADDRESS = "http://123.207.85.214/chat/login.php";
    public static final String USERNAME = "username";
    public static final String PWD = "pwd";
    public static final String LOGINDATE = "LOGINDATE";
    private String status;
    private String user;
    private String name;
    protected ImageButton Reg_Button;
    protected Button Login_Button;
    protected EditText User_Login,User_Pwd;
    private String TAG = "Login";
    public static final String LOGIN_USER = "LOGIN_USER";
    public static final String LOGIN_NAME = "LOGIN_NAME";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_login_layout);
        InitView();
        ReceiveToDataForReg();
        SaveText();
//        android5.0的第一次权限检查
        PowerManage.CheckOne(this);
        PowerManage.verifyStoragePermissions(this);
    }
    //    接收到注册界面数据
    private void ReceiveToDataForReg() {
        if (getIntent() != null){
            String user_name = getIntent().getStringExtra(FreeRegister.USER_NAME);
            User_Login.setText(user_name);
            String pwd = getIntent().getStringExtra(FreeRegister.USER_PWD);
            User_Pwd.setText(pwd);
        }
    }

    private void InitView() {
        Login_Button = findViewById(R.id.Login_Button);
        User_Login   = findViewById(R.id.User_Edit);
        User_Pwd     = findViewById(R.id.Password_EditText);
        Reg_Button   = findViewById(R.id.reg);
        Reg_Button  .setOnClickListener(this);
        Login_Button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Login_Button:
               LoginCheck();
                break;
            case R.id.reg:
//                测试

                startActivity(new Intent(LoginActivity.this, FreeRegister.class));
//                PowerManage.getUninstallAppIntent(this,"com.example.evilsay.wechatson");
                break;
        }
    }

    //回调解析JSON字符
    private void JsonSteam(String result){
        LessonResult lessonResult = new LessonResult();
        try {
            JSONObject jsonObject     = new JSONObject(result);
            status   = jsonObject.getString("status");
            user     = jsonObject.getString("user");
            name     = jsonObject.getString("name");
            lessonResult.setStatus(status);
            lessonResult.setName(name);
            lessonResult.setUser(user);
            Log.e(TAG, "JsonSteam: "+status+"\n"+user+"\n"+name+"\n" );
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JsonSteam:unOk "+status+"\n"+user+"\n"+name+"\n" );
        }
    }
//    网络检查
    private void LostHttp(){
        HttpUtils.HttLostEnqueue(ADDRESS, new Callback() {
            @Override
//          网络请求失败
            public void onFailure(Call call, IOException e) { HttpLostDialog(); }
            @Override
//          网络请求成功
            public void onResponse(Call call, Response response) throws IOException {
//              POST请求传值
                HttpUtils.httpPostUtils_Login(User_Login.getText().toString(), User_Pwd.getText().toString(), new Callback() {
                    @Override
//                  POST请求传值失败
                    public void onFailure(Call call, IOException e) {}
                    @Override
//                  POST请求传值成功
                    public void onResponse(Call call, Response response) throws IOException {
                        JsonSteam(response.body().string());
                        if (status.equals("登陆成功")){
                            ShowSuccessView();
                        }else { ShowUnSuccessView(); }
                    }
                });
            }
        });
    }
//  EditText字符串不能为空
    private void LoginCheck(){
        if (!User_Login.getText().toString().equals("")&&!User_Pwd.getText().toString().equals("")){
            LostHttp();
        }else{
            Toast.makeText(this, "请输入账号或者密码", Toast.LENGTH_SHORT).show();
        }

    }
    public void putDateForFragmentMe(){
        final Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra(LOGIN_USER,user);
        intent.putExtra(LOGIN_NAME,name);
        startActivityForResult(intent,2);
        setResult(2,intent);
        startActivity(intent);
    }
//    保存数据
    private void SaveText(){
//        获取sharedPreferences对象
        SharedPreferences preferences = getSharedPreferences(LOGINDATE,MODE_PRIVATE);
//        传值
        String ace = preferences.getString(USERNAME,"");
        String ace1= preferences.getString(PWD,"");
        User_Login.setText(ace);
        User_Pwd.  setText(ace1);
    }
//    拿到数据
    private void GetText(){
        String login_user = User_Login.getText().toString();
        String pwd_user   = User_Pwd.getText().toString();
//        获取sharedPreferences对象
        SharedPreferences preferences = getSharedPreferences(LOGINDATE,MODE_PRIVATE);
        //获取editor对象
        SharedPreferences.Editor editor = preferences.edit();//获取编辑器
        // 传值
        editor.putString(USERNAME,login_user);
        editor.putString(PWD,pwd_user);
        editor.apply();//提交修改
    }
    //    登录成功后跳转到主界面
    private void ShowSuccessView(){
        //另开一条UI线程
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GetText();
                new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("登录成功")
                        .setContentText("开始聊天吧")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                putDateForFragmentMe();
                                SaveText();
                            }
                        }).show();
            }
        });
    }
    //    登录失败
    private void ShowUnSuccessView(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("登录失败")
                        .setContentText("请重新输入账号密码")
                        .show();
                User_Login.setText("");
                User_Pwd.setText("");
            }
        });
    }

    public void HttpLostDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("网络错误")
                        .setContentText("请检查网络是否开启？")
                        .show();
            }
        });
    }
}
