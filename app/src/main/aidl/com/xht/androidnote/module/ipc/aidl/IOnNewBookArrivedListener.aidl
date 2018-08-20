// IOnNewBookArrivedListener.aidl
package com.xht.androidnote.module.ipc.aidl;

import com.xht.androidnote.module.ipc.aidl.Book;

interface IOnNewBookArrivedListener {

    void onNewBookArrived(in Book newBook);

}
