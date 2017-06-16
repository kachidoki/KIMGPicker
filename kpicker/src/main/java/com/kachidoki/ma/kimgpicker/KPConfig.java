package com.kachidoki.ma.kimgpicker;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;

import com.kachidoki.ma.kimgpicker.Loader.ImageLoader;
import com.kachidoki.ma.kimgpicker.Utils.Utils;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Kachidoki on 2017/6/13.
 */

public class KPConfig {

    /**
     * 是否需要裁剪
     */
    public boolean needCrop;

    /**
     * 是否多选
     */
    public boolean multiSelect = false;

    /**
     * 是否记住上次的选中记录(只对多选有效)
     */
    public boolean rememberSelected = true;

    /**
     * 最多选择图片数
     */
    public int maxNum = 9;

    /**
     * 第一个item是否显示相机
     */
    public boolean needCamera;

    public int statusBarColor = -1;


    /**
     * 标题
     */
    public String title;

    /**
     * 标题颜色
     */
    public int titleColor;

    /**
     * titlebar背景色
     */
    public int titleBgColor;

    public int navigationColor;

    public int allImageViewColor;

    /**
     * 确定按钮文字颜色
     */
    public int btnTextColor;


    public String allImagesText;

    /**
     * 拍照存储路径
     */
    public String cropCacheFolder;
    public String takeImageFile;

    /**
     * 裁剪输出大小
     */
    public int aspectX = 1;
    public int aspectY = 1;
    public int outputX = 400;
    public int outputY = 400;

    public KPConfig(Builder builder) {
        this.needCrop = builder.needCrop;
        this.multiSelect = builder.multiSelect;
        this.rememberSelected = builder.rememberSelected;
        this.maxNum = builder.maxNum;
        this.needCamera = builder.needCamera;
        this.statusBarColor = builder.statusBarColor;
        this.title = builder.title;
        this.titleBgColor = builder.titleBgColor;
        this.navigationColor = builder.navigationColor;
        this.allImageViewColor = builder.allImageViewColor;
        this.titleColor = builder.titleColor;
        this.btnTextColor = builder.btnTextColor;
        this.allImagesText = builder.allImagesText;
        this.cropCacheFolder = builder.cropCacheFolder;
        this.takeImageFile = builder.takeImageFile;
        this.aspectX = builder.aspectX;
        this.aspectY = builder.aspectY;
        this.outputX = builder.outputX;
        this.outputY = builder.outputY;
    }

    public static class Builder implements Serializable {

        private boolean needCrop = false;
        private boolean multiSelect = true;
        private boolean rememberSelected = false;
        private int maxNum = 9;
        private boolean needCamera = true;


        private int statusBarColor = -1;
        private int titleBgColor = -1;

        private int navigationColor = -1;

        private int allImageViewColor = -1;


        private String title;
        private int titleColor;


        private int btnTextColor;

        private String allImagesText;

        private String cropCacheFolder;
        private String takeImageFile;

        private int aspectX = 1;
        private int aspectY = 1;
        private int outputX = 400;
        private int outputY = 400;

        public Builder(Context context) {

            if (Utils.existSDCard()){
                takeImageFile = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/DCIM/camera/";
                // /sdcard/Android/data/<application package>/cache
                cropCacheFolder = context.getExternalCacheDir().getPath();
            } else{
                takeImageFile = Environment.getDataDirectory().getAbsolutePath();
                // /data/data/<application package>/cache
                cropCacheFolder = context.getCacheDir().getPath();
            }

            title = "选择图片";
            titleColor = Color.WHITE;
            statusBarColor = context.getResources().getColor(R.color.BlueDark);
            titleBgColor = context.getResources().getColor(R.color.BlueTight);
            allImageViewColor = context.getResources().getColor(R.color.ABlueTight
            );


            btnTextColor = Color.WHITE;

            allImagesText = "全部图片";


        }

        public Builder needCrop(boolean needCrop) {
            this.needCrop = needCrop;
            return this;
        }

        public Builder multiSelect(boolean multiSelect) {
            this.multiSelect = multiSelect;
            return this;
        }

        public Builder rememberSelected(boolean rememberSelected){
            this.rememberSelected = rememberSelected;
            return this;
        }

        public Builder maxNum(int maxNum) {
            this.maxNum = maxNum;
            return this;
        }

        public Builder needCamera(boolean needCamera) {
            this.needCamera = needCamera;
            return this;
        }

        public Builder statusBarColor(int statusBarColor) {
            this.statusBarColor = statusBarColor;
            return this;
        }

        public Builder titleBgColor(int titleBgColor) {
            this.titleBgColor = titleBgColor;
            return this;
        }

        public Builder navigationColor(int navigationColor) {
            this.navigationColor = navigationColor;
            return this;
        }

        public Builder allImageViewColor(int allImageViewColor) {
            this.allImageViewColor = allImageViewColor;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder titleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }




        public Builder btnTextColor(int btnTextColor) {
            this.btnTextColor = btnTextColor;
            return this;
        }


        public Builder allImagesText(String allImagesText) {
            this.allImagesText = allImagesText;
            return this;
        }


        public Builder cropSize(int aspectX, int aspectY, int outputX, int outputY) {
            this.aspectX = aspectX;
            this.aspectY = aspectY;
            this.outputX = outputX;
            this.outputY = outputY;
            return this;
        }

        public KPConfig build() {
            return new KPConfig(this);
        }
    }
}
