// IBinderPool.aidl
package com.xht.androidnote.module.ipc.binderpool;


interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
