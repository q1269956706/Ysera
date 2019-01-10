package com.example.evilsay.wechatson.Utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import com.example.evilsay.wechatson.FreeRegister;

import java.lang.reflect.Array;
import java.util.Arrays;

import cn.pedant.SweetAlert.SweetAlertDialog;

//动态验证存储权限 Android6.0以上的权限申请
public class PowerManage {
    //定义的一个返回码
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            //写
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //读45
            Manifest.permission.READ_EXTERNAL_STORAGE,
            //通讯录
            Manifest.permission.READ_CONTACTS,
            //通话记录
            Manifest.permission.READ_CALL_LOG
    };

    public static void verifyStoragePermissions(final Activity activity){
//        检查是否有写入权限
        int permission = ActivityCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED){
            // 我们没有权限提示用户并申请权限
            ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            // 提示用户手机更新状态
            new SweetAlertDialog(activity,SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("欢迎来到WeChatSon请注册")
                    .setContentText("<第一次安装是没有聊天记录的哦>")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            activity.startActivity(new Intent(activity, FreeRegister.class));
                        }
                    })
                    .show();

        }
    }
//    Android5.0系统第一次启动检查
    public static void CheckOne(final Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("share",Context.MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun",true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFirstRun){
            new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("欢迎来到WeChatSon请注册")
                    .setContentText("<第一次安装是没有聊天记录的哦>")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            context.startActivity(new Intent(context, FreeRegister.class));
                        }
                    })
                    .show();
            editor.putBoolean("isFirstRun",false);
            editor.apply();
        }
    }
//    app自毁
    public static void getUninstallAppIntent(Activity activity,String packageInfo) {
        Uri uri = Uri.fromParts("package", packageInfo, null);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        activity.startActivity(intent);
    }
//    权限申请失败
}
