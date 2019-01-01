package com.example.shiran.drhelp.util;


import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.shiran.drhelp.ui.ContactsActivity;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class Permissions extends AppCompatActivity {

    private static final int RC_VIDEO_APP_PERMISSIONS = 111;
    private final String PERMISSION_MESSAGE = "This app need access to " +
            "your CAMERA and MIC to make video calls";
    private final String TOAST_PERMISSION_MESSAGE = "Sorry to inform you that\n" +
            "DrHelp CAN'T run WITHOUT permissions";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERMISSIONS)
    public boolean requestPermissions(Activity activity){
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO};
        if(EasyPermissions.hasPermissions(activity, permissions)){
            return true;
        } else {
            EasyPermissions.requestPermissions(activity, PERMISSION_MESSAGE, RC_VIDEO_APP_PERMISSIONS, permissions);
            if(EasyPermissions.somePermissionDenied(activity, permissions)){
                Log.d("permission", "permission denied");
            }
        }
        Toast.makeText(activity, TOAST_PERMISSION_MESSAGE, Toast.LENGTH_LONG).show();
        return false;
    }
}
