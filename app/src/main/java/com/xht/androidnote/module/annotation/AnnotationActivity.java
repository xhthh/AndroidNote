package com.xht.androidnote.module.annotation;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.annotation.test.Animal;
import com.xht.androidnote.module.annotation.test.Test;
import com.xht.annotations.BindView;

import java.lang.reflect.Field;

/**
 * Created by xht on 2019/10/8.
 */
public class AnnotationActivity extends BaseActivity {

    @BindView(id = R.id.btn_1)
    Button btn1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_annotation;
    }

    @Override
    protected void initEventAndData() {

        Bind_AnnotationActivity.bindView(this);
        btn1.setText("btnTest1");

        //testMethod();

        test(null);


        test2("foo bar");
    }

    /**
     * 利用反射解析Annotation
     */
    private void testMethod() {
        Class clazz = Animal.class;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Test test = field.getAnnotation(Test.class);
            if (test != null) {
                int id = test.id();
                Log.i("xht", "testMethod()---id==" + id);
            }
        }
    }

    @CheckResult
    private String test2(@NonNull String parameter) {
        return parameter.replace(" ","-");
    }

    private void test(@NonNull String str) {
        Log.i("xht", "test()------null");
    }
}