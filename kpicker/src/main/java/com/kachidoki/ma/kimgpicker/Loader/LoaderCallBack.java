package com.kachidoki.ma.kimgpicker.Loader;

import com.kachidoki.ma.kimgpicker.Bean.ImgFolder;

import java.util.List;

/**
 * Created by Kachidoki on 2017/6/11.
 */

public interface LoaderCallBack {
    void ImgsLoaded(List<ImgFolder> folders);
}
