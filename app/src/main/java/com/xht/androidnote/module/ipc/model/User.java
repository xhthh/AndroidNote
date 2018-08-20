package com.xht.androidnote.module.ipc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.xht.androidnote.module.ipc.aidl.Book;

import java.io.Serializable;

/**
 * Created by xht on 2018/6/28.
 */

public class User implements Parcelable, Serializable {

    private static final long serialVersionUID = 5899687131106368811L;
    public int userId;
    public String userName;
    public boolean isMale;

    public Book book;

    public User() {
    }

    public User(int userId, String userName, boolean isMale) {
        this.userId = userId;
        this.userName = userName;
        this.isMale = isMale;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(userId);
        out.writeString(userName);
        out.writeInt(isMale ? 1 : 0);
        out.writeParcelable(book, 0);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        isMale = in.readInt() == 1;
        book = in
                .readParcelable(Thread.currentThread().getContextClassLoader());
    }

    @Override
    public String toString() {
        return String.format(
                "User:{userId:%s, userName:%s, isMale:%s}, with child:{%s}",
                userId, userName, isMale, book);
    }
}
