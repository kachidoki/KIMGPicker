package com.kachidoki.ma.kimgpicker;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

    public void clearSelected(){
        dataHolder.clearSelectedImages();
    }

    //goPick

    public static void GoPick(Context context,KPConfig config,ImageLoader loader){
        GoPick(context,config,loader,false);
    }

    public static void GoPick(Context context,KPConfig config,ImageLoader loader,boolean isuselast){
        getInstance().getDataHolder().config=config;
        getInstance().imageLoader=loader;
        Intent intent = new Intent(context, ImagePickActivity.class);
        intent.putExtra(Code.EXTRA_USE_LASTSELECTED,isuselast);
        context.startActivity(intent);
    }

    public static void GoCrop(Activity context, String imagePath) {
        File outFile = Utils.createFile(KIMGPicker.getInstance().dataHolder.config.cropCacheFolder,"",".jpg");
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




}
