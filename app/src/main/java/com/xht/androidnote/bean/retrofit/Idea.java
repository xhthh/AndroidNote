package com.xht.androidnote.bean.retrofit;

/**
 * Created by xht on 2018/5/17.
 */

public class Idea {
    private String content;
    private String user_id;

    public Idea(String content, String user_id) {
        this.content = content;
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
