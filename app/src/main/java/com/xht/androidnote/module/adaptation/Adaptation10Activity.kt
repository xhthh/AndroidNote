package com.xht.androidnote.module.adaptation

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.xht.androidnote.R
import com.xht.androidnote.base.BaseActivity
import com.xht.androidnote.utils.FileUtil
import kotlinx.android.synthetic.main.activity_adaptation10.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.BufferedOutputStream

class Adaptation10Activity : BaseActivity() {
    private val TAG = "adaptation10"

    var filesDirPath: String? = null
    var cacheDirPath: String? = null
    var externalFilesDirPath: String? = null
    var externalFilesDirPicturesPath: String? = null
    var externalCacheDirPath: String? = null
    var externalStorageDirectoryPath: String? = null
    var externalStoragePublicDirectoryPath: String? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_adaptation10
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initEventAndData() {
        /*
        2022-01-11 17:36:05.979 20127-20127/com.xht.androidnote E/adaptation: 内部路径：filesDir = /data/user/0/com.xht.androidnote/files cacheDir = /data/user/0/com.xht.androidnote/cache
        2022-01-11 17:36:05.980 20127-20127/com.xht.androidnote E/adaptation: 内部路径：getExternalFilesDir = /storage/emulated/0/Android/data/com.xht.androidnote/files getExternalCacheDir = /storage/emulated/0/Android/data/com.xht.androidnote/cache
        2022-01-11 17:36:05.980 20127-20127/com.xht.androidnote E/adaptation: 外部路径：getExternalStorageDirectory() = /storage/emulated/0
         */
        filesDirPath = filesDir.path
        cacheDirPath = cacheDir.path
        /**
         * type
         * Environment.DIRECTORY_MUSIC, Environment.DIRECTORY_PODCASTS, Environment.DIRECTORY_RINGTONES,
         * Environment.DIRECTORY_ALARMS, Environment.DIRECTORY_NOTIFICATIONS, Environment.DIRECTORY_PICTURES,
         * or Environment.DIRECTORY_MOVIES.
         */
        externalFilesDirPath = getExternalFilesDir("")?.path
        externalFilesDirPicturesPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path
        externalCacheDirPath = externalCacheDir?.path
        externalStorageDirectoryPath = Environment.getExternalStorageDirectory().path
        externalStoragePublicDirectoryPath =
            Environment.getExternalStoragePublicDirectory("").absolutePath

        var pathList = "内部路径：getFilesDir() = " + filesDirPath +
                "\n\n getCacheDir() = " + cacheDirPath +
                "\n\n getExternalFilesDir() = " + externalFilesDirPath +
                "\n\n getExternalFilesDir(Environment.DIRECTORY_PICTURES) = " + externalFilesDirPicturesPath +
                "\n\n getExternalCacheDir() = " + externalCacheDirPath +
                "\n\n 外部路径：getExternalStorageDirectory() = " + externalStorageDirectoryPath +
                "\n\n getExternalStoragePublicDirectory() = " + externalStoragePublicDirectoryPath
        Log.e(TAG, pathList)
        tvPathList.text = pathList

        btnSelectPic.setOnClickListener {
//            PermissionX.init(this)
//                .permissions(
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_MEDIA_IMAGES
//                )
//                .onExplainRequestReason(ExplainReasonCallback { scope, deniedList -> })
//                .request(RequestCallback { allGranted, grantedList, deniedList ->
//                    thread {
//                        selectPic()
//                    }
//                })

            val code = android.os.Build.VERSION.SDK_INT
            Log.e(TAG, "------CODE = $code")
            doSomethingWithPermissions()
        }

        btnAndroidId.setOnClickListener {
            val androidId: String =
                Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

            Log.e(TAG, "androidId = $androidId")
            tvAndroidId.text = androidId
        }

        btnInsertFile.setOnClickListener {
            insertFile()
        }

        btnSAF.setOnClickListener {
            openDocument()
        }

        val externalFilesDir = FileUtil.getExternalFilesDir(this)
        Log.e(TAG, "FileUtil.getExternalFilesDir = $externalFilesDir")
    }

    /**
     * 打开文件选择器
     * ACTION_OPEN_DOCUMENT 和 ACTION_GET_CONTENT 有啥区别
     *
     * ACTION_PICK_IMAGES 可以
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun openDocument() {
//        //通过系统的文件浏览器选择一个文件
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        //筛选，只显示可以“打开”的结果，如文件(而不是联系人或时区列表)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        //过滤只显示图像类型文件
//        intent.type = "image/*"
//        startActivityForResult(intent, 233)
        val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
        //intent.type = "video/*"
        intent.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, 5)//多选图片
        startActivityForResult(intent, 233)
    }

    private val IMAGE_PROJECTION = arrayOf(
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media._ID
    )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 233 && resultCode == Activity.RESULT_OK) {
            //processOpenDoc(data)
            processMultiSelect(data)
        }
    }

    /**
     * Android 13 文件选择器多选，返回uri处理
     * uri 转换File路径，看网上方案是将文件复制到了沙盒环境。。。，不是原文件真实路径
     */
    private fun processMultiSelect(data: Intent?) {
        val itemCount = data?.clipData!!.itemCount
        val stringBuilder = StringBuilder()
        for (i in 0 until itemCount) {
            var uri: Uri = data.clipData!!.getItemAt(i).uri;
            Log.i(TAG, "Uri: $uri")
            Toast.makeText(this, "选择图片的 uri = $uri", Toast.LENGTH_SHORT).show()
            stringBuilder.append(
                "uri = $uri \n file.path = " + FileUtil.getFileAbsolutePath(this, uri) + "\n\n"
            )
        }
        tvSelectUri.text = stringBuilder.toString()
    }

    /**
     * 处理 Intent.ACTION_OPEN_DOCUMENT 返回
     */
    private fun processOpenDoc(data: Intent?) {
        if (data != null) {
            //获取文件uri
            val uri = data.data
            val cursor = uri?.let {
                contentResolver.query(
                    it, IMAGE_PROJECTION, null, null,
                    null, null
                )
            }
            if (cursor != null && cursor.moveToFirst()) {
                val displayName = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        IMAGE_PROJECTION[0]
                    )
                )
                val size = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))
                Log.i(TAG, "Uri: $uri")
                Log.i(TAG, "Name: $displayName")
                Log.i(TAG, "Size: $size")
                Toast.makeText(this, "选择图片的 uri = $uri", Toast.LENGTH_SHORT).show()
                tvSelectUri.text =
                    "uri = $uri \n file.path = " + FileUtil.getFileAbsolutePath(this, uri)
            }
            cursor?.close()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @AfterPermissionGranted(100)
    fun doSomethingWithPermissions() {

        val permissions = if (Build.VERSION.SDK_INT < 33) {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        } else {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

        if (EasyPermissions.hasPermissions(
                this,
                *permissions
            )
        ) {
            selectPic()
            // 分支一 : 如果有上述权限, 执行该操作
            Toast.makeText(this, "权限申请通过", Toast.LENGTH_LONG).show()
        } else {
            // 分之二 : 如果没有上述权限 , 那么申请权限
            EasyPermissions.requestPermissions(
                this,
                "权限申请原理对话框 : 描述申请权限的原理",
                100,
                *permissions
            )
        }
    }

    /**
     * 通过 ContentResolver 插入文件至外部存储，Download 文件夹下，设置文件名称、内容等信息
     */
    private fun insertFile() {
        // 操作 external.db 数据库
        // 获取 Uri 路径
        val uri = MediaStore.Files.getContentUri("external")
        // 将要新建的文件的文件索引插入到 external.db 数据库中
        // 需要插入到 external.db 数据库 files 表中, 这里就需要设置一些描述信息
        val contentValues = ContentValues()
        // 设置插入 external.db 数据库中的 files 数据表的各个字段的值
        // 设置存储路径 , files 数据表中的对应 relative_path 字段在 MediaStore 中以常量形式定义
        contentValues.put(
            MediaStore.Downloads.RELATIVE_PATH,
            "${Environment.DIRECTORY_DOWNLOADS}/MediaStore测试"
        )
        // 设置文件名称
        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, "hello.txt")
        // 设置文件标题, 一般是删除后缀, 可以不设置
        contentValues.put(MediaStore.Downloads.TITLE, "插入一条文件")
        // uri 表示操作哪个数据库 , contentValues 表示要插入的数据内容
        val insertUri = contentResolver.insert(uri, contentValues)
        // 向 Download/hello/hello.txt 文件中插入数据
        val os = contentResolver.openOutputStream(insertUri!!)
        val bos = BufferedOutputStream(os)
        bos.write("Hello World，插一条数据".toByteArray())
        bos.close()
        Toast.makeText(this, "插入成功", Toast.LENGTH_SHORT).show()
    }

    /**
     * 在手机外部存储中，有公共访问区域，即：DCIM、Movie、Music、Pictures、Alarm、Ringtones、Download等。
     * 当文件被存储在这些目录下，系统会自动将文件索引存储在媒体数据库中，
     * 媒体数据库所在位置：data/data/com.android.providers.media，该数据库有几个表，如下：
     * MediaStore.Images : 图片，存储在 DCIM/ 和 Pictures/ 目录中
     * MediaStore.Video ：视频，存储在 DCIM/、Movies/ 和 Pictures/ 目录中
     * MediaStore.Audio ：音频，存储在 Alarms/、Audiobooks/、Music/、Notifications/、Podcasts/ 和 Ringtones/ 目录中
     *
     * Android10开启分区存储后，将无法直接通过File操作这些目录，只能通过MediaStore去操作。
     */
    private fun selectPic() {
        var picUri: Uri? = null
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
            "${MediaStore.MediaColumns.DATE_ADDED} desc"
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                if (picUri == null) {
                    picUri = uri
                }
                Log.e(TAG, "image uri is $uri")
            }
            cursor.close()
        }
        //取最新的一个uri设置给ImageView
        Glide.with(this).load(picUri).into(ivPic)
        tvNewestPicUri.text =
            "最新图片 uri = $picUri \n file.path = " + FileUtil.getFilePathFromContentUri(
                picUri,
                contentResolver
            )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You must allow all the permissions.", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }
        }
    }
}