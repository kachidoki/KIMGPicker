package com.kachidoki.ma.kimgpicker;

import android.content.Context;
import android.graphics.Bitmap;

import com.kachidoki.ma.kimgpicker.Utils.CompressorUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by Kachidoki on 2017/6/21.
 */

public class KPCompressor {

    private int maxWidth = 612;
    private int maxHeight = 816;
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    private int quality = 80;
    private String destinationDirectoryPath;

    public KPCompressor(Context context) {
        destinationDirectoryPath = context.getCacheDir().getAbsolutePath() + File.separator + "Compressor";
        File folder = new File(destinationDirectoryPath);
        if (!folder.exists() || !folder.isDirectory()) folder.mkdirs();
    }

    public KPCompressor MaxSize(int maxWidth,int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        return this;
    }

    public KPCompressor CompressFormat(Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
        return this;
    }

    public KPCompressor Quality(int quality) {
        this.quality = quality;
        return this;
    }

    public KPCompressor DestinationPath(String destinationDirectoryPath) {
        this.destinationDirectoryPath = destinationDirectoryPath;
        return this;
    }

    public File compressToFile(File imageFile) throws IOException {
        return compressToFile(imageFile, imageFile.getName());
    }

    public File compressToFile(File imageFile, String compressedFileName) throws IOException {
        return CompressorUtil.compressImage(imageFile, maxWidth, maxHeight, compressFormat, quality,
                destinationDirectoryPath + File.separator + compressedFileName);
    }

    public Bitmap compressToBitmap(File imageFile) {
        return CompressorUtil.decodeSampledBitmapFromFile(imageFile, maxWidth, maxHeight);
    }
}
