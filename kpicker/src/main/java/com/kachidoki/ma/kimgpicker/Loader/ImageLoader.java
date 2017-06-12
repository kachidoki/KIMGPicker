package com.kachidoki.ma.kimgpicker.Loader;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by Kachidoki on 2017/6/12.
 */

public interface ImageLoader {

    void displayImage(Context context, String path, ImageView imageView, int width, int height);

}
