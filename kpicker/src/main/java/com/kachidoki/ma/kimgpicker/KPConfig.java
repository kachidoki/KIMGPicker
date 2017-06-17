package com.kachidoki.ma.kimgpicker;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;

import com.kachidoki.ma.kimgpicker.Utils.Utils;

import java.io.Serializable;

/**
 * Created by Kachidoki on 2017/6/13.
 */

public class KPConfig {

    /**
     * Whether it needs to be cut
     */
    public boolean needCrop;

    /**
     * Whether to choose more
     */
    public boolean multiSelect;

    /**
     * Remember whether the last selected record (valid only for multiple elections)
     */
    public boolean rememberSelected;

    /**
     * Choose the maximum number of images
     */
    public int maxNum;

    /**
     * Does the first item show the camera
     */
    public boolean needCamera;


    /**
     * theme color need user to set
     */
    public int statusBarColor;
    public int titleBgColor;
    public int navigationColor;
    public int allImageViewColor;

    /**
     * title
     */
    public String title;
    public int titleColor;


    /**
     * okButton text color
     */
    public int btnTextColor;


    public String allImagesText;

    /**
     * photo storage path
     */
    public String cropCacheFolder;
    public String takeImageFile;

    /**
     * Trim output size
     */
    public int aspectX;
    public int aspectY;
    public int outputX;
    public int outputY;

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

            title = context.getString(R.string.pick_img);
            titleColor = Color.WHITE;
            statusBarColor = context.getResources().getColor(R.color.BlueDark);
            titleBgColor = context.getResources().getColor(R.color.BlueTight);
            allImageViewColor = context.getResources().getColor(R.color.ABlueTight);


            btnTextColor = Color.WHITE;

            allImagesText = context.getString(R.string.all_img);


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

        public Builder cropCacheFolder(String cropCacheFolder) {
            this.cropCacheFolder = cropCacheFolder;
            return this;
        }

        public Builder takeImageFile(String takeImageFile) {
            this.takeImageFile = takeImageFile;
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
