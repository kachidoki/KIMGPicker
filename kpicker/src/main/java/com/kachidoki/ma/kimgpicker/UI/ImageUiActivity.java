package com.kachidoki.ma.kimgpicker.UI;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kachidoki.ma.kimgpicker.R;

/**
 * Created by Kachidoki on 2017/6/22.
 */

public abstract class ImageUiActivity extends ImageConfigActivity{

    protected View titleBar;
    protected Button btOk;
    protected ImageView btBack;
    protected TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create contentView
        createInit();

        titleBar = findViewById(R.id.TitleBar);
        btOk = (Button) findViewById(R.id.tbConfirm);
        btBack = (ImageView) findViewById(R.id.tbBack);
        title = (TextView) findViewById(R.id.tvTitle);

        // init other view
        initView();

        //set the theme with the config
        if (Build.VERSION.SDK_INT >= 21) {
            if (config.navigationColor!=-1) getWindow().setNavigationBarColor(config.navigationColor);
            getWindow().setStatusBarColor(config.statusBarColor);
        }
        titleBar.setBackgroundColor(config.titleBgColor);
        title.setTextColor(config.titleColor);
        btOk.setTextColor(config.btnTextColor);
        if (config.multiSelect) {
            btOk.setVisibility(View.VISIBLE);
        } else {
            btOk.setVisibility(View.GONE);
        }

    }

    protected void statusBarColorTransparent(){
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }
    }

    protected void statusBarColorUser(){
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(config.statusBarColor);
        }
    }

    protected void setOkText(){
        if (picker.getDataHolder().getSelectImageCount()>0){
            btOk.setText(getString(R.string.select_complete,picker.getDataHolder().getSelectImageCount()));
            btOk.setEnabled(true);
        }else {
            btOk.setText(getString(R.string.complete));
            btOk.setEnabled(false);
        }
    }

    abstract void createInit();

    abstract void initView();

}
