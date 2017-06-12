package com.kachidoki.ma.kimgpicker.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by Kachidoki on 2017/6/12.
 */

public class ActivityUtils {

    public static boolean checkPermission(@NonNull String permission, Context context) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void showToast(String toastText,Context context) {
        Toast.makeText(context , toastText, Toast.LENGTH_SHORT).show();
    }
}
