package com.xht.androidnote.module.view.edittext;

import android.widget.EditText;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by xht on 2020/1/9.
 */
public class EditTextActivity extends BaseActivity {
    @BindView(R.id.edit_text_test1)
    EditText editTextTest1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_text;
    }

    @Override
    protected void initEventAndData() {

     editTextTest1.addTextChangedListener(new PriceWatcher(editTextTest1));

    }

}
