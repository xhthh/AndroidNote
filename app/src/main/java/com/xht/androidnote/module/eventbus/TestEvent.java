package com.xht.androidnote.module.eventbus;

/**
 * Created by xht on 2018/9/10.
 */

public class TestEvent implements Event{

    private String flag;

    public TestEvent(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
