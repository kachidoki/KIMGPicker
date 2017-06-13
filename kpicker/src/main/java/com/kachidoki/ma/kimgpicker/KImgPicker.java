package com.kachidoki.ma.kimgpicker;


import android.content.Context;
import android.content.Intent;

import com.kachidoki.ma.kimgpicker.Loader.ImageLoader;
import com.kachidoki.ma.kimgpicker.UI.ImagePickActivity;

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



    //goPick

    public static void GoPick(Context context,KPConfig config,ImageLoader loader){
        getInstance().getDataHolder().config=config;
        getInstance().imageLoader=loader;
        context.startActivity(new Intent(context, ImagePickActivity.class));
    }
}
