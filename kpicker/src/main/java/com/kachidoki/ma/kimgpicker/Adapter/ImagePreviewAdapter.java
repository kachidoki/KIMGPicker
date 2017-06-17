package com.kachidoki.ma.kimgpicker.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.kachidoki.ma.kimgpicker.Bean.ImgItem;
import com.kachidoki.ma.kimgpicker.KIMGPicker;
import com.kachidoki.ma.kimgpicker.UI.ImagePreviewActivity;
import com.kachidoki.ma.kimgpicker.Utils.Utils;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Kachidoki on 2017/6/13.
 */

public class ImagePreviewAdapter extends PagerAdapter {

    private int screenWidth;
    private int screenHeight;
    private KIMGPicker imagePicker;
    private List<ImgItem> images;
    private Context context;
    public PhotoClickListener listener;

    public interface PhotoClickListener{
        void OnPhotoClick(ImgItem imgItem);
    }

    public ImagePreviewAdapter(Context context, List<ImgItem> images) {
        this.context = context;
        this.images = images;

        DisplayMetrics dm = Utils.getScreenPix((ImagePreviewActivity)context);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        imagePicker = KIMGPicker.getInstance();
    }

    public void setData(List<ImgItem> images) {
        this.images = images;
    }

    public void setPhotoViewClickListener(PhotoClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        PhotoView photoView = new PhotoView(context);
        ImgItem imageItem = images.get(position);
        imagePicker.getImageLoader().displayImage(context, imageItem.getPath(), photoView, screenWidth, screenHeight);
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (listener != null) listener.OnPhotoClick(images.get(position));
            }
        });
        container.addView(photoView);
        return photoView;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
