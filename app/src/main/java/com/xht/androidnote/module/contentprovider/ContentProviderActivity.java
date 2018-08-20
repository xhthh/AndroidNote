package com.xht.androidnote.module.contentprovider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xht on 2018/6/12.
 * 内容提供者
 */

public class ContentProviderActivity extends BaseActivity {
    @BindView(R.id.lv_contact)
    ListView mLvContact;

    private ArrayAdapter<String> adapter;
    private List<String> contactList = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_content_provider;
    }

    @Override
    protected void initEventAndData() {

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactList);
        mLvContact.setAdapter(adapter);


        providerTest();
    }

    private void providerTest() {
        // 设置uri
        Uri uri_user = Uri.parse("content://com.xht.androidnote/user");

        //插入表中数据
        ContentValues values = new ContentValues();
        values.put("_id", 3);
        values.put("name", "孙悟空");

        // 获取ContentResolver
        ContentResolver contentResolver = getContentResolver();
        // 通过ContentResolver根据URI向ContentProvider中插入数据
        contentResolver.insert(uri_user, values);

        //通过ContentResolver向ContentProvider中查询数据
        Cursor cursor = contentResolver.query(uri_user, new String[]{"_id", "name"}, null, null,
                null);
        while (cursor.moveToNext()) {
            L.i("query user：" + cursor.getInt(0) + " " + cursor.getString(1));
        }

        cursor.close();


        /**
         * 对job表进行操作
         */
        // 和上述类似,只是URI需要更改,从而匹配不同的URI CODE,从而找到不同的数据资源
        Uri uri_job = Uri.parse("content://com.xht.androidnote/job");

        // 插入表中数据
        ContentValues values2 = new ContentValues();
        values2.put("_id", 3);
        values2.put("job", "NBA Player");

        // 获取ContentResolver
        ContentResolver resolver2 = getContentResolver();
        // 通过ContentResolver 根据URI 向ContentProvider中插入数据
        resolver2.insert(uri_job, values2);

        // 通过ContentResolver 向ContentProvider中查询数据
        Cursor cursor2 = resolver2.query(uri_job, new String[]{"_id", "job"}, null, null, null);
        while (cursor2.moveToNext()) {
            L.i("query job:" + cursor2.getInt(0) + " " + cursor2.getString(1));
            // 将表中数据全部输出
        }
        cursor2.close();
        // 关闭游标


    }

    @OnClick(R.id.btn_read_contact)
    public void onViewClicked() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .READ_CONTACTS}, 1);
        } else {
            readContacts();
        }
    }

    private void readContacts() {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // 获取联系人姓名
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract
                            .CommonDataKinds.Phone.DISPLAY_NAME));
                    //获取联系人手机号
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract
                            .CommonDataKinds.Phone.NUMBER));
                    contactList.add(displayName + "\n" + number);
                    adapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        L.i("contactList==" + contactList);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
