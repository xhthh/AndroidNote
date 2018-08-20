package com.xht.androidnote.module.retrofit;

import com.xht.androidnote.bean.retrofit.CourseBean;
import com.xht.androidnote.bean.retrofit.FeedBackBean;
import com.xht.androidnote.bean.retrofit.UpLoadUserBean;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by xht on 2018/5/17.
 */

public interface ApiService {

    /**
     * http://api.lovek12.com/v1911/index.php?r=resource/index-app&device_type=ad1
     * @Query 表示请求参数，将会以key=value的方式拼接在url后面
     * 如果Query参数比较多，那么可以通过@QueryMap方式将所有的参数集成在一个Map统一传递
     * @param r
     * @param grade_id
     * @param course_type
     * @param page
     * @param size
     * @return
     */
    @GET("index.php")
    Call<CourseBean> getCourseList(@Query("r") String r,
                                   @Query("grade_id") String grade_id,
                                   @Query("course_type") String course_type,
                                   @Query("page") String page,
                                   @Query("size") String size);


    /**
     * http://api.lovek12.com/v1911/index.php?r=feed-back/index&content=ssdsdsdsd&user_id=1269&device_type=ad1
     * @FormUrlEncoded 将会自动将请求参数的类型调整为application/x-www-form-urlencoded，假如content传递的参数为Good Luck，
     * 那么最后得到的请求体就是content=Good+Luck
     * @param content
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?r=feed-back/index")
    Call<FeedBackBean> feedBack(@Field("content") String content,
                                @Field("user_id") String user_id);



    /**
     * 上传头像
     */
    @Multipart
    @POST("index.php?r=user/upload-avatar")
    Call<UpLoadUserBean> uploadPic(@Query("device_type") String device_type,
                                   @Query("user_id") String user_id,
                                   @Part MultipartBody.Part file);

    @Multipart
    @POST("index.php?r=user/upload-avatar")
    Call<UpLoadUserBean> uploadPic(@Part List<MultipartBody.Part> partList);
}
