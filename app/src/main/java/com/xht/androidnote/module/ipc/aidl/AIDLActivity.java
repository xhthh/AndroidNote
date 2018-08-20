package com.xht.androidnote.module.ipc.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by xht on 2018/7/3.
 */

public class AIDLActivity extends BaseActivity {

    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private IBookManager mRemoteBookManager;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    L.i("AIDLActivity---receive new book : " + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            try {
                /*List<Book> list = bookManager.getBookList();
                L.i("AIDLActivity---query book list, list type：" + list.getClass()
                        .getCanonicalName());
                L.i("AIDLActivity---query book list：" + list.toString());*/


                /*List<Book> list = bookManager.getBookList();n
                L.i("AIDLActivity---query book list：" + list.toString());
                Book newBook = new Book(3, "福尔摩斯探案集");
                bookManager.addBook(newBook);
                List<Book> newList = bookManager.getBookList();
                L.i("AIDLActivity---query book newList：" + newList.toString());*/

                mRemoteBookManager = bookManager;
                List<Book> list = bookManager.getBookList();
                L.i("AIDLActivity---query book list, list type：" + list.getClass()
                        .getCanonicalName());
                L.i("AIDLActivity---query book list：" + list.toString());
                Book newBook = new Book(3, "福尔摩斯探案集");
                bookManager.addBook(newBook);
                L.i("AIDLActivity---add book：" + newBook);
                List<Book> newList = bookManager.getBookList();
                L.i("AIDLActivity---query book newList：" + newList.toString());
                bookManager.registerListener(mOnNewBookArrivedListener);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            L.i("AIDLActivity---binder died.");
        }
    };

    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener
            .Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_aidl;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    protected void onDestroy() {
        unRegistAndBind();
        super.onDestroy();
    }

    @OnClick({R.id.btn_aidl_start, R.id.btn_aidl_stop, R.id.btn_aidl_bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_aidl_bind:
                Intent intent = new Intent(mContext, BookManagerService.class);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn_aidl_start:
                Toast.makeText(mContext, "click button", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (mRemoteBookManager != null) {
                            try {
                                // 点击调用远程耗时方法，出现ANR
                                // 解决方法：把调用放在非 UI 线程中
                                mRemoteBookManager.getBookList();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                break;
            case R.id.btn_aidl_stop:
                unRegistAndBind();
                break;
        }
    }

    private void unRegistAndBind() {
        if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                L.i("AIDLActivity---onDestroy unregister listener: " + mOnNewBookArrivedListener);
                mRemoteBookManager.unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        unbindService(mConnection);
    }
}
