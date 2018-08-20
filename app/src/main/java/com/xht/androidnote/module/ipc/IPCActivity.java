package com.xht.androidnote.module.ipc;

import android.view.View;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.ipc.aidl.AIDLActivity;
import com.xht.androidnote.module.ipc.binderpool.BinderPoolActivity;
import com.xht.androidnote.module.ipc.messenger.MessengerActivity;
import com.xht.androidnote.module.ipc.model.Student;
import com.xht.androidnote.module.ipc.model.User;
import com.xht.androidnote.module.ipc.model.UserManager;
import com.xht.androidnote.utils.CommonUtils;
import com.xht.androidnote.utils.L;
import com.xht.androidnote.utils.MyConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import butterknife.OnClick;

/**
 * Created by xht on 2018/6/27.
 * 进程间通信
 * 1、Bundle
 * 2、文件共享
 * 3、Messenger
 * 4、AIDL
 * <p>
 * 5、ContentProvider
 * 6、Socket
 */

public class IPCActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_ipc;
    }

    @Override
    protected void initEventAndData() {
        UserManager.sUserId = 2;
        L.i("IPCActivity---sUserId==" + UserManager.sUserId);

        /*try {
            serialization();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            deserialization();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        persistToFile();


    }

    private void persistToFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User(1, "hello fuck", false);
                File dir = new File(MyConstants.PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File cachedFile = new File(MyConstants.CACHED_PATH);
                ObjectOutputStream objectOutputStream = null;
                try {
                    objectOutputStream = new ObjectOutputStream(new FileOutputStream(cachedFile));
                    L.i("IPCActivity---persist");
                    objectOutputStream.writeObject(user);
                    L.i("IPCActivity---persist user：" + user);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    CommonUtils.close(objectOutputStream);
                }

            }
        }).start();
    }

    @OnClick({R.id.btn_remote_one, R.id.btn_remote_two, R.id.btn_binder, R.id.btn_messenger, R.id
            .btn_aidl, R.id.btn_binder_pool})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_remote_one:
                skip2Activity(RemoteOneActivity.class);
                break;
            case R.id.btn_remote_two:
                skip2Activity(RemoteTwoActivity.class);
                break;
            case R.id.btn_binder:
                skip2Activity(BinderActivity.class);
                break;
            case R.id.btn_messenger:
                skip2Activity(MessengerActivity.class);
                break;
            case R.id.btn_aidl:
                skip2Activity(AIDLActivity.class);
                break;
            case R.id.btn_binder_pool:
                skip2Activity(BinderPoolActivity.class);
                break;

        }
    }

    private void deserialization() throws ClassNotFoundException, IOException {
        //反序列化过程
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("cache.txt"));
        Student newStudent = (Student) in.readObject();
        in.close();
    }

    private void serialization() throws IOException {
        //序列化过程
        Student student = new Student(0, "Jason", false);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream
                ("cache.txt"));
        out.writeObject(student);
        out.close();
    }


}
