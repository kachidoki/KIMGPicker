package com.kachidoki.ma.kimgpicker;

import com.kachidoki.ma.kimgpicker.Bean.ImgItem;
import com.kachidoki.ma.kimgpicker.Loader.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kachidoki on 2017/6/12.
 */

public class KImgPicker {


    private boolean multiMode = true;    //图片选择模式
    private int selectLimit = 9;         //最大选择图片数量
    private boolean crop = true;         //裁剪
    private boolean showCamera = true;   //显示相机
    private boolean isSaveRectangle = false;  //裁剪后的图片是否是矩形，否者跟随裁剪框的形状
    private int outPutX = 800;           //裁剪保存宽度
    private int outPutY = 800;           //裁剪保存高度
    private int focusWidth = 280;         //焦点框的宽度
    private int focusHeight = 280;        //焦点框的高度
    private ImageLoader imageLoader;     //图片加载器
    private File cropCacheFolder;
    private File takeImageFile;

    //裁剪形状

    private List<ImgItem> SelectedImages = new ArrayList<>();
    private List<OnImageSelectedListener> mImageSelectedListeners;


    public boolean isMultiMode() {
        return multiMode;
    }

    public void setMultiMode(boolean multiMode) {
        this.multiMode = multiMode;
    }

    public int getSelectLimit() {
        return selectLimit;
    }

    public void setSelectLimit(int selectLimit) {
        this.selectLimit = selectLimit;
    }

    public boolean isCrop() {
        return crop;
    }

    public void setCrop(boolean crop) {
        this.crop = crop;
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public boolean isSaveRectangle() {
        return isSaveRectangle;
    }

    public void setSaveRectangle(boolean saveRectangle) {
        isSaveRectangle = saveRectangle;
    }

    public int getOutPutX() {
        return outPutX;
    }

    public void setOutPutX(int outPutX) {
        this.outPutX = outPutX;
    }

    public int getOutPutY() {
        return outPutY;
    }

    public void setOutPutY(int outPutY) {
        this.outPutY = outPutY;
    }

    public int getFocusWidth() {
        return focusWidth;
    }

    public void setFocusWidth(int focusWidth) {
        this.focusWidth = focusWidth;
    }

    public int getFocusHeight() {
        return focusHeight;
    }

    public void setFocusHeight(int focusHeight) {
        this.focusHeight = focusHeight;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public File getCropCacheFolder() {
        return cropCacheFolder;
    }

    public void setCropCacheFolder(File cropCacheFolder) {
        this.cropCacheFolder = cropCacheFolder;
    }

    public File getTakeImageFile() {
        return takeImageFile;
    }

    public void setTakeImageFile(File takeImageFile) {
        this.takeImageFile = takeImageFile;
    }


    public List<ImgItem> getSelectedImages() {
        return SelectedImages;
    }

    public void setSelectedImages(List<ImgItem> selectedImages) {
        SelectedImages = selectedImages;
    }

    public void addSelectedImageItem(int position, ImgItem item, boolean isAdd) {
        if (isAdd) SelectedImages.add(item);
        else SelectedImages.remove(item);
        notifyImageSelectedChanged(position, item, isAdd);
    }

    public void setSelectedImages(ArrayList<ImgItem> selectedImages) {
        if (selectedImages == null) {
            return;
        }
        this.SelectedImages = selectedImages;
    }

    public boolean isSelect(ImgItem item) {
        return SelectedImages.contains(item);
    }

    public int getSelectImageCount() {
        if (SelectedImages == null) {
            return 0;
        }
        return SelectedImages.size();
    }

    public void clear() {
        if (mImageSelectedListeners != null) {
            mImageSelectedListeners.clear();
            mImageSelectedListeners = null;
        }
//        if (mImageFolders != null) {
//            mImageFolders.clear();
//            mImageFolders = null;
//        }
        if (SelectedImages != null) {
            SelectedImages.clear();
        }
//        mCurrentImageFolderPosition = 0;
    }


    private KImgPicker(){}
    public static KImgPicker getInstance(){
        return Holder.sInstance;
    }

    private static class Holder{
        private static final KImgPicker sInstance = new KImgPicker();
    }

    /**
     * 图片选中的监听
     */

    public interface OnImageSelectedListener {
        void onImageSelected(int position, ImgItem item, boolean isAdd);
    }

    public void addOnImageSelectedListener(OnImageSelectedListener l) {
        if (mImageSelectedListeners == null) mImageSelectedListeners = new ArrayList<>();
        mImageSelectedListeners.add(l);
    }

    public void removeOnImageSelectedListener(OnImageSelectedListener l) {
        if (mImageSelectedListeners == null) return;
        mImageSelectedListeners.remove(l);
    }



    private void notifyImageSelectedChanged(int position, ImgItem item, boolean isAdd) {
        if (mImageSelectedListeners == null) return;
        for (OnImageSelectedListener l : mImageSelectedListeners) {
            l.onImageSelected(position, item, isAdd);
        }
    }
}
