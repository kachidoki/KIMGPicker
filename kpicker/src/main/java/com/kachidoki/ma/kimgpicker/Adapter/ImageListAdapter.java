package com.kachidoki.ma.kimgpicker.Adapter;

import android.Manifest;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;


import com.kachidoki.ma.kimgpicker.Bean.ImgItem;
import com.kachidoki.ma.kimgpicker.KImgPicker;
import com.kachidoki.ma.kimgpicker.R;
import com.kachidoki.ma.kimgpicker.UI.ImgListActivity;
import com.kachidoki.ma.kimgpicker.Utils.ActivityUtils;
import com.kachidoki.ma.kimgpicker.Utils.Code;
import com.kachidoki.ma.kimgpicker.Utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class ImageListAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int ITEM_TYPE_CAMERA = 0;  //第一个条目是相机
    private static final int ITEM_TYPE_NORMAL = 1;  //第一个条目不是相机
    private KImgPicker picker;
    private Context context;
    private List<ImgItem> images;       //当前需要显示的所有的图片数据
    private List<ImgItem> mSelectedImages; //全局保存的已经选中的图片数据
    private boolean isShowCamera;         //是否显示拍照按钮
    private int mImageSize;               //每个条目的大小
    private OnImageItemClickListener listener;   //图片被点击的监听

    public void setOnItemClickListener(OnImageItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnImageItemClickListener {
        void onImageItemClick(View view, ImgItem imageItem, int position);
    }

    public void setImgData(List<ImgItem> images) {
        if (images == null) images = new ArrayList<>();
        else this.images = images;
        notifyDataSetChanged();
    }

    public ImgItem getItem(int position) {
        if (isShowCamera) {
            if (position == 0) return null;
            return images.get(position - 1);
        } else {
            return images.get(position);
        }
    }

    public ImageListAdapter(Context context) {
        this.context = context;
        images=new ArrayList<>();
        mImageSize = Utils.getImageItemWidth(context);
        picker = KImgPicker.getInstance();
        isShowCamera = picker.isShowCamera();
        mSelectedImages = picker.getSelectedImages();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CAMERA){
            return new CameraViewHolder(LayoutInflater.from(context).inflate(R.layout.item_camera,parent,false));
        }
        return new ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_imglist,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof CameraViewHolder){
            ((CameraViewHolder)holder).setCamera();
        }else if (holder instanceof ImageViewHolder){
            ((ImageViewHolder)holder).setData(position);
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





    private class ImageViewHolder extends ViewHolder {

        View rootView;
        ImageView ivThumb;
        View mask;
        CheckBox cbCheck;

        public ImageViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            ivThumb = (ImageView) itemView.findViewById(R.id.iv_thumb);
            mask = itemView.findViewById(R.id.mask);
            cbCheck = (CheckBox) itemView.findViewById(R.id.cb_check);
            itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize));
        }

        public void setData(final int position){
            final ImgItem imageItem = getItem(position);
            ivThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onImageItemClick(rootView, imageItem, position);
                }
            });
            cbCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectLimit = picker.getSelectLimit();
                    if (cbCheck.isChecked() && mSelectedImages.size() >= selectLimit) {
                        ActivityUtils.showToast("最多选择"+selectLimit+"张图片",context);
                        cbCheck.setChecked(false);
                        mask.setVisibility(View.GONE);
                    } else {
                        picker.addSelectedImageItem(position, imageItem, cbCheck.isChecked());
                        mask.setVisibility(View.VISIBLE);
                    }
                }
            });
            //根据是否多选，显示或隐藏checkbox
            if (picker.isMultiMode()) {
                cbCheck.setVisibility(View.VISIBLE);
                boolean checked = mSelectedImages.contains(imageItem);
                if (checked) {
                    mask.setVisibility(View.VISIBLE);
                    cbCheck.setChecked(true);
                } else {
                    mask.setVisibility(View.GONE);
                    cbCheck.setChecked(false);
                }
            } else {
                cbCheck.setVisibility(View.GONE);
            }
            picker.getImageLoader().displayImage(context, imageItem.getPath(), ivThumb, mImageSize, mImageSize); //显示图片
        }

    }

    private class CameraViewHolder extends ViewHolder {

        View mItemView;

        public CameraViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
        }

        public void setCamera(){
            mItemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize)); //让图片是个正方形
            mItemView.setTag(null);
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ActivityUtils.checkPermission(Manifest.permission.CAMERA,context)) {
                        ActivityCompat.requestPermissions((ImgListActivity)context, new String[]{Manifest.permission.CAMERA}, Code.REQUEST_PERMISSION_CAMERA);
                    } else {
                        Utils.takePicture((ImgListActivity)context, Code.REQUEST_CODE_TAKE,picker.getTakeImageFile());
                    }
                }
            });
        }
    }
}
