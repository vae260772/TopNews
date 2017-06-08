package com.example.lihui20.testhttp.service;

import com.example.lihui20.testhttp.model.Data;

import java.util.List;

import retrofit.Call;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by lihui20 on 2016/12/6.
 */
public interface  HttpService {

    @POST("toutiao/index")
    Call<List<Data>> getData(@Query("type") String type, @Query("key") String key);
}
