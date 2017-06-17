package com.kachidoki.ma.kimgpicker;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.kachidoki.ma.kimgpicker.Loader.ImageLoader;
import com.kachidoki.ma.kimgpicker.UI.ImagePickActivity;
import com.kachidoki.ma.kimgpicker.Utils.Code;
import com.kachidoki.ma.kimgpicker.Utils.Utils;

import java.io.File;

/**
 * Created by Kachidoki on 2017/6/13.
 */

public class KIMGPicker {

    public static final String RESULT = "KP_RESULE";

    private KPData dataHolder;
    private ImageLoader imageLoader;

    public static KIMGPicker getInstance(){
        return INSTANCE.INSTANCE;
    }

    private static class INSTANCE{
        private static final KIMGPicker INSTANCE = new KIMGPicker();
    }

    public KPData getDataHolder() {
        if (dataHolder==null) dataHolder = KPData.getInstance();
        return dataHolder;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void clearData(){
        dataHolder.clearCache();
        dataHolder.clearSelectedImages();
    }

    public void clearCache(){
        dataHolder.clearCache();
    }

    public void clearSelected(){
        dataHolder.clearSelectedImages();
    }

    //goPick

    public static void GoPick(Activity context,KPConfig config,ImageLoader loader,int request){
        GoPick(context,config,loader,request,false);
    }

    public static void GoPick(Activity context,KPConfig config,ImageLoader loader,int request,boolean isuselast){
        getInstance().getDataHolder().config=config;
        getInstance().imageLoader=loader;
        Intent intent = new Intent(context, ImagePickActivity.class);
        intent.putExtra(Code.EXTRA_USE_LASTSELECTED,isuselast);
        context.startActivityForResult(intent,request);
    }

    public static void GoCrop(Activity context, String imagePath) {
        File outFile = Utils.createFile(new File(KIMGPicker.getInstance().dataHolder.config.cropCacheFolder),"crop_",".jpg");
        //cache the file to return
        getInstance().dataHolder.setCacheTakeFile(outFile);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Utils.getImageContentUri(context,new File(imagePath)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", KIMGPicker.getInstance().getDataHolder().config.aspectX);
        intent.putExtra("aspectY", KIMGPicker.getInstance().getDataHolder().config.aspectY);
        intent.putExtra("outputX", KIMGPicker.getInstance().getDataHolder().config.outputX);
        intent.putExtra("outputY", KIMGPicker.getInstance().getDataHolder().config.outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        context.startActivityForResult(intent, Code.REQUEST_CODE_CROP);
    }


    public static void GoTake(Activity context,String takeImagePath){
        File takeImageFile = new File(takeImagePath);
        takeImageFile = Utils.createFile(takeImageFile, "IMG_", ".jpg");
        getInstance().dataHolder.setCacheTakeFile(takeImageFile);
        Utils.takePicture(context,Code.REQUEST_CODE_TAKE,takeImageFile);
    }



}
