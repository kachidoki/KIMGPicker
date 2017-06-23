package com.kachidoki.ma.kimgpicker.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.kachidoki.ma.kimgpicker.KIMGPicker;
import com.kachidoki.ma.kimgpicker.R;
import com.kachidoki.ma.kimgpicker.Utils.ActivityUtils;
import com.kachidoki.ma.kimgpicker.Utils.Code;
import com.kachidoki.ma.kimgpicker.Utils.Utils;


/**
 * Created by Kachidoki on 2017/6/21.
 */

public class BlankActivity extends ImageConfigActivity{

    private String imagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.getIntExtra(Code.EXTRA_WHICH_REQUEST,0)==Code.REQUEST_CODE_TAKE){
            requestTake();
        }
        if (intent.getIntExtra(Code.EXTRA_WHICH_REQUEST,0)==Code.REQUEST_CODE_CROP){
            imagePath = intent.getStringExtra(Code.EXTRA_IMAGE_PATH);
            requestCrop();
        }

    }

    private void requestCrop(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (ActivityUtils.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,this)) {
                Utils.GoCrop(this,picker,imagePath,Code.REQUEST_CODE_CROP);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Code.REQUEST_PERMISSION_STORAGE);
            }
        }else{
            Utils.GoCrop(this,picker,imagePath,Code.REQUEST_CODE_CROP);
        }
    }


    public void requestTake(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (!ActivityUtils.checkPermission(Manifest.permission.CAMERA,this)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Code.REQUEST_PERMISSION_CAMERA);
            } else {
                Utils.GoTake(this,picker,Code.REQUEST_CODE_TAKE);
            }
        }else {
            Utils.GoTake(this,picker,Code.REQUEST_CODE_TAKE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Code.REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.GoCrop(this,picker,imagePath,Code.REQUEST_CODE_CROP);
            } else {
                ActivityUtils.showToast(getString(R.string.img_permissions_limit),this);
            }
        } else if (requestCode == Code.REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.GoTake(this,picker,Code.REQUEST_CODE_TAKE);
            } else {
                ActivityUtils.showToast(getString(R.string.take_permissions_limit),this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Code.REQUEST_CODE_TAKE&&resultCode == RESULT_OK){
            setBackResult(picker.getDataHolder().getCacheResult());
        }else if (requestCode == Code.REQUEST_CODE_CROP&&resultCode == RESULT_OK){
            setBackResult(picker.getDataHolder().getCacheResult());
        }
        exit();
    }
}
