package com.xht.androidnote.module.proxy;

import android.util.Log;
import android.widget.Button;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2019/8/1.
 */
public class ProxyActivity extends BaseActivity {
    @BindView(R.id.btn_dynamic_proxy)
    Button btnDynamicProxy;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_proxy;
    }

    @Override
    protected void initEventAndData() {
    }

    private void dynamicProxy() {
        AndroidDeveloper developer = new AndroidDeveloper("xht");

        Developer xht = (Developer) Proxy.newProxyInstance(developer.getClass().getClassLoader(),
                developer.getClass().getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("code".equals(method.getName())) {
                            Log.i("xht", "学习基础知识");
                            return method.invoke(developer, args);//调用method对应的正常的方法
                        }

                        if ("debug".equals(method.getName())) {
                            Log.i("xht", "there is no bug!");
                            return null;
                        }
                        return null;
                    }
                });

        xht.code();
        xht.debug();
    }

    @OnClick(R.id.btn_dynamic_proxy)
    public void onViewClicked() {
        dynamicProxy();
    }
}
