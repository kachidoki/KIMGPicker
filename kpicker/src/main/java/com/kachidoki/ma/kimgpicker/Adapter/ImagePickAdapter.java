package com.kachidoki.ma.kimgpicker.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.kachidoki.ma.kimgpicker.Bean.ImgItem;
import com.kachidoki.ma.kimgpicker.KIMGPicker;
import com.kachidoki.ma.kimgpicker.R;
import com.kachidoki.ma.kimgpicker.Utils.ActivityUtils;
import com.kachidoki.ma.kimgpicker.Utils.Utils;
import com.kachidoki.ma.kimgpicker.Widget.ColorCheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kachidoki on 2017/6/13.
 */

public class ImagePickAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int ITEM_TYPE_CAMERA = 0;  //first is camera
    private static final int ITEM_TYPE_NORMAL = 1;  //first is not camera

    private KIMGPicker picker;
    private Context context;
    private OnPickItemListener listener;
    private List<ImgItem> images;
    private List<ImgItem> mSelectedImages;
    private boolean isShowCamera;
    private int mImageSize;               //item size

    public void setOnItemClickListener(OnPickItemListener listener) {
        this.listener = listener;
    }

    public interface OnPickItemListener{

        void onImageClick(ImgItem imageItem, int position);

        void onImageSelected(ImgItem imageItem, int position,boolean isAdd);

        void onCameraClick();
    }

    public void setImgData(List<ImgItem> images) {
        if (images == null) this.images = new ArrayList<>();
        else this.images = images;
        notifyDataSetChanged();
    }

    public List<ImgItem> getImgData(){
        return images;
    }

    public ImgItem getItem(int position) {
        if (isShowCamera) {
            if (position == 0) return null;
            return images.get(position - 1);
        } else {
            return images.get(position);
        }
    }

    public void SelectChange(){
        notifyDataSetChanged();
    }

    public ImagePickAdapter(Context context) {
        this.context = context;
        images = new ArrayList<>();
        mImageSize = Utils.getImageItemWidth(context);
        picker = KIMGPicker.getInstance();
        isShowCamera = picker.getDataHolder().getConfig().needCamera;
        mSelectedImages = picker.getDataHolder().getSelectedImages();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CAMERA){
            return new ImagePickAdapter.CameraViewHolder(LayoutInflater.from(context).inflate(R.layout.item_camera,parent,false));
        }
        return new ImagePickAdapter.ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_imglist,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImagePickAdapter.CameraViewHolder){
            ((ImagePickAdapter.CameraViewHolder)holder).setCamera();
        }else if (holder instanceof ImagePickAdapter.ImageViewHolder){
            ((ImagePickAdapter.ImageViewHolder)holder).setData(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowCamera) return position == 0 ? ITEM_TYPE_CAMERA : ITEM_TYPE_NORMAL;
        return ITEM_TYPE_NORMAL;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return isShowCamera ? images.size() + 1 : images.size();
    }




    private class ImageViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        ImageView ivThumb;
        View mask;
        ColorCheckBox cbCheck;

        public ImageViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            ivThumb = (ImageView) itemView.findViewById(R.id.iv_thumb);
            mask = itemView.findViewById(R.id.mask);
            cbCheck = (ColorCheckBox) itemView.findViewById(R.id.cb_check);
            //set color with config
            cbCheck.setBorderColor(picker.getDataHolder().config.titleBgColor);
            cbCheck.setCheckedColor(picker.getDataHolder().config.statusBarColor);

            itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize));
        }

        public void setData(final int position){
            final ImgItem imageItem = getItem(position);
            ivThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onImageClick(imageItem, position);
                }
            });
            cbCheck.setUserListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectLimit = picker.getDataHolder().getConfig().maxNum;
                    if (cbCheck.isChecked()) {
                        // when click, the item is not choose
                        if (mSelectedImages.size()<selectLimit){
                            // can choose
                            mSelectedImages.add(imageItem);
                            if (listener != null) listener.onImageSelected(imageItem,position,true);
                            mask.setVisibility(View.VISIBLE);
                        }else {
                            // more than limit, keep num
                            ActivityUtils.showToast(context.getString(R.string.select_limit,selectLimit),context);
                            cbCheck.setChecked(false);
                            mask.setVisibility(View.GONE);
                        }
                    } else {
                        // when click, the item is choose
                        mSelectedImages.remove(imageItem);
                        if (listener != null) listener.onImageSelected(imageItem,position,false);
                        mask.setVisibility(View.GONE);
                    }
                }
            });
            // show or not show the checkbox with multiSelect
            if (picker.getDataHolder().config.multiSelect) {
                cbCheck.setVisibility(View.VISIBLE);
                boolean selected = mSelectedImages.contains(imageItem);
                if (selected) {
                    mask.setVisibility(View.VISIBLE);
                    cbCheck.setChecked(true);
                } else {
                    mask.setVisibility(View.GONE);
                    cbCheck.setChecked(false);
                }
            } else {
                cbCheck.setVisibility(View.GONE);
            }
            // use imageLoader display the picture
            picker.getImageLoader().displayImage(context, imageItem.getPath(), ivThumb, mImageSize, mImageSize);
        }

    }

    private class CameraViewHolder extends RecyclerView.ViewHolder {

        View mItemView;

        public CameraViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
        }

        public void setCamera(){
            // set size
            mItemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize));
            mItemView.setTag(null);
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onCameraClick();
                }
            });
        }
    }
}
