package com.kachidoki.ma.kimgpicker.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kachidoki.ma.kimgpicker.Adapter.ImagePreviewAdapter;
import com.kachidoki.ma.kimgpicker.Bean.ImgItem;
import com.kachidoki.ma.kimgpicker.KIMGPicker;
import com.kachidoki.ma.kimgpicker.R;
import com.kachidoki.ma.kimgpicker.Utils.ActivityUtils;
import com.kachidoki.ma.kimgpicker.Utils.Code;
import com.kachidoki.ma.kimgpicker.Widget.TouchViewPager;

import java.util.List;

/**
 * Created by Kachidoki on 2017/6/13.
 */

public class ImagePreviewActivity extends AppCompatActivity implements View.OnClickListener {

    private KIMGPicker picker;

    private View topBar;
    private Button tbOk;
    private ImageView tbBack;
    private TextView tvTitle;
    private CheckBox ckSelect;

    private TouchViewPager viewPager;
    private ImagePreviewAdapter mAdapter;

    private List<ImgItem> previewImgs;
    private int mCurrentPosition = 0;
    private int maxSelect;
    private List<ImgItem> selectedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_pre);
        initData();
        initView();
        initAdapter();
    }

    private void initView(){
        viewPager = (TouchViewPager) findViewById(R.id.viewpager);
        topBar = findViewById(R.id.top_bar);
        tbOk = (Button) findViewById(R.id.tbConfirm);
        tbOk.setOnClickListener(this);
        setOkText();
        tbBack = (ImageView) findViewById(R.id.tbBack);
        tbBack.setOnClickListener(this);
        ckSelect = (CheckBox) findViewById(R.id.ckSelect);
        ckSelect.setOnClickListener(this);
        boolean isSelect = picker.getDataHolder().isSelect(previewImgs.get(mCurrentPosition));
        ckSelect.setChecked(isSelect);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(mCurrentPosition+"/"+previewImgs.size());
    }

    private void initData(){
        mCurrentPosition = getIntent().getIntExtra(Code.EXTRA_SELECTED_IMAGE_POSITION, 0);
        picker = KIMGPicker.getInstance();
        selectedImages = picker.getDataHolder().getSelectedImages();
        previewImgs = picker.getDataHolder().previewCache;
        maxSelect = picker.getDataHolder().config.maxNum;
    }

    private void initAdapter(){
        mAdapter = new ImagePreviewAdapter(this, previewImgs);
        mAdapter.setPhotoViewClickListener(new ImagePreviewAdapter.PhotoClickListener() {
            @Override
            public void OnPhotoClick(ImgItem imgItem) {
                //收起标题
            }
        });
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(mCurrentPosition, false);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentPosition = position;
                boolean isSelect = picker.getDataHolder().isSelect(previewImgs.get(position));
                ckSelect.setChecked(isSelect);
                tvTitle.setText(mCurrentPosition+"/"+previewImgs.size());
            }
        });
        viewPager.setCurrentItem(mCurrentPosition);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ckSelect){
            if (ckSelect.isChecked()) {
                if (selectedImages.size()<maxSelect){
                    selectedImages.add(previewImgs.get(mCurrentPosition));
                }else {
                    ActivityUtils.showToast("最多选择"+maxSelect+"张图片",this);
                    ckSelect.setChecked(false);
                }
            } else {
                selectedImages.remove(previewImgs.get(mCurrentPosition));
            }
            setOkText();
        }
    }

    private void setOkText(){
        if (selectedImages.size()>0){
            tbOk.setText("完成("+selectedImages.size()+"/"+maxSelect+")");
            tbOk.setEnabled(true);
        }else {
            tbOk.setText("完成");
            tbOk.setEnabled(false);
        }
    }

    public static void GoPreview(Context context,int position){
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(Code.EXTRA_SELECTED_IMAGE_POSITION, position);
        context.startActivity(intent);
    }



}
