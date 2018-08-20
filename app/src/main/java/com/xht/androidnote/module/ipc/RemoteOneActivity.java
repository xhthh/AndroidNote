package com.xht.androidnote.module.ipc;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.ipc.model.User;
import com.xht.androidnote.module.ipc.model.UserManager;
import com.xht.androidnote.utils.CommonUtils;
import com.xht.androidnote.utils.L;
import com.xht.androidnote.utils.MyConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by xht on 2018/6/27.
 */

public class RemoteOneActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_remote_one;
    }

    @Override
    protected void initEventAndData() {
        L.i("RemoteOneActivity---sUserId==" + UserManager.sUserId);
        recoverFromFile();
    }

    private void recoverFromFile() {
        User user = null;
        File dir = new File(MyConstants.PATH);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File cachedFile = new File(MyConstants.CACHED_PATH);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(cachedFile));
            user = (User) objectInputStream.readObject();
            L.i("RemoteOneActivity---persist user：" + user);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            CommonUtils.close(objectInputStream);
        }
    }
}
