package com.kachidoki.ma.kimgpicker;

import com.kachidoki.ma.kimgpicker.Bean.ImgItem;

import java.util.List;

/**
 * Created by Kachidoki on 2017/6/13.
 */

public class KPData {

    public static KPData getInstance(){
        return INSTANCE.INSTANCE;
    }

    private static class INSTANCE{
        private static final KPData INSTANCE = new KPData();
    }

    public KPConfig config;
    public List<ImgItem> selectedImg;

    public KPConfig getConfig() {
        return config;
    }

    public void setConfig(KPConfig config) {
        this.config = config;
    }

    public boolean isSelect(ImgItem item) {
        return selectedImg.contains(item);
    }

    public int getSelectImageCount() {
        if (selectedImg == null) {
            return 0;
        }
        return selectedImg.size();
    }

    public List<ImgItem> getSelectedImages() {
        return selectedImg;
    }


    public void clearSelectedImages() {
        if (selectedImg != null) selectedImg.clear();
    }

}
