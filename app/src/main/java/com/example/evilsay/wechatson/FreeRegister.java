package com.example.evilsay.wechatson;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evilsay.wechatson.Model.LessonResult;
import com.example.evilsay.wechatson.Utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.widget.Toast.LENGTH_SHORT;


public class FreeRegister extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_ADDRESS = "http://123.207.85.214/chat/register.php";
    private static final String TAG = "FreeRegisterActivity";
    public static final String USER_PWD = "USER_PWD";
    public static final String USER_NAME = "USER_NAME";
    protected EditText userEditText, passwordEditText, userNameNew;
    protected TextView cancel;
    private String status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_reg_layout);
        InitView();
    }

    private void InitView() {
        Button regButton = findViewById(R.id.Login_Button);
        cancel           = findViewById(R.id.cancel);
        userEditText     = findViewById(R.id.User_Edit);
        passwordEditText = findViewById(R.id.Password_EditText);
        userNameNew      = findViewById(R.id.user_name_new);
        regButton.setOnClickListener(this);
        cancel   .setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Login_Button:
                RegCheck();
                break;
            case R.id.cancel:
                startActivity(new Intent(FreeRegister.this,LoginActivity.class));
                break;
        }
    }

    //    传值到登录界面

    private void ErrorShow() {
        new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                .setTitleText("注册失败，用户名重复")
                .show();
    }

//    注册失败提示用户重新输入
    private void ShowViewUnSuccess(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ErrorShow();
                userEditText.setText("");
                passwordEditText.setText("");
                userNameNew.setText("");
            }
        });
    }
//    注册成功连接并进行传值到登录界面
    private void ShowViewSuccess(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new SweetAlertDialog(FreeRegister.this,SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("注册成功啦")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                startActivity(new Intent(FreeRegister.this,LoginActivity.class));
                            }
                        })
                        .show();
            }
        });
    }
//    检查字符串是否为空
    private void RegCheck(){
        if (!userNameNew.getText().toString().equals("")&&
                !passwordEditText.getText().toString().equals("")
                && !userEditText.getText().toString().equals("")){
            LostHttp();
        }else{
            Toast.makeText(this, "请输入用户名，密码，账号", LENGTH_SHORT).show();
        }
    }
//    网络检查是否为正常
    private void LostHttp(){
        HttpUtils.HttLostEnqueue(LOG_ADDRESS, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LostHttpShowDialog();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                HttpUtils.httpPostUtils_Reg(userNameNew.getText().toString(),
                        userEditText.getText().toString(),
                        passwordEditText.getText().toString(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //Gson反射类解析
                        LessonResult lessonResult = new Gson().fromJson(response.body().string(),LessonResult.class);
                        CheckLoginCode(lessonResult.getStatus());
                    }
                });
            }
        });
    }
    //原生解析
    private void ResultString(String result){
        LessonResult lessonResult = new LessonResult();
        try {
            JSONObject   object       = new JSONObject(result);
            status  = object.getString("status");
            lessonResult.setStatus(status);
            Log.e(TAG, "JsonToString: "+status );
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JsonToString: "+status );
        }
    }
//    GOSN解析
    private void ResultGson(String result){
        JsonObject statusObj = new JsonParser().parse(result).getAsJsonObject();
        status = statusObj.get("status").getAsString();
    }
    private void LostHttpShowDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new SweetAlertDialog(FreeRegister.this,SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("网络错误,请检查网络是否开启")
                        .show();
            }
        });
    }
    private void CheckLoginCode(String status){
        if (status.equals("注册成功")){
            ShowViewSuccess();
        }else{
            ShowViewUnSuccess();
        }
    }
}
