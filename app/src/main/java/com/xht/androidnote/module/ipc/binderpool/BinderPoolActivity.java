package com.xht.androidnote.module.ipc.binderpool;

import android.os.IBinder;
import android.os.RemoteException;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;

/**
 * Created by xht on 2018/7/4.
 */

public class BinderPoolActivity extends BaseActivity {
    private ISecurityCenter mSecurityCenter;
    private ICompute mCompute;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_binder_pool;
    }

    @Override
    protected void initEventAndData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();
    }

    private void doWork() {
        BinderPool binderPool = BinderPool.getInstance(this);
        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        mSecurityCenter = SecurityCenterImpl.asInterface(securityBinder);

        L.i("BinderPoolActivity---visit ISecurityCenter");

        String msg = "hello world~";
        L.i("BinderPoolActivity---content: " + msg);

        try {
            String password = mSecurityCenter.encrypt(msg);
            L.i("BinderPoolActivity---encrypt: " + password);
            L.i("BinderPoolActivity---decrypt: " + mSecurityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        L.i("BinderPoolActivity---visit ICompute");
        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);

        mCompute = ComputeImpl.asInterface(computeBinder);
        try {
            L.i("BinderPoolActivity--- 3+5 = " + mCompute.add(3, 5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


}
