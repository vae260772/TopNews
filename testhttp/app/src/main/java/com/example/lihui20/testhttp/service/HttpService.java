package com.example.lihui20.testhttp.service;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by lihui20 on 2016/12/6.
 */
public interface  HttpService {

    @POST("toutiao/index")
    Call<ResponseBody> getData(@Query("type") String type,@Query("key") String key);
}
