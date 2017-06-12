package com.kachidoki.ma.kimgpicker.Utils;

import android.content.Context;

/**
 * Created by Kachidoki on 2017/6/12.
 */

public class ProviderUtil {

    public static String getFileProviderName(Context context){
        return context.getPackageName()+".provider";
    }
}
