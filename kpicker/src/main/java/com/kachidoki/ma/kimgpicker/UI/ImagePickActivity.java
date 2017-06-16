package com.kachidoki.ma.kimgpicker.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kachidoki.ma.kimgpicker.Adapter.ImagePickAdapter;
import com.kachidoki.ma.kimgpicker.Adapter.PopFolderAdapter;
import com.kachidoki.ma.kimgpicker.Bean.ImgFolder;
import com.kachidoki.ma.kimgpicker.Bean.ImgItem;
import com.kachidoki.ma.kimgpicker.KIMGPicker;
import com.kachidoki.ma.kimgpicker.Loader.ImgDataLoder;
import com.kachidoki.ma.kimgpicker.Loader.LoaderCallBack;
import com.kachidoki.ma.kimgpicker.R;
import com.kachidoki.ma.kimgpicker.Utils.ActivityUtils;
import com.kachidoki.ma.kimgpicker.Utils.Code;
import com.kachidoki.ma.kimgpicker.Utils.Utils;
import com.kachidoki.ma.kimgpicker.Widget.PopFolderWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kachidoki on 2017/6/13.
 */

public class ImagePickActivity extends AppCompatActivity implements View.OnClickListener,LoaderCallBack, ImagePickAdapter.OnPickItemListener {

    private KIMGPicker picker;
    private boolean first=false;

    //.....view
    private Button btOk;
    private Button btDir;
    private View mFooterBar;
    private TextView title;
    private RecyclerView recyclerView;
    //---------
    private PopFolderWindow popFolderWindow;
    //Adapter
    private ImagePickAdapter pickAdapter;
    private PopFolderAdapter popAdapter;

    private List<ImgFolder> folderList;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_pick);
        picker = KIMGPicker.getInstance();

        Intent intent = getIntent();
        if (!intent.getBooleanExtra(Code.EXTRA_USE_LASTSELECTED,false)){
            picker.getDataHolder().newSelect();
        }

        initView();
        initRecyclerView();
        requestPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!first&&!pickAdapter.getImgData().isEmpty()){
            pickAdapter.SelectChange();
            first=true;
        }
    }

    public void initView(){
        btOk = (Button) findViewById(R.id.tbConfirm);
        btOk.setOnClickListener(this);
        btDir = (Button) findViewById(R.id.btn_dir);
        btDir.setOnClickListener(this);
        findViewById(R.id.tbBack).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tvTitle);
        if (picker.getDataHolder().config.multiSelect) {
            btOk.setVisibility(View.VISIBLE);
        } else {
            btOk.setVisibility(View.GONE);
        }
        mFooterBar = findViewById(R.id.footer_bar);
    }

    public void initRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        pickAdapter = new ImagePickAdapter(this);
        pickAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(pickAdapter);
        //pop
        popAdapter = new PopFolderAdapter(this);
    }

    public void requestPermissions(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (ActivityUtils.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,this)) {
                new ImgDataLoder(this, null, this);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Code.REQUEST_PERMISSION_STORAGE);
            }
        }else{
            new ImgDataLoder(this, null, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Code.REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new ImgDataLoder(this, null, this);
            } else {
                ActivityUtils.showToast("权限被禁止,无法选择本地图片",this);
            }
        } else if (requestCode == Code.REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                KIMGPicker.GoTake(this,picker.getDataHolder().config.takeImageFile);
            } else {
                ActivityUtils.showToast("权限被禁止,无法打开相机",this);
            }
        }
    }

    @Override
    public void ImgsLoaded(List<ImgFolder> folders) {
        this.folderList = folders;
        if (folders.size() == 0) {
            pickAdapter.setImgData(new ArrayList<ImgItem>());
        }else {
            pickAdapter.setImgData(folders.get(0).getImgs());
        }
        popAdapter.setFolders(folders);
    }

    public void createPopFolderList(){
        popFolderWindow = new PopFolderWindow(this, popAdapter);
        popFolderWindow.setOnItemClickListener(new PopFolderWindow.OnItemClickListener() {
            @Override
            public void onItemClick(ImgFolder folder,int position) {
                popAdapter.setLastSelsct(position);
                popFolderWindow.dismiss();
                if (folder!=null) {
                    pickAdapter.setImgData(folder.getImgs());
                    btDir.setText(folder.getName());
                }
//-------------------------------------recycler滑动到顶部------------------------------------------
            }
        });
        popFolderWindow.setMargin(mFooterBar.getHeight());
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_dir) {
            //点击文件夹按钮
            if (folderList == null) {
                ActivityUtils.showToast("没有图片",this);
                return;
            }
            createPopFolderList();
            popAdapter.setFolders(folderList);  //刷新数据??
            if (popFolderWindow.isShowing()) {
                popFolderWindow.dismiss();
            } else {
                popFolderWindow.showAtLocation(mFooterBar, Gravity.NO_GRAVITY, 0, 0);
            }
        } else if (id == R.id.tbConfirm){
            //this click is just in multiSelect and selectCount > 0
            setBackResult(picker.getDataHolder().getSelectedResult());
            exit();
        } else if (id == R.id.tbBack){
            onBackPressed();
        }
    }

    public void onImageClick(ImgItem imageItem, int position) {
        position = picker.getDataHolder().config.needCamera ? position - 1 : position;
        if (picker.getDataHolder().config.multiSelect) {
            //多选去预览画面
            picker.getDataHolder().setPreCache(pickAdapter.getImgData());
            ImagePreviewActivity.GoPreview(this,position);
        } else {
            if (picker.getDataHolder().config.needCrop) {
                //去裁剪
                KIMGPicker.GoCrop(this,imageItem.getPath());
            } else {
                //不裁剪返回
                finish();
            }
        }
    }

    @Override
    public void onImageSelected(ImgItem imageItem, int position,boolean isAdd) {
        int maxCount = picker.getDataHolder().config.maxNum;
        //增加了一个
        if (picker.getDataHolder().getSelectImageCount() > 0) {
            btOk.setText("完成("+picker.getDataHolder().getSelectImageCount()+"/"+maxCount+")");
            btOk.setEnabled(true);
        } else {
            btOk.setText("完成");
            btOk.setEnabled(false);
        }
    }


    @Override
    public void onCameraClick() {
        if (!ActivityUtils.checkPermission(Manifest.permission.CAMERA,this)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Code.REQUEST_PERMISSION_CAMERA);
        } else {
            KIMGPicker.GoTake(this,picker.getDataHolder().config.takeImageFile);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Code.REQUEST_CODE_TAKE&&resultCode==RESULT_OK){
            //back from take picture,this will ignore the selected picture
            setBackResult(picker.getDataHolder().getCacheResult());
            exit();
        }else if (requestCode==Code.REQUEST_CODE_CROP&&resultCode==RESULT_OK){
            //back from take picture,this just happened in single select
            setBackResult(picker.getDataHolder().getCacheResult());
            exit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (popFolderWindow!=null&&popFolderWindow.isShowing()){
            popFolderWindow.dismiss();
        }else {
            exit();
        }
    }

    private void exit(){
        KIMGPicker.getInstance().clearCache();
        finish();
    }

    private void setBackResult(ArrayList<String> result){
        Intent backIntent = new Intent();
        backIntent.putStringArrayListExtra(KIMGPicker.RESULT,result);
        setResult(RESULT_OK,backIntent);
    }



}
