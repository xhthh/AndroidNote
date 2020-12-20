package com.xht.androidnote.module.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by xht on 2018/6/19.
 */

public class ContentFragment extends BaseFragment {
    private static final String ARGUMENT = "argument";
    @BindView(R.id.tv_fragment_content_title)
    TextView mTvContent;

    private String mArgument;

    public static ContentFragment newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mArgument = bundle.getString(ARGUMENT);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_content;
    }

    @Override
    protected void initEventAndData() {
        mTvContent.setText(mArgument);
    }
}
