package com.kachidoki.ma.kimgpicker.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.kachidoki.ma.kimgpicker.Adapter.ImagePickAdapter;
import com.kachidoki.ma.kimgpicker.Adapter.PopFolderAdapter;
import com.kachidoki.ma.kimgpicker.Bean.ImgFolder;
import com.kachidoki.ma.kimgpicker.Bean.ImgItem;
import com.kachidoki.ma.kimgpicker.Loader.ImgDataLoder;
import com.kachidoki.ma.kimgpicker.Loader.LoaderCallBack;
import com.kachidoki.ma.kimgpicker.R;
import com.kachidoki.ma.kimgpicker.Utils.ActivityUtils;
import com.kachidoki.ma.kimgpicker.Utils.Code;
import com.kachidoki.ma.kimgpicker.Utils.Utils;
import com.kachidoki.ma.kimgpicker.Widget.DividerGridItemDecoration;
import com.kachidoki.ma.kimgpicker.Widget.PopFolderWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kachidoki on 2017/6/13.
 */

public class ImagePickActivity extends ImageUiActivity implements View.OnClickListener,LoaderCallBack, ImagePickAdapter.OnPickItemListener {

    private boolean first=false;

    private Button btDir;
    private View mFooterBar;
    private RecyclerView recyclerView;

    private PopFolderWindow popFolderWindow;

    private ImagePickAdapter pickAdapter;
    private PopFolderAdapter popAdapter;

    private List<ImgFolder> folderList;


    @Override
    public void createInit(){
        setContentView(R.layout.activity_img_pick);
        Intent intent = getIntent();
        if (!intent.getBooleanExtra(Code.EXTRA_USE_LASTSELECTED,false)){
            picker.getDataHolder().newSelect();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!first&&!pickAdapter.getImgData().isEmpty()){
            pickAdapter.SelectChange();
            first=true;
        }
    }

    @Override
    public void initView(){
        btOk.setOnClickListener(this);
        btDir = (Button) findViewById(R.id.btn_dir);
        btDir.setOnClickListener(this);
        btBack.setOnClickListener(this);
        mFooterBar = findViewById(R.id.footer_bar);
        //set title with config
        title.setText(config.title);
        mFooterBar.setBackgroundColor(config.allImageViewColor);
        btDir.setText(config.allImagesText);
        setOkText();

        initRecyclerView();
        requestPermissions();
    }

    public void initRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        pickAdapter = new ImagePickAdapter(this);
        pickAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(pickAdapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //pop
        popAdapter = new PopFolderAdapter(this);
    }

    public void requestPermissions(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (ActivityUtils.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,this)) {
                new ImgDataLoder(this, null, this,config.allImagesText);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Code.REQUEST_PERMISSION_STORAGE);
            }
        }else{
            new ImgDataLoder(this, null, this,config.allImagesText);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Code.REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new ImgDataLoder(this, null, this,config.allImagesText);
            } else {
                ActivityUtils.showToast(getString(R.string.img_permissions_limit),this);
            }
        } else if (requestCode == Code.REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.GoTake(this,picker,Code.REQUEST_CODE_TAKE);
            } else {
                ActivityUtils.showToast(getString(R.string.take_permissions_limit),this);
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
                //scroll to top
                recyclerView.scrollToPosition(0);
            }
        });
        popFolderWindow.setMargin(mFooterBar.getHeight());
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_dir) {
            //click the folder
            if (folderList == null) {
                ActivityUtils.showToast(getString(R.string.no_img),this);
                return;
            }
            createPopFolderList();
            popAdapter.setFolders(folderList);  //refresh
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
            // multiSelect go Preview
            picker.getDataHolder().setPreCache(pickAdapter.getImgData());
            ImagePreviewActivity.GoPreview(this,position,Code.REQUEST_CODE_PREVIEW);
        } else {
            if (picker.getDataHolder().config.needCrop) {
                // go crop
                Utils.GoCrop(this,picker,imageItem.getPath(),Code.REQUEST_CODE_CROP);
            } else {
                // single and not crop, add imageItem finish
                setBackResult(picker.getDataHolder().getSelectedSingleResult(imageItem));
                exit();
            }
        }
    }

    @Override
    public void onImageSelected(ImgItem imageItem, int position,boolean isAdd) {
        // add one
        if (picker.getDataHolder().getSelectImageCount() > 0) {
            btOk.setText(getString(R.string.select_complete,picker.getDataHolder().getSelectImageCount()));
            btOk.setEnabled(true);
        } else {
            btOk.setText(getString(R.string.complete));
            btOk.setEnabled(false);
        }
        setOkText();
    }


    @Override
    public void onCameraClick() {
        if (!ActivityUtils.checkPermission(Manifest.permission.CAMERA,this)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Code.REQUEST_PERMISSION_CAMERA);
        } else {
            Utils.GoTake(this,picker,Code.REQUEST_CODE_TAKE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Code.REQUEST_CODE_TAKE&&resultCode == RESULT_OK){
            //back from take picture,this will ignore the selected picture
            setBackResult(picker.getDataHolder().getCacheResult());
            exit();
        }else if (requestCode == Code.REQUEST_CODE_CROP&&resultCode == RESULT_OK){
            //back from take picture,this just happened in single select
            setBackResult(picker.getDataHolder().getCacheResult());
            exit();
        }else if (requestCode == Code.REQUEST_CODE_PREVIEW&&resultCode == RESULT_OK){
            //back from preview,just happen in multiSelect,and is selected,just finish
            setBackResult(picker.getDataHolder().getSelectedResult());
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

}
