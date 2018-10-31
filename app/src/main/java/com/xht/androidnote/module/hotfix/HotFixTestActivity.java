package com.xht.androidnote.module.hotfix;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2018/10/31.
 * 1、修改bug，rebuild Project，得到class文件，通过AndroidSdk中的build-tools下的dex.bat，生成dex文件
 * 2、获取应用程序的dex文件与要修复的dex文件，获取相应的pathList和dexElements，合并为一个Element[]
 * 3、通过反射设置最终的dexElements
 */

public class HotFixTestActivity extends BaseActivity {
    @BindView(R.id.btn_test)
    Button mBtnTest;
    @BindView(R.id.btn_hot_fix)
    Button mBtnHotFix;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hot_fix_test;
    }

    @Override
    protected void initEventAndData() {

    }

    @OnClick({R.id.btn_test, R.id.btn_hot_fix})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_test:
                SimpleHotFixBugTest test = new SimpleHotFixBugTest();
                test.getBug(mContext);
                break;
            case R.id.btn_hot_fix:
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    FixDexUtils.loadFixedDex(mContext, Environment.getExternalStorageDirectory());
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FixDexUtils.loadFixedDex(mContext, Environment.getExternalStorageDirectory());
                } else {
                    Toast.makeText(mContext, "Permission denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
