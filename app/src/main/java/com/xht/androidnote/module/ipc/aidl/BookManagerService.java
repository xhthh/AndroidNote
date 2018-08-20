package com.xht.androidnote.module.ipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.xht.androidnote.utils.L;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by xht on 2018/7/3.
 */

public class BookManagerService extends Service {

    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    /*private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListenerList = new
            CopyOnWriteArrayList<>();*/

    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new
            RemoteCallbackList<>();

    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            // 模拟耗时操作
            SystemClock.sleep(5000);
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            /*if (!mListenerList.contains(listener)) {
                mListenerList.add(listener);
            } else {
                L.i("BookManagerService---already exists.");
            }
            L.i("BookManagerService---registerListener，size：" + mListenerList.size());*/


            mListenerList.register(listener);
            final int N = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            L.i("BookManagerService---registerListener, current size:" + N);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            /*if (mListenerList.contains(listener)) {
                mListenerList.remove(listener);
                L.i("BookManagerService---unregisterListener succeed.");
            } else {
                L.i("BookManagerService---unregisterListener---not found, can not unregister.");
            }
            L.i("BookManagerService---unregisterListener，current size：" + mListenerList.size());*/


            boolean success = mListenerList.unregister(listener);
            if (success) {
                L.i("BookManagerService---unregister success.");
            } else {
                L.i("BookManagerService---not found, can not unregister.");
            }
            final int N = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            L.i("BookManagerService---unregisterListener, current size:" + N);
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "IOS"));

        new Thread(new ServiceWorker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("com.xht.androidnote." +
                "permission.ACCESS_BOOK_SERVICE");
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed.set(true);
        super.onDestroy();
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        /*L.i("BookManagerService---onNewBookArrived, notify listeners：" + mListenerList.size());
        for (int i = 0; i < mListenerList.size(); i++) {
            IOnNewBookArrivedListener listener = mListenerList.get(i);
            L.i("BookManagerService---onNewBookArrived, notify listener：" + listener);
            listener.onNewBookArrived(book);
        }*/

        int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener listener = mListenerList.getBroadcastItem(i);
            if (listener != null) {
                listener.onNewBookArrived(book);
            }
        }
        mListenerList.finishBroadcast();
    }

    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            while (!mIsServiceDestroyed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new Book#" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
