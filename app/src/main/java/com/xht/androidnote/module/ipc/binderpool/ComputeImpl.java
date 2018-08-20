package com.xht.androidnote.module.ipc.binderpool;

import android.os.RemoteException;

/**
 * Created by xht on 2018/7/4.
 */

public class ComputeImpl extends ICompute.Stub {

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }

}
