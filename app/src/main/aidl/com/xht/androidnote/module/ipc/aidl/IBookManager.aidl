// IBookManager.aidl
package com.xht.androidnote.module.ipc.aidl;

import com.xht.androidnote.module.ipc.aidl.Book;
import com.xht.androidnote.module.ipc.aidl.IOnNewBookArrivedListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);

    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
