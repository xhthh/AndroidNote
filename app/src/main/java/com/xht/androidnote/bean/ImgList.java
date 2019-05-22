package com.xht.androidnote.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xht on 2019/5/17.
 */

public class ImgList implements Parcelable {
    /**
     * img : https://img2.autoimg.cn/hscdfs/g2/M05/3A/86/ChcCRFy-sHuAA-EUAAJP7oidsbE330.jpg
     * imgheight : 1632
     * imgwidth : 918
     */
    public String img;
    public int imgheight;
    public int imgwidth;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.img);
        dest.writeInt(this.imgheight);
        dest.writeInt(this.imgwidth);
    }

    public ImgList() {
    }

    protected ImgList(Parcel in) {
        this.img = in.readString();
        this.imgheight = in.readInt();
        this.imgwidth = in.readInt();
    }

    public static final Parcelable.Creator<ImgList> CREATOR = new Parcelable.Creator<ImgList>() {
        @Override
        public ImgList createFromParcel(Parcel source) {
            return new ImgList(source);
        }

        @Override
        public ImgList[] newArray(int size) {
            return new ImgList[size];
        }
    };
}
