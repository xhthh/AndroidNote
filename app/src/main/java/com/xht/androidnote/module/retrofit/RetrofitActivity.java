package com.xht.androidnote.module.retrofit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.bean.retrofit.CourseBean;
import com.xht.androidnote.bean.retrofit.FeedBackBean;
import com.xht.androidnote.bean.retrofit.UpLoadUserBean;
import com.xht.androidnote.utils.L;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by xht on 2018/5/16.
 */

public class RetrofitActivity extends BaseActivity {
    @BindView(R.id.btn_retrofit_get)
    Button mBtnRetrofitGet;
    @BindView(R.id.btn_retrofit_post)
    Button mBtnRetrofitPost;
    @BindView(R.id.btn_retrofit_post_string)
    Button mBtnRetrofitPostString;
    @BindView(R.id.btn_retrofit_post_upload)
    Button mBtnRetrofitPostUpload;
    @BindView(R.id.btn_retrofit_download_display_img)
    Button mBtnRetrofitDownloadDisplayImg;
    @BindView(R.id.btn_retrofit_cache)
    Button mBtnRetrofitCache;
    @BindView(R.id.tv_retrofit_get_result)
    TextView mTvRetrofitGetResult;
    @BindView(R.id.iv_retrofit_image)
    ImageView mIvRetrofitImage;

    private BottomSheetDialog bottomSheetDialog;

    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;
    private String imagePath;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_retrofit;
    }

    @Override
    protected void initEventAndData() {

        //=============Java动态代理获取注解、方法名、方法参数===========
        ITest iTest = (ITest) Proxy.newProxyInstance(ITest.class.getClassLoader(), new
                Class<?>[]{ITest.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Integer a = (Integer) args[0];
                Integer b = (Integer) args[1];
                L.i("方法名：" + method.getName());
                L.i("a==" + a + "  b==" + b);

                GET get = method.getAnnotation(GET.class);
                L.i("注解：" + get.value());
                return null;
            }
        });
        iTest.add(2, 3);

    }

    @OnClick({R.id.btn_retrofit_get, R.id.btn_retrofit_post, R.id.btn_retrofit_post_string, R.id
            .btn_retrofit_post_upload, R.id
            .btn_retrofit_download_display_img, R.id.btn_retrofit_cache})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_retrofit_get:
                getRequest();
                break;
            case R.id.btn_retrofit_post:
                postRequest();
                break;
            case R.id.btn_retrofit_post_string:
                break;
            case R.id.btn_retrofit_post_upload:
                showSelectDialog();
                break;
            case R.id.btn_retrofit_download_display_img:
                break;
            case R.id.btn_retrofit_cache:
                break;
        }
    }

    private void showSelectDialog() {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.item_choose_photo);
        }

        bottomSheetDialog.findViewById(R.id.tv_take_photo).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                takePhoto();
            }
        });

        bottomSheetDialog.findViewById(R.id.tv_album).setOnClickListener(new View.OnClickListener
                () {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission
                        .WRITE_EXTERNAL_STORAGE) != PackageManager
                        .PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission
                                    .WRITE_EXTERNAL_STORAGE},
                            ALBUM_REQUEST_CODE);
                } else {
                    selectAlbumPhoto();
                }
            }
        });
        bottomSheetDialog.findViewById(R.id.tv_cancel_pic).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    /**
     * 从相册选择图片
     */
    private void selectAlbumPhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }

    /**
     * 拍照
     */
    private void takePhoto() {

    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        /*Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image*//*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);*/


        Intent intent = new Intent("com.android.camera.action.CROP");// 调用android系统自带的一个图片剪裁页面
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 150);// 输出图片的大小
        intent.putExtra("outputY", 150);
        //是否将数据保留在Bitmap中返回
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imagePath)));
        //设置输出的格式
        //intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());

        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    /**
     * 上传头像
     *
     * @param imagePath
     */
    private void uploadPic(String imagePath) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new RetrofitClient.LoggingInterceptor());

        OkHttpClient okHttpClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("http://api.lovek12.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);


        L.i("uploadpic()---imagePath==" + imagePath);
        File file = new File(imagePath);

        final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data")
                , file);


        /*MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("device_type", "ad1")
                .addFormDataPart("user_id", "1269")//ParamKey.TOKEN 自定义参数key常量类，即参数名
                .addFormDataPart("UserInfo[avatar]", file.getName(), requestBody);

        List<MultipartBody.Part> partList = multipartBodyBuilder.build().parts();

        Call<UpLoadUserBean> call = service.uploadPic(partList);*/

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder()
                .addFormDataPart("UserInfo[avatar]", file.getName(), requestBody);
        MultipartBody.Part part = multipartBodyBuilder.build().part(0);

        Call<UpLoadUserBean> call = service.uploadPic("ad1", "1269", part);

        call.enqueue(new Callback<UpLoadUserBean>() {
            @Override
            public void onResponse(Call<UpLoadUserBean> call, Response<UpLoadUserBean> response) {
                L.i("uploadPic()---onResponse===" + response.body().getData());
                if (response != null && response.body() != null) {
                    L.i("上传结果===" + response.body().getNote());
                    mTvRetrofitGetResult.setText(response.body().getNote());
                }
            }

            @Override
            public void onFailure(Call<UpLoadUserBean> call, Throwable t) {
                L.i("uploadPic()---onFailure()---上传失败");
            }
        });

    }

    /**
     * post请求
     */
    private void postRequest() {
        Retrofit retrofit = RetrofitClient.getRetrofit();
        ApiService service = retrofit.create(ApiService.class);

        Call<FeedBackBean> call = service.feedBack("My Life`s Getting Better", "1269");
        call.enqueue(new Callback<FeedBackBean>() {
            @Override
            public void onResponse(Call<FeedBackBean> call, Response<FeedBackBean> response) {
                L.i("postRequest()---onResponse---response==" + response.body().toString());
                mTvRetrofitGetResult.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<FeedBackBean> call, Throwable t) {
                L.i("postRequest()---onFailure---请求失败");
            }
        });
    }

    /**
     * 一般的GET请求
     * http://api.lovek12.com/v1911/index.php?r=resource/index-app&device_type=ad1
     */
    private void getRequest() {
        Retrofit retrofit = RetrofitClient.getRetrofit();
        ApiService service = retrofit.create(ApiService.class);
        Call<CourseBean> call = service.getCourseList("resource/get-resource-by-gradex", "0",
                "0", "1", "10");

        /*try {
            call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        call.enqueue(new Callback<CourseBean>() {
            @Override
            public void onResponse(Call<CourseBean> call, Response<CourseBean> response) {
                L.i("getRequest()---onResponse---response==" + response.body().toString());
                mTvRetrofitGetResult.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<CourseBean> call, Throwable t) {
                L.i("getRequest()---onFailure---请求失败");
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    selectAlbumPhoto();
                } else {
                    Toast.makeText(mContext, "You denied the permission!", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case CAMERA_REQUEST_CODE:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case CROP_REQUEST_CODE://调用剪裁后返回
                if (resultCode == RESULT_OK && intent != null) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        //在这里获得了剪裁后的Bitmap对象，可以用于上传
                        Bitmap image = bundle.getParcelable("data");
                        //设置到ImageView上
                        mIvRetrofitImage.setImageBitmap(image);
                        //也可以进行一些保存、压缩等操作后上传
                        uploadPic(imagePath);
                    }
                }
                break;
            case ALBUM_REQUEST_CODE://调用相册后返回
                if (intent != null) {
                    //Uri uri = geturi(intent);
                    Uri uri = intent.getData();

                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = mContext.managedQuery(uri, proj, null, null, null);
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    imagePath = cursor.getString(columnIndex);

                    cropPhoto(intent.getData());
                }
                break;
            case CAMERA_REQUEST_CODE:

                break;
        }
    }

    /*public Uri geturi(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = mContext.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append
                ("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new
                String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri.parse("content://media/external/images/media/" + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }*/
}
