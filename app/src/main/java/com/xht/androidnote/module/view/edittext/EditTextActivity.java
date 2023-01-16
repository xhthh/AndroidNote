package com.xht.androidnote.module.view.edittext;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2020/1/9.
 */
public class EditTextActivity extends BaseActivity {
    @BindView(R.id.edit_text_test1)
    EditText editTextTest1;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    String strHint;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_text;
    }

    @Override
    protected void initEventAndData() {

        //输入字符星号化
        editTextTest1.setTransformationMethod(new WordReplacement());

        //EditText 获得焦点时hint消失，失去焦点时hint显示
        editTextTest1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    ((TextView) v).setHint(strHint);
                } else {
                    ((TextView) v).setHint("");
                }

            }
        });

        editTextTest1.addTextChangedListener(new PriceWatcher(editTextTest1));


        editTextTest1.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int sStart;
            private int sEnd;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
                Log.e("xht","------输入文本 = " + temp.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_title:
                break;
        }
    }
}
