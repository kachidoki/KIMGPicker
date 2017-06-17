package com.kachidoki.ma.kimgpicker;

import com.kachidoki.ma.kimgpicker.Bean.ImgItem;

import java.io.File;
import java.util.ArrayList;
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
    public List<ImgItem> previewCache = new ArrayList<>();
    public File cacheTakeFile;

    public void setPreCache(List<ImgItem> list){
        if (list!=null){
            previewCache = list;
        }
    }

    public void clearCache(){
        if (previewCache!=null) previewCache.clear();
        if (cacheTakeFile!=null) cacheTakeFile=null;
    }

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

    public void newSelect(){
        if (selectedImg==null){
            selectedImg = new ArrayList<>();
        }else {
            selectedImg.clear();
        }
    }

    public void clearSelectedImages() {
        if (selectedImg != null) selectedImg.clear();
    }

    public void setCacheTakeFile(File cache){
        if (cache!=null){
            cacheTakeFile = cache;
        }
    }

    public ArrayList<String> getCacheResult(){
        ArrayList<String> result = new ArrayList<>();
        result.add(cacheTakeFile.getAbsolutePath());
        return result;
    }

    public ArrayList<String> getSelectedResult(){
        ArrayList<String> result = new ArrayList<>();
        for (ImgItem img:selectedImg){
            result.add(img.getPath());
        }
        return result;
    }

    public ArrayList<String> getSelectedSingleResult(ImgItem imgItem){
        ArrayList<String> result = new ArrayList<>();
        result.add(imgItem.getPath());
        return result;
    }

}
