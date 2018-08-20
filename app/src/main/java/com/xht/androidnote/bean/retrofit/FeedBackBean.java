package com.xht.androidnote.bean.retrofit;

/**
 * 意见反馈实体类
 */
public class FeedBackBean {

    private String status;
    private String data;
    private String note;

    @Override
    public String toString() {
        return "FeedBackBean{" +
                "status='" + status + '\'' +
                ", data='" + data + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


}
