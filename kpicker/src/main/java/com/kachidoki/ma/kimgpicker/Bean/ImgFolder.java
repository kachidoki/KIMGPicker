package com.kachidoki.ma.kimgpicker.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Kachidoki on 2017/6/11.
 */

public class ImgFolder implements Parcelable {

    private String name;
    private String path;
    private ImgItem cover;
    private List<ImgItem> imgs;


    public ImgFolder(String name,String path){
        this.name=name;
        this.path=path;
    }


    protected ImgFolder(Parcel in) {
        name = in.readString();
        path = in.readString();
        cover = in.readParcelable(ImgItem.class.getClassLoader());
        imgs = in.createTypedArrayList(ImgItem.CREATOR);
    }

    public static final Creator<ImgFolder> CREATOR = new Creator<ImgFolder>() {
        @Override
        public ImgFolder createFromParcel(Parcel in) {
            return new ImgFolder(in);
        }

        @Override
        public ImgFolder[] newArray(int size) {
            return new ImgFolder[size];
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
        dest.writeParcelable(cover, flags);
        dest.writeTypedList(imgs);
    }

    @Override
    public boolean equals(Object obj) {
        try {
            ImgFolder other = (ImgFolder) obj;
            return this.path.equalsIgnoreCase(other.path) && this.name.equalsIgnoreCase(other.name);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(obj);
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public ImgItem getCover() {
        return cover;
    }

    public void setCover(ImgItem cover) {
        this.cover = cover;
    }

    public List<ImgItem> getImgs() {
        return imgs;
    }

    public void setImgs(List<ImgItem> imgs) {
        this.imgs = imgs;
    }
}
