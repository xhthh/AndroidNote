package com.xht.androidnote.module.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xht on 2018/9/10.
 */

public class EventBusHelper {

    private static EventBusHelper defaultInstance;

    public static synchronized EventBusHelper getInstance() {
        if (defaultInstance == null) {
            synchronized (EventBusHelper.class) {
                defaultInstance = new EventBusHelper();
            }
        }
        return defaultInstance;
    }

    /**
     * 注册订阅者，调用register注册的对象，这个对象内包含onEvent\*函数来处理事件内容。
     * <pre>
     * 即以下4个接口的回调方法：
     *
     * <pre>
     *
     * @param subscriber 订阅源
     *
     */
    public void register(Object subscriber) {
        if(!EventBus.getDefault().isRegistered(subscriber)){
            EventBus.getDefault().register(subscriber);
        }
    }

    /**
     * 取消订阅
     *
     * @param subscriber 订阅源
     */
    public void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /**
     * 发布事件
     *
     * @param event
     */
    public void post(Object event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 发布粘性事件
     */
    public void postSticky(Object event){
        EventBus.getDefault().postSticky(event);
    }

    /**
     * 移除粘性事件
     */
    public void removeStickyEvent(Object event){
        EventBus.getDefault().removeStickyEvent(event);
    }

    /**
     * 移除所有粘性事件
     */
    public void removeAllStickyEvents(){
        EventBus.getDefault().removeAllStickyEvents();
    }

    public boolean isRegistered(Object subscriber){
        return EventBus.getDefault().isRegistered(subscriber);
    }
}
