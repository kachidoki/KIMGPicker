package com.kachidoki.ma.kimgpicker.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kachidoki on 2017/6/11.
 */

public class ImgItem implements Parcelable{

    private String name;
    private String path;
    private long size;
    private int width;
    private int height;
    private String mimeType;
    private long addTime;

    public ImgItem(String name, String path, long size, int width, int height, String mimeType, long addTime) {
        this.name=name;
        this.path=path;
        this.size=size;
        this.width=width;
        this.height=height;
        this.mimeType=mimeType;
        this.addTime=addTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    protected ImgItem(Parcel in) {
        name = in.readString();
        path = in.readString();
        size = in.readLong();
        width = in.readInt();
        height = in.readInt();
        mimeType = in.readString();
        addTime = in.readLong();
    }

    public static final Creator<ImgItem> CREATOR = new Creator<ImgItem>() {
        @Override
        public ImgItem createFromParcel(Parcel in) {
            return new ImgItem(in);
        }

        @Override
        public ImgItem[] newArray(int size) {
            return new ImgItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(path);
        dest.writeLong(size);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(mimeType);
        dest.writeLong(addTime);
    }

    @Override
    public boolean equals(Object obj) {
        try {
            ImgItem other = (ImgItem) obj;
            return this.path.equalsIgnoreCase(other.path) && this.addTime == other.addTime;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(obj);
    }
}
