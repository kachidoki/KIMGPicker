package com.kachidoki.ma.kimgpicker.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kachidoki.ma.kimgpicker.KIMGPicker;
import com.kachidoki.ma.kimgpicker.KPConfig;
import com.kachidoki.ma.kimgpicker.Utils.ActivityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Kachidoki on 2017/6/16.
 */

public abstract class ImageConfigActivity extends AppCompatActivity {

    protected KIMGPicker picker;
    protected KPConfig config;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        picker = KIMGPicker.getInstance();
        config = picker.getDataHolder().config;
    }

    protected void exit(){
        KIMGPicker.getInstance().clearCache();
        finish();
    }

    protected void setBackResult(ArrayList<String> result){
        Intent backIntent = new Intent();

        // compressor and in UI
        if (config.needCompressor&&picker.getCompressor()!=null){
            ArrayList<String> comRes = new ArrayList<>();
            for (String imgpath:result){
                try {
                    Log.e("Compressor","Compressor is work");
                    comRes.add(
                            picker.getCompressor().compressToFile(new File(imgpath)).getAbsolutePath()
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                    ActivityUtils.showToast(e.getMessage(),this);
                }
            }
            backIntent.putStringArrayListExtra(KIMGPicker.RESULT,comRes);
        }else {
            // nomal
            backIntent.putStringArrayListExtra(KIMGPicker.RESULT,result);
        }

        setResult(RESULT_OK,backIntent);
    }
}
