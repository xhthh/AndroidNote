package com.xht.androidnote.module.material;

import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.widget.LinearLayout;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.ScreenUtils;

import butterknife.BindView;

/**
 * Created by xht on 2019/4/22.
 */

public class DrawerLayoutActivity extends BaseActivity {
    @BindView(R.id.drawer)
    LinearLayout mDrawer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_drawerlayout;
    }

    @Override
    protected void initEventAndData() {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void setLpDynamic() {
        int drawerWidth = (int)(77f / 100f * ScreenUtils.getWidth(this));
        DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(drawerWidth, DrawerLayout.LayoutParams.MATCH_PARENT);
        mDrawer.setLayoutParams(layoutParams);
    }
}
