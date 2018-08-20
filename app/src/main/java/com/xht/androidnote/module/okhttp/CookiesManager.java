package com.xht.androidnote.module.okhttp;

import com.xht.androidnote.base.App;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by xht on 2018/4/28.
 */

public class CookiesManager implements CookieJar {
    private final PersistentCookieStore cookieStore = new PersistentCookieStore(App.getAppContext());

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }
}