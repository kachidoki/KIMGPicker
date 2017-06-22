package com.kachidoki.ma.kimgpicker;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.kachidoki.ma.kimgpicker.Loader.ImageLoader;
import com.kachidoki.ma.kimgpicker.UI.BlankActivity;
import com.kachidoki.ma.kimgpicker.UI.ImagePickActivity;
import com.kachidoki.ma.kimgpicker.Utils.Code;


/**
 * Created by Kachidoki on 2017/6/13.
 */

public class KIMGPicker {

    public static final String RESULT = "KP_RESULT";

    private KPData dataHolder;
    private ImageLoader imageLoader;
    private KPCompressor compressor;

    public static KIMGPicker getInstance(){
        return INSTANCE.INSTANCE;
    }

    private static class INSTANCE{
        private static final KIMGPicker INSTANCE = new KIMGPicker();
    }

    public KPCompressor getCompressor() {
        return compressor;
    }

    public KPData getDataHolder() {
        if (dataHolder==null) dataHolder = KPData.getInstance();
        return dataHolder;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void clearData(){
        if (dataHolder==null) return;
        dataHolder.clearCache();
        dataHolder.clearSelectedImages();
    }

    public void clearCache(){
        if (dataHolder==null) return;
        dataHolder.clearCache();
    }

    public void clearSelected(){
        if (dataHolder==null) return;
        dataHolder.clearSelectedImages();
    }

    // goPick
    public static void GoPick(Activity context,ImageLoader loader,int request){
        if (getInstance().dataHolder==null){
            getInstance().dataHolder.config = new KPConfig.Builder(context).build();
        }
        GoPick(context,getInstance().dataHolder.config,loader,request);
    }

    public static void GoPick(Activity context,KPConfig config,ImageLoader loader,int request){
        GoPick(context,config,loader,null,request);
    }

    public static void GoPick(Activity context,KPConfig config,ImageLoader loader,KPCompressor compressor,int request){
        getInstance().getDataHolder().config = config;
        getInstance().imageLoader = loader;
        getInstance().compressor = compressor;
        Intent intent = new Intent(context, ImagePickActivity.class);
        context.startActivityForResult(intent,request);
    }



    public static void GoCrop(Activity context,String imagePath,int request){
        if (getInstance().dataHolder==null){
            getInstance().getDataHolder().config = new KPConfig.Builder(context).build();
        }
        GoCrop(context,getInstance().dataHolder.config,null,imagePath,request);
    }

    public static void GoCrop(Activity context,KPConfig config,KPCompressor compressor,String imagePath,int request){
        getInstance().getDataHolder().config = config;
        getInstance().compressor = compressor;
        Intent intent = new Intent(context, BlankActivity.class);
        intent.putExtra(Code.EXTRA_IMAGE_PATH,imagePath);
        intent.putExtra(Code.EXTRA_WHICH_REQUEST,Code.REQUEST_CODE_CROP);
        context.startActivityForResult(intent,request);
    }

    public static void GoTake(Activity context,int request){
        if (getInstance().dataHolder==null){
            getInstance().getDataHolder().config = new KPConfig.Builder(context).build();
        }
        GoTake(context,getInstance().dataHolder.config,null,request);
    }

    public static void GoTake(Activity context,KPConfig config,KPCompressor compressor,int request){
        getInstance().getDataHolder().config = config;
        getInstance().compressor = compressor;
        Intent intent = new Intent(context, BlankActivity.class);
        intent.putExtra(Code.EXTRA_WHICH_REQUEST,Code.REQUEST_CODE_TAKE);
        context.startActivityForResult(intent,request);
    }



}
