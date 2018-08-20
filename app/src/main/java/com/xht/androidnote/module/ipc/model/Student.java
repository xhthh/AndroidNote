package com.xht.androidnote.module.ipc.model;

import java.io.Serializable;

/**
 * Created by xht on 2018/6/28.
 */

public class Student implements Serializable {
    private static final long serialVersionUID = 1698350653108996963L;

    public int userId;
    public String userName;
    public boolean isMale;

    public Student(int userId, String userName, boolean isMale) {
        this.userId = userId;
        this.userName = userName;
        this.isMale = isMale;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    @Override
    public String toString() {
        return "Student{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", isMale=" + isMale +
                '}';
    }
}
