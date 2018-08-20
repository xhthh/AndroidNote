package com.xht.androidnote.bean.retrofit;

/**
 * 上传用户信息实体类
 */
public class UpLoadUserBean {

	private String status;
	private String data;
	private String note;
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

	@Override
	public String toString() {
		return "UpLoadUserBean{" +
				"status='" + status + '\'' +
				", data='" + data + '\'' +
				", note='" + note + '\'' +
				'}';
	}
}
