package com.kachidoki.ma.kimgpicker.Loader;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import com.kachidoki.ma.kimgpicker.Bean.ImgFolder;
import com.kachidoki.ma.kimgpicker.Bean.ImgItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kachidoki on 2017/6/11.
 */

public class ImgDataLoder implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int LOAD_ALL = 0;
    public static final int LOAD_PATH = 1;
    private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED
    };

    private Activity context;
    private LoaderCallBack callBack;
    private List<ImgFolder> imgFolders;

    public ImgDataLoder(Activity context,String path,LoaderCallBack call){
        this.context=context;
        this.callBack=call;
        LoaderManager loaderManager = context.getLoaderManager();
        if (path!=null){
            Bundle bundle=new Bundle();
            bundle.putString("path",path);
            loaderManager.initLoader(LOAD_PATH,bundle,this);
        }else {
            loaderManager.initLoader(LOAD_ALL,null,this);
        }
        imgFolders=new ArrayList<>();
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        if (id == LOAD_ALL)
            cursorLoader = new CursorLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION, null,
                    null, IMAGE_PROJECTION[6] + " DESC");
        if (id == LOAD_PATH)
            cursorLoader = new CursorLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION, IMAGE_PROJECTION[1] + " like '%" + args.getString("path") + "%'",
                    null, IMAGE_PROJECTION[6] + " DESC");

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        imgFolders.clear();
        if (data!=null){
            List<ImgItem> allImg=new ArrayList<>();
            while (data.moveToNext()){
                String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                String imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                long imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                int imageWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                int imageHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                String imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                long imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));

                ImgItem img = new ImgItem(imageName,imagePath,imageSize,imageWidth,imageHeight,imageMimeType,imageAddTime);
                allImg.add(img);

                File imgFile = new File(imagePath);
                File parentFolder = imgFile.getParentFile();
                ImgFolder imgFolder = new ImgFolder(parentFolder.getName(),parentFolder.getAbsolutePath());
                int index = imgFolders.indexOf(imgFolder);
                if (index==-1){
                    List<ImgItem> imgs = new ArrayList<>();
                    imgs.add(img);
                    imgFolder.setCover(img);
                    imgFolder.setImgs(imgs);
                    imgFolders.add(imgFolder);
                }else {
                    imgFolders.get(index).getImgs().add(img);
                }
            }
            if (data.getCount()>0){
                ImgFolder allImgsFolder = new ImgFolder("全部图片","/");
                allImgsFolder.setCover(allImg.get(0));
                allImgsFolder.setImgs(allImg);
                imgFolders.add(0,allImgsFolder);
            }
        }
        //IMGPICKER SET

        callBack.ImgsLoaded(imgFolders);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        imgFolders.clear();
    }
}
