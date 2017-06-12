package com.kachidoki.ma.kimgpicker.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.kachidoki.ma.kimgpicker.Adapter.ImageListAdapter;
import com.kachidoki.ma.kimgpicker.Adapter.PopFolderAdapter;
import com.kachidoki.ma.kimgpicker.Bean.ImgFolder;
import com.kachidoki.ma.kimgpicker.Bean.ImgItem;
import com.kachidoki.ma.kimgpicker.KImgPicker;
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
 * Created by Kachidoki on 2017/6/12.
 */

public class ImgListActivity extends AppCompatActivity implements View.OnClickListener,LoaderCallBack,KImgPicker.OnImageSelectedListener, ImageListAdapter.OnImageItemClickListener {

    public static final String EXTRAS_TAKE_PICKERS = "TAKE";
    public static final String EXTRAS_IMAGES = "IMAGES";

    private KImgPicker picker;
    private boolean isOrigin = false;

    private Button btOk;
    private Button btDir;
    private Button btPre;
    private View mFooterBar;
    private List<ImgFolder> folderList;

    private PopFolderAdapter popFolderAdapter;
    private PopFolderWindow popFolderWindow;

    private ImageListAdapter imageListAdapter;
    private RecyclerView recyclerView;

    private boolean directPhoto = false;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        directPhoto = savedInstanceState.getBoolean(EXTRAS_TAKE_PICKERS,false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRAS_TAKE_PICKERS, directPhoto);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_list);

        picker = picker.getInstance();
        picker.clear();
        picker.addOnImageSelectedListener(this);

        Intent data = getIntent();
        // 新增可直接拍照
        if (data != null && data.getExtras() != null){
            directPhoto = data.getBooleanExtra(EXTRAS_TAKE_PICKERS,false); // 默认不是直接打开相机
            if (directPhoto){
                if (!(ActivityUtils.checkPermission(Manifest.permission.CAMERA,this))) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Code.REQUEST_PERMISSION_CAMERA);
                } else {
                    Utils.takePicture(this, Code.REQUEST_CODE_TAKE,picker.getTakeImageFile());
                }
            }
            List<ImgItem> images = data.getParcelableArrayListExtra(EXTRAS_IMAGES);
            picker.setSelectedImages(images);
        }


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);


        findViewById(R.id.btn_back).setOnClickListener(this);
        mFooterBar = findViewById(R.id.footer_bar);
        btOk = (Button) findViewById(R.id.btn_ok);
        btOk.setOnClickListener(this);
        btDir = (Button) findViewById(R.id.btn_dir);
        btDir.setOnClickListener(this);
        btPre = (Button) findViewById(R.id.btn_preview);
        btPre.setOnClickListener(this);
        if (picker.isMultiMode()) {
            btOk.setVisibility(View.VISIBLE);
            btPre.setVisibility(View.VISIBLE);
        } else {
            btOk.setVisibility(View.GONE);
            btPre.setVisibility(View.GONE);
        }

        popFolderAdapter = new PopFolderAdapter(this);
        imageListAdapter = new ImageListAdapter(this);
        imageListAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(imageListAdapter);

        onImageSelected(0, null, false);

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
                ActivityUtils.showToast("权限被禁止，无法选择本地图片",this);
            }
        } else if (requestCode == Code.REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.takePicture(this, Code.REQUEST_CODE_TAKE,picker.getTakeImageFile());
            } else {
                ActivityUtils.showToast("权限被禁止，无法打开相机",this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        picker.removeOnImageSelectedListener(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_ok) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(Code.EXTRA_RESULT_ITEMS, (ArrayList<ImgItem>) picker.getSelectedImages());
            setResult(Code.RESULT_CODE_ITEMS, intent);  //多选不允许裁剪裁剪，返回数据
            finish();
        } else if (id == R.id.btn_dir) {
            if (folderList == null) {
                Log.i("ImageGridActivity", "您的手机没有图片");
                return;
            }
            //点击文件夹按钮
            createPopupFolderList();
            popFolderAdapter.setFolders(folderList);  //刷新数据
            if (popFolderWindow.isShowing()) {
                popFolderWindow.dismiss();
            } else {
                popFolderWindow.showAtLocation(mFooterBar, Gravity.NO_GRAVITY, 0, 0);
            }
        } else if (id == R.id.btn_preview) {
//            Intent intent = new Intent(ImageGridActivity.this, ImagePreviewActivity.class);
//            intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
//            intent.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, imagePicker.getSelectedImages());
//            intent.putExtra(ImagePreviewActivity.ISORIGIN, isOrigin);
//            intent.putExtra(ImagePicker.EXTRA_FROM_ITEMS,true);
//            startActivityForResult(intent, ImagePicker.REQUEST_CODE_PREVIEW);
        } else if (id == R.id.btn_back) {
            //点击返回按钮
            finish();
        }
    }

    /** 创建弹出的ListView */
    private void createPopupFolderList() {
        popFolderWindow = new PopFolderWindow(this, popFolderAdapter);
        popFolderWindow.setOnItemClickListener(new PopFolderWindow.OnItemClickListener() {
            @Override
            public void onItemClick(ImgFolder folder,int position) {
                popFolderAdapter.setLastSelsct(position);
                popFolderWindow.dismiss();
                if (folder!=null) {
                    imageListAdapter.setImgData(folder.getImgs());
                    btDir.setText(folder.getName());
                }
//-------------------------------------recycler滑动到顶部------------------------------------------
            }
        });
        popFolderWindow.setMargin(mFooterBar.getHeight());
    }

    @Override
    public void ImgsLoaded(List<ImgFolder> imageFolders) {
        this.folderList = imageFolders;
//        picker.setImageFolders(imageFolders);
        if (imageFolders.size() == 0) {
            imageListAdapter.setImgData(new ArrayList<ImgItem>());
        }else {
            imageListAdapter.setImgData(imageFolders.get(0).getImgs());
        }
        popFolderAdapter.setFolders(imageFolders);
    }

    @Override
    public void onImageItemClick(View view, ImgItem imageItem, int position) {
        //根据是否有相机按钮确定位置
//        position = picker.isShowCamera() ? position - 1 : position;
//        if (picker.isMultiMode()) {
//            Intent intent = new Intent(ImgListActivity.this, ImagePreviewActivity.class);
//            intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
//
//            /**
//             * 2017-03-20
//             *
//             * 依然采用弱引用进行解决，采用单例加锁方式处理
//             */
//
//            // 据说这样会导致大量图片的时候崩溃
////            intent.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, imagePicker.getCurrentImageFolderItems());
//
//            // 但采用弱引用会导致预览弱引用直接返回空指针
//            DataHolder.getInstance().save(DataHolder.DH_CURRENT_IMAGE_FOLDER_ITEMS, imagePicker.getCurrentImageFolderItems());
//            intent.putExtra(ImagePreviewActivity.ISORIGIN, isOrigin);
//            startActivityForResult(intent, ImagePicker.REQUEST_CODE_PREVIEW);  //如果是多选，点击图片进入预览界面
//        } else {
//            imagePicker.clearSelectedImages();
//            imagePicker.addSelectedImageItem(position, imagePicker.getCurrentImageFolderItems().get(position), true);
//            if (imagePicker.isCrop()) {
//                Intent intent = new Intent(ImageGridActivity.this, ImageCropActivity.class);
//                startActivityForResult(intent, ImagePicker.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
//            } else {
//                Intent intent = new Intent();
//                intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
//                setResult(ImagePicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
//                finish();
//            }
//        }
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onImageSelected(int position, ImgItem item, boolean isAdd) {
        if (picker.getSelectImageCount() > 0) {
            btOk.setText("完成"+picker.getSelectImageCount()+"/"+picker.getSelectLimit());
            btOk.setEnabled(true);
            btPre.setEnabled(true);
        } else {
            btOk.setText("完成");
            btOk.setEnabled(false);
            btPre.setEnabled(false);
        }
        btPre.setText("预览("+picker.getSelectImageCount()+")");
        for (int i = picker.isShowCamera()? 1 : 0; i < imageListAdapter.getItemCount(); i++) {
            if (imageListAdapter.getItem(i).getPath() != null && imageListAdapter.getItem(i).getPath().equals(item.getPath())) {
                imageListAdapter.notifyItemChanged(i);
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getExtras() != null) {
            if (resultCode == Code.RESULT_CODE_BACK) {
//                isOrigin = data.getBooleanExtra(ImagePreviewActivity.ISORIGIN, false);
            } else {
                //从拍照界面返回
                //点击 X , 没有选择照片
                if (data.getParcelableExtra(Code.EXTRA_RESULT_ITEMS) == null) {
                    //什么都不做 直接调起相机
                } else {
                    //说明是从裁剪页面过来的数据，直接返回就可以
                    setResult(Code.RESULT_CODE_ITEMS, data);
                }
                finish();
            }
        } else {
//            //如果是裁剪，因为裁剪指定了存储的Uri，所以返回的data一定为null
//            if (resultCode == RESULT_OK && requestCode == Code.REQUEST_CODE_TAKE) {
//                //发送广播通知图片增加了
//                Utils.galleryAddPic(this, picker.getTakeImageFile());
//
//                /**
//                 *  对机型做旋转处理
//                 */
//                String path = picker.getTakeImageFile().getAbsolutePath();
//                ImgItem imageItem = new ImageItem();
//                imageItem.path = path;
//                imagePicker.clearSelectedImages();
//                imagePicker.addSelectedImageItem(0, imageItem, true);
//                if (picker.isCrop()) {
//                    Intent intent = new Intent(ImgListActivity.this, ImageCropActivity.class);
//                    startActivityForResult(intent, ImagePicker.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
//                } else {
//                    Intent intent = new Intent();
//                    intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
//                    setResult(ImagePicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
//                    finish();
//                }
//            } else if (directPhoto){
//                finish();
//            }
        }
    }

}
