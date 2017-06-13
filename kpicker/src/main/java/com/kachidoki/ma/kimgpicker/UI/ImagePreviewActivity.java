package com.kachidoki.ma.kimgpicker.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kachidoki.ma.kimgpicker.Adapter.ImagePreviewAdapter;
import com.kachidoki.ma.kimgpicker.Bean.ImgItem;
import com.kachidoki.ma.kimgpicker.KIMGPicker;
import com.kachidoki.ma.kimgpicker.R;
import com.kachidoki.ma.kimgpicker.Utils.Code;
import com.kachidoki.ma.kimgpicker.Widget.TouchViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kachidoki on 2017/6/13.
 */

public class ImagePreviewActivity extends AppCompatActivity implements View.OnClickListener,CheckBox.OnCheckedChangeListener{

    private KIMGPicker picker;

    private View topBar;
    private Button tbOk;
    private ImageView tbBack;
    private CheckBox ckSelect;

    private TouchViewPager viewPager;
    private ImagePreviewAdapter mAdapter;

    private List<ImgItem> mImageItems;
    private int mCurrentPosition = 0;
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
        tbOk = (Button) findViewById(R.id.btn_ok);
        tbOk.setOnClickListener(this);
        tbBack = (ImageView) findViewById(R.id.btn_back);
        tbBack.setOnClickListener(this);
    }

    private void initData(){
        mCurrentPosition = getIntent().getIntExtra(Code.EXTRA_SELECTED_IMAGE_POSITION, 0);
        picker = KIMGPicker.getInstance();
        selectedImages = picker.getDataHolder().getSelectedImages();

    }

    private void initAdapter(){
        mAdapter = new ImagePreviewAdapter(this, mImageItems);
        mAdapter.setPhotoViewClickListener(new ImagePreviewAdapter.PhotoClickListener() {
            @Override
            public void OnPhotoClick(ImgItem imgItem) {

            }
        });
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(mCurrentPosition, false);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

}
