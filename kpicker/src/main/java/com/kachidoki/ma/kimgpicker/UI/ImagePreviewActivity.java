package com.kachidoki.ma.kimgpicker.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.kachidoki.ma.kimgpicker.Adapter.ImagePreviewAdapter;
import com.kachidoki.ma.kimgpicker.Bean.ImgItem;
import com.kachidoki.ma.kimgpicker.R;
import com.kachidoki.ma.kimgpicker.Utils.ActivityUtils;
import com.kachidoki.ma.kimgpicker.Utils.Code;
import com.kachidoki.ma.kimgpicker.Widget.ColorCheckBox;
import com.kachidoki.ma.kimgpicker.Widget.TouchViewPager;

import java.util.List;

/**
 * Created by Kachidoki on 2017/6/13.
 */

public class ImagePreviewActivity extends ImageUiActivity implements View.OnClickListener, ImagePreviewAdapter.PhotoClickListener {


    private ColorCheckBox ckSelect;

    private TouchViewPager viewPager;
    private ImagePreviewAdapter mAdapter;

    private List<ImgItem> previewImgs;
    private int mCurrentPosition = 0;
    private int maxSelect;
    private List<ImgItem> selectedImages;


    @Override
    void createInit() {
        setContentView(R.layout.activity_img_pre);
        initData();
    }


    @Override
    void initView(){
        viewPager = (TouchViewPager) findViewById(R.id.viewpager);
        btOk.setOnClickListener(this);
        setOkText();
        btBack.setOnClickListener(this);
        ckSelect = (ColorCheckBox) findViewById(R.id.ckSelect);
        ckSelect.setUserListener(this);
        boolean isSelect = picker.getDataHolder().isSelect(previewImgs.get(mCurrentPosition));
        ckSelect.setChecked(isSelect);
        title.setText(getString(R.string.preview_image_count,mCurrentPosition,previewImgs.size()));
        initAdapter();
    }

    private void initData(){
        mCurrentPosition = getIntent().getIntExtra(Code.EXTRA_SELECTED_IMAGE_POSITION, 0);
        selectedImages = picker.getDataHolder().getSelectedImages();
        previewImgs = picker.getDataHolder().previewCache;
        maxSelect = config.maxNum;
    }

    private void initAdapter(){
        mAdapter = new ImagePreviewAdapter(this, previewImgs);
        mAdapter.setPhotoViewClickListener(this);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(mCurrentPosition, false);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentPosition = position;
                boolean isSelect = picker.getDataHolder().isSelect(previewImgs.get(position));
                ckSelect.setChecked(isSelect);
                title.setText(getString(R.string.preview_image_count,mCurrentPosition,previewImgs.size()));
            }
        });
        viewPager.setCurrentItem(mCurrentPosition);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ckSelect){
            //just happen in multiSelect
            if (ckSelect.isChecked()) {
                if (selectedImages.size()<maxSelect){
                    selectedImages.add(previewImgs.get(mCurrentPosition));
                }else {
                    ActivityUtils.showToast(getString(R.string.select_limit,maxSelect),this);
                    ckSelect.setChecked(false);
                }
            } else {
                selectedImages.remove(previewImgs.get(mCurrentPosition));
            }
            setOkText();
        }else if (id == R.id.tbConfirm){
            setResult(RESULT_OK);
            finish();
        }else if (id == R.id.tbBack){
            finish();
        }
    }

    @Override
    public void OnPhotoClick(ImgItem imgItem) {
        if (titleBar.getVisibility()==View.VISIBLE){
            titleBar.setAnimation(AnimationUtils.loadAnimation(this,R.anim.top_out));
            ckSelect.setAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_out));
            ckSelect.setVisibility(View.GONE);
            titleBar.setVisibility(View.GONE);
            statusBarColorTransparent();
        }else {
            titleBar.setAnimation(AnimationUtils.loadAnimation(this,R.anim.top_in));
            ckSelect.setAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in));
            titleBar.setVisibility(View.VISIBLE);
            ckSelect.setVisibility(View.VISIBLE);
            statusBarColorUser();
        }
    }


    public static void GoPreview(Activity context, int position,int request){
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(Code.EXTRA_SELECTED_IMAGE_POSITION, position);
        context.startActivityForResult(intent,request);
    }



}
