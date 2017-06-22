package com.kachidoki.ma.kimgpickerSample;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
        imageView = (ImageView) findViewById(R.id.compressor);

        final KPConfig single = new KPConfig.Builder(this)
                                    .needCamera(true)
                                    .multiSelect(false)
                                    .needCrop(false)
                                    .needCompressor(true)
                                    .build();

        final KPConfig multi = new KPConfig.Builder(this)
                .needCamera(false)
                .multiSelect(true)
                .needCrop(false)
                .build();

        final KPCompressor compressor = new KPCompressor(this);



        findViewById(R.id.multiSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KIMGPicker.GoPick(MainActivity.this, multi,new GlideImageLoader(),request);
            }
        });

        findViewById(R.id.single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                KIMGPicker.GoPick(MainActivity.this, single,new GlideImageLoader(),request);
                KIMGPicker.GoPick(MainActivity.this,single,new GlideImageLoader(),compressor,request,false);
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
            Glide.with(context)                             //配置上下文
                    .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                    .error(R.mipmap.default_image)           //设置错误图片
                    .placeholder(R.color.gray)     //设置占位图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                    .into(imageView);
        }
    }
}
