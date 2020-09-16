package com.xht.androidnote.module.retrofit;

import com.xht.androidnote.utils.L;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xht on 2018/5/17.
 */

public class RetrofitClient {

    // java.lang.IllegalArgumentException: baseUrl must end in /
    private static final String BASE_URL = "http://api.lovek12.com/v1911/";

    private static Retrofit sRetrofit;

    private RetrofitClient() {
    }

    public static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new LoggingInterceptor());
            OkHttpClient okHttpClient = builder.build();

//            GsonBuilder gsonBuilder = new GsonBuilder();
//            Gson gson = gsonBuilder.enableComplexMapKeySerialization().create();
//            gson.toJson()

            sRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return sRetrofit;
    }

    public static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            L.i("xht", String.format(Locale.CHINA, "Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            L.i("xht", String.format(Locale.CHINA, "Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }
}
