package com.kachidoki.ma.kimgpickerSample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kachidoki.ma.kimgpicker.KIMGPicker;
import com.kachidoki.ma.kimgpicker.KPCompressor;
import com.kachidoki.ma.kimgpicker.KPConfig;
import com.kachidoki.ma.kimgpicker.Loader.ImageLoader;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView result;
    final int request = 1111;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.result);
        imageView = (ImageView) findViewById(R.id.Img);

        final KPConfig allConfig =
                new KPConfig.Builder(this)
                            .needCompressor(false)//选择后是否需要压缩,需要配合KPCompressor使用
                            .needCamera(true)//是否需要显示拍照
                            .needCrop(false)//选择完是否需要裁剪,只能用于单选
                            .cropSize(1,1,300,300)//裁剪长宽比例与输出宽高
                            .multiSelect(true)//是否需要多选
                            .rememberSelected(true)//是否使用上次选择的结果
                            .maxNum(6)//多选数量上限
                            .statusBarColor(getResources().getColor(R.color.colorAccent))//状态栏色调
                            .titleBgColor(getResources().getColor(R.color.colorAccent))//toolbar色调
                            .navigationColor(getResources().getColor(R.color.colorAccent))//导航栏色调
                            .allImageViewColor(getResources().getColor(R.color.colorAccent))//选择文件夹色调
                            .title("标题")//选择页面标题
                            .titleColor(getResources().getColor(R.color.colorPrimary))//标题字体
                            .btnTextColor(getResources().getColor(R.color.colorPrimary))//确定按钮字体颜色
                            .allImagesText("全部")//全部图片的名称
                            .cropCacheFolder(getExternalCacheDir().getAbsolutePath())//裁剪图片存放文件夹,若不设置使用默认值
                            .takeImageFile(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/DCIM/camera/")//拍照图片存放文件夹,若不设置使用默认值
                            .build();

        final KPConfig single = new KPConfig.Builder(this)
                                    .needCamera(true)
                                    .multiSelect(false)
                                    .needCrop(false)
                                    .build();

        final KPConfig compressorConfig = new KPConfig.Builder(this)
                                            .multiSelect(false)
                                            .needCompressor(true)
                                            .build();

        final KPConfig multi = new KPConfig.Builder(this)
                .needCamera(false)
                .multiSelect(true)
                .needCrop(false)
                .build();

        final KPCompressor compressor = new KPCompressor(this);

        findViewById(R.id.allConfig).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KIMGPicker.GoPick(MainActivity.this, allConfig,new GlideImageLoader(),request);
            }
        });

        findViewById(R.id.multiSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KIMGPicker.GoPick(MainActivity.this, multi,new GlideImageLoader(),request);
            }
        });

        findViewById(R.id.single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KIMGPicker.GoPick(MainActivity.this, single,new GlideImageLoader(),request);
            }
        });

        findViewById(R.id.compressor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KIMGPicker.GoPick(MainActivity.this,compressorConfig,new GlideImageLoader(),compressor,request);
            }
        });

        findViewById(R.id.goTake).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KIMGPicker.GoTake(MainActivity.this,request);
            }
        });

        findViewById(R.id.goCrop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KIMGPicker.GoCrop(MainActivity.this,"some image",request);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==request&&resultCode==RESULT_OK){
            ArrayList<String> res = data.getStringArrayListExtra(KIMGPicker.RESULT);
            result.setText(res.toString());
            imageView.setImageBitmap(BitmapFactory.decodeFile(res.get(0)));
        }
    }

    class GlideImageLoader implements ImageLoader {

        @Override
        public void displayImage(Context context, String path, ImageView imageView, int width, int height) {
            Glide.with(context)
                    .load(Uri.fromFile(new File(path)))
                    .error(R.mipmap.default_image)           //设置错误图片
                    .placeholder(R.color.gray)               //设置占位图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                    .into(imageView);
        }
    }
}
