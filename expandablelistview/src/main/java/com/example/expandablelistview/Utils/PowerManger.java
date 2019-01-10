package com.example.expandablelistview.Utils;

import android.Manifest;
import android.app.Activity;

import king.bird.tool.PermissionUtils;

public class PowerManger {
    private static final int REQUEST_CODE = 1;
    private static String[] POWER_CHECK = {
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
    };
    public static void Apply(final Activity activity){
        PermissionUtils.checkAndRequestMorePermissions(activity,POWER_CHECK,REQUEST_CODE, new PermissionUtils.PermissionRequestSuccessCallBack() {
            @Override
            public void onHasPermission() {

            }
        });
    }
}
