# KPicker

![](https://img.shields.io/github/release/kachidoki/KIMGPicker.svg)

<p align="center"><img src="https://github.com/kachidoki/KIMGPicker/blob/master/ss.png" width="50%" /></p>



## Gradle依赖

```groovy
dependencies {
    compile 'com.kachidoki.library:kpicker:1.1.0'
}
```

## 使用

### 设置Config

各种自定义配置满足你的需求

```java
KPConfig allConfig 
  = new KPConfig.Builder(this)
                .needCompressor(false)//选择后是否需要压缩,需要配合KPCompressor使用
                .needCamera(true)//是否需要显示拍照
                .needCrop(false)//选择完是否需要裁剪,只能用于单选
                .cropSize(1,1,300,300)//裁剪长宽比例与输出宽高
                .multiSelect(true)//是否需要多选
                .rememberSelected(true)//是否使用上次选择的结果
                .maxNum(6)//多选数量上限
                .statusBarColor(getColor(R.color.colorAccent))//状态栏色调
                .titleBgColor(getColor(R.color.colorAccent))//toolbar色调
                .navigationColor(getColor(R.color.colorAccent))	//导航栏色调
                .allImageViewColor(getColor(R.color.colorAccent))//选择文件夹色调
                .title("标题")//选择页面标题
                .titleColor(getColor(R.color.colorPrimary))	//标题字体
                .btnTextColor(getColor(R.color.colorPrimary))//确定按钮字体颜色
                .allImagesText("全部")//全部图片的名称
                .cropCacheFolder("")//裁剪图片存放文件夹,若不设置使用默认值
                .takeImageFile("")//拍照图片存放文件夹,若不设置使用默认值
                .build();
```

### 配置ImageLoader

根据你的APP加载图片的需要，实现包内的**ImageLoader**接口，来展示图片，以**Glide**示例：

```java
class GlideImageLoader implements ImageLoader {
        
        @Override
        public void displayImage(Context context, String path, ImageView imageView, int width, int height) {
            Glide.with(context)                               
                    .load(Uri.fromFile(new File(path)))      
                    .error(R.mipmap.default_image)//设置错误图片
                    .placeholder(R.color.gray)//设置占位图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                    .into(imageView);
        }
    }
```

### 进入选择

将**config**和**ImageLoader**传入即可

```java
KIMGPicker.GoPick(this, allConfig,new GlideImageLoader(),request);
```

### 图片压缩

有的时候选择完图片需要压缩后才能上传到服务器

如果要使用压缩需要将**config**中的**needCompressor**为**true**且要设置**Compressor**

```java
KPCompressor compressor = new KPCompressor(this)
                              .MaxSize(400,400)//最大长宽
                              .Quality(90)//输出质量
                              .CompressFormat(Bitmap.CompressFormat.JPEG)//输出格式
                              .DestinationPath("")//输出的文件夹
 
  KPConfig compressorConfig = new KPConfig.Builder(this)
                                          .multiSelect(false)
                                          .needCompressor(true)
                                          .build();
//调用时传入参数即可
KIMGPicker.GoPick(this,compressorConfig,new GlideImageLoader(),compressor,request);
```

> 压缩过程默认在UI线程，如果图片过大或者选择图片较多导致压缩卡顿太久，可以单独使用KPCompressor进行异步操作

### 获取结果

返回的是一个选择图片的路径的**ArrayList**，通过名为**KIMGPicker.RESULT**的Extra获取

```java
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==request&&resultCode==RESULT_OK){
            ArrayList<String> res = data.getStringArrayListExtra(KIMGPicker.RESULT);
            result.setText(res.toString());
            imageView.setImageBitmap(BitmapFactory.decodeFile(res.get(0)));
        }
    }
```

### 也可直接进入拍照或裁剪或进行压缩

**KIMGPicker**类下给出了多种重载方法，提供可直接进入拍照或者裁剪界面

**KPCompressor**类也可单独使用进行压缩

**更多参看源码**

## License

请查看[LICENSE](https://github.com/kachidoki/KIMGPicker/blob/v1.1.0/LICENSE)

