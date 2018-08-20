package com.xht.androidnote.module.okhttp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.utils.L;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xht on 2018/4/23.
 *
 * OkHttp的基本使用
 */

public class OkHttpActivity extends BaseActivity {

    @BindView(R.id.btn_okhttp_get)
    Button mBtnOkhttpGet;
    @BindView(R.id.btn_okhttp_post)
    Button mBtnOkhttpPost;
    @BindView(R.id.tv_okhttp_get_result)
    TextView mTvOkhttpGetResult;
    @BindView(R.id.iv_okhttp_image)
    ImageView mIvOkhttpImage;

    private String mBaseUrl = "http://192.168.7.95:8080/OkHttp/";

    private OkHttpClient okHttpClient;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_okhttp;
    }

    @Override
    protected void initEventAndData() {
        okHttpClient = new OkHttpClient()
                .newBuilder()
                .cookieJar(new CookiesManager())
                .build();
    }

    @OnClick({R.id.btn_okhttp_get, R.id.btn_okhttp_post, R.id.btn_okhttp_post_string,
            R.id.btn_okhttp_post_file, R.id.btn_okhttp_post_upload, R.id.btn_okhttp_download,
            R.id.btn_okhttp_download_display_img, R.id.btn_okhttp_cache})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_okhttp_get:
                getRequest();
                break;
            case R.id.btn_okhttp_post:
                postRequest();
                break;
            case R.id.btn_okhttp_post_string:
                postString();
                break;
            case R.id.btn_okhttp_post_file:
                requestStoragePermission();
                break;
            case R.id.btn_okhttp_post_upload:
                posUpload();
                break;
            case R.id.btn_okhttp_download:
                download();
                break;
            case R.id.btn_okhttp_download_display_img:
                downloadAndDisplay();
                break;
            case R.id.btn_okhttp_cache:
                cacheTest();
                break;

        }
    }

    private void cacheTest() {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .cache(new Cache(new File("cache"), 24 * 1024 * 1024))
                .build();

        Request request = new Request
                .Builder()
                .url("http://www.baidu.com")
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadAndDisplay() {
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(mBaseUrl + "files/james.jpg")
                .get()
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure()==" + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                InputStream is = response.body().byteStream();

                final Bitmap bitmap = BitmapFactory.decodeStream(is);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        L.e("download display success");
                        mIvOkhttpImage.setImageBitmap(bitmap);
                    }
                });

            }
        });
    }

    private void download() {
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(mBaseUrl + "files/james.jpg")
                .get()
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure()==" + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final long total = response.body().contentLength();
                long sum = 0L;

                InputStream is = response.body().byteStream();

                File file = new File(Environment.getExternalStorageDirectory(), "LeBron.jpg");
                FileOutputStream fos = new FileOutputStream(file);

                int len = 0;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);

                    sum += len;
                    L.e(sum + " / " + total);

                    final long finalSum = sum;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvOkhttpGetResult.setText(finalSum + " / " + total);
                        }
                    });
                }

                fos.flush();
                fos.close();
                is.close();

                L.e("download success");
            }
        });
    }

    private void posUpload() {
        File file = new File(Environment.getExternalStorageDirectory(), "james.jpg");


        if (!file.exists()) {
            L.e(file.getAbsolutePath() + " not exists");
            return;
        }

        MediaType mediaType = MediaType.parse("application/octet-stream;charset=utf-8");

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        RequestBody requestBody = multipartBuilder.setType(MultipartBody.FORM)
                .addFormDataPart("userName", "xhthh")
                .addFormDataPart("passWord", "233")
                .addFormDataPart("mPhoto", "LeBron.jpg", RequestBody.create(mediaType, file))
                .build();

        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(long byteWrited, long contentLength) {
                L.e(byteWrited + " / " + contentLength);
            }
        });


        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(mBaseUrl + "uploadInfo")
                .post(countingRequestBody)
                .build();

        excuteRequest(request);
    }

    private void postFile() {
        File file = new File(Environment.getExternalStorageDirectory(), "james.jpg");


        if (!file.exists()) {
            L.e(file.getAbsolutePath() + " not exists");
            return;
        }

        MediaType mediaType = MediaType.parse("application/octet-stream;charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, file);


        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(mBaseUrl + "postFile")
                .post(requestBody)
                .build();

        excuteRequest(request);
    }

    private void postString() {
        MediaType mediaType = MediaType.parse("text/plain;charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, "草泥马");


        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(mBaseUrl + "postString")
                .post(requestBody)
                .build();

        excuteRequest(request);
    }

    private void postRequest() {
        // 1、创建okHttpClient对象
        //OkHttpClient okHttpClient = new OkHttpClient();

        // 2、构造request
        // 2.1构造requestBody
        FormBody formBody = new FormBody.Builder()
                .add("userName", "孙悟空")
                .add("passWord", "龟派气功")
                .build();

        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(mBaseUrl + "login")
                .post(formBody)
                .build();

        // 3、将request封装为call
        // 4、执行call
        excuteRequest(request);
    }

    private void getRequest() {
        // 1、创建okHttpClient对象
        //OkHttpClient okHttpClient = new OkHttpClient();

        // 2、构造request
        Request.Builder builder = new Request.Builder();

        Request request = builder
                .url(mBaseUrl + "login?userName=sunwukong&passWord=1234")
                .get()
                .build();

        // 3、将request封装为call
        // 4、执行call
        excuteRequest(request);
    }

    private void excuteRequest(Request request) {
        Call call = okHttpClient.newCall(request);

        // 同步执行
        //call.execute();
        // 异步执行
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure()==" + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String result = response.body().string();
                L.i("onResponse()==" + result);

                //java.lang.IllegalStateException: closed
                //原因为OkHttp请求回调中response.body().string()只能有效调用一次
                //在调用了response.body().string()方法之后，response中的流会被关闭，再次调用会报错

                //Only the original thread that created a view hierarchy can touch its views.
                //mTvOkhttpGetResult.setText(result);
                // 当前为子线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvOkhttpGetResult.setText(result);
                    }
                });

            }
        });
    }

    public void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            postFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    postFile();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
