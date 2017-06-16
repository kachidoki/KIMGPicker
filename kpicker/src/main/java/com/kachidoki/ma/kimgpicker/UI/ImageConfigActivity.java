package com.kachidoki.ma.kimgpicker.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kachidoki.ma.kimgpicker.KIMGPicker;
import com.kachidoki.ma.kimgpicker.KPConfig;
import com.kachidoki.ma.kimgpicker.R;

/**
 * Created by Kachidoki on 2017/6/16.
 */

public abstract class ImageConfigActivity extends AppCompatActivity {

    protected KIMGPicker picker;
    protected KPConfig config;

    protected View titleBar;
    protected Button btOk;
    protected ImageView btBack;
    protected TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        picker = KIMGPicker.getInstance();
        config = picker.getDataHolder().config;

        titleBar = findViewById(R.id.TitleBar);
        btOk = (Button) findViewById(R.id.tbConfirm);
        btBack = (ImageView) findViewById(R.id.tbBack);
        title = (TextView) findViewById(R.id.tvTitle);

        //set the theme with the config

    }

    abstract void initView();
}
