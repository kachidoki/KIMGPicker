package com.kachidoki.ma.kimgpickerSample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kachidoki.ma.kimgpicker.KIMGPicker;
import com.kachidoki.ma.kimgpicker.KPConfig;
import com.kachidoki.ma.kimgpicker.Loader.ImageLoader;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView result;
    final int request = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.result);

        final KPConfig single = new KPConfig.Builder(this)
                                    .needCamera(true)
                                    .multiSelect(false)
                                    .needCrop(true)
                                    .build();

        final KPConfig multi = new KPConfig.Builder(this)
                .needCamera(true)
                .multiSelect(true)
                .needCrop(false)
                .build();


        findViewById(R.id.multiSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, ImagePickActivity.class));
                KIMGPicker.GoPick(MainActivity.this, multi,new GlideImageLoader(),request);
            }
        });

        findViewById(R.id.single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KIMGPicker.GoPick(MainActivity.this, single,new GlideImageLoader(),request);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==request&&resultCode==RESULT_OK){
            ArrayList<String> res = data.getStringArrayListExtra(KIMGPicker.RESULT);
            result.setText(res.toString());
        }
    }

    class GlideImageLoader implements ImageLoader {

        @Override
        public void displayImage(Context context, String path, ImageView imageView, int width, int height) {
            Glide.with(context)                             //配置上下文
                    .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                    .error(R.mipmap.default_image)           //设置错误图片
                    .placeholder(R.mipmap.default_image)     //设置占位图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                    .into(imageView);
        }
    }
}
