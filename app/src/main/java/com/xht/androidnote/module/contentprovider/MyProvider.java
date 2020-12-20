package com.xht.androidnote.module.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by xht on 2018/6/12.
 */

public class MyProvider extends ContentProvider {

    private Context mContext;
    private DBHelper mDBHelper;
    private SQLiteDatabase db;

    // ContentProvider唯一标识
    public static final String AUTOHORITY = "com.xht.androidnote";

    public static final int User_Code = 1;
    public static final int Job_Code = 2;

    // UriMatcher类使用:在ContentProvider 中注册URI
    private static UriMatcher mMatcher;

    static {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // 若URI资源路径 = content://com.xht.androidnote/user ，则返回注册码User_Code
        mMatcher.addURI(AUTOHORITY, "user", User_Code);

        // 若URI资源路径 = content://com.xht.androidnote/job ，则返回注册码Job_Code
        mMatcher.addURI(AUTOHORITY, "job", Job_Code);
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        // 在ContentProvider创建时对数据库进行初始化
        // 运行在主线程，故不能做耗时操作,此处仅作展示
        mDBHelper = new DBHelper(mContext);
        db = mDBHelper.getWritableDatabase();

        // 初始化两个表的数据(先清空两个表,再各加入一个记录)
        db.execSQL("delete from user");
        db.execSQL("insert into user values(1,'LeBron');");
        db.execSQL("insert into user values(2,'DWade');");

        db.execSQL("delete from job");
        db.execSQL("insert into job values(1,'Android');");
        db.execSQL("insert into job values(2,'iOS');");

        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // 根据URI匹配 URI_CODE，从而匹配ContentProvider中相应的表名
        String table = getTableName(uri);

        // 向该表添加数据
        db.insert(table, null, values);

        //
        mContext.getContentResolver().notifyChange(uri, null);

        /*long id = ContentUris.parseId(uri);
        L.i("MyProvider---id==" + id);*/


        return uri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String
            selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String table = getTableName(uri);

        // 查询数据
        return db.query(table, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String
            selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[]
            selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    /**
     * 根据URI匹配 URI_CODE，从而匹配ContentProvider中相应的表名
     */
    private String getTableName(Uri uri) {
        String tableName = null;
        switch (mMatcher.match(uri)) {
            case User_Code:
                tableName = DBHelper.USER_TABLE_NAME;
                break;
            case Job_Code:
                tableName = DBHelper.JOB_TABLE_NAME;
                break;
        }
        return tableName;
    }
}
