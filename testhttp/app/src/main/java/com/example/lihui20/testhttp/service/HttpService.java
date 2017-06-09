package com.example.lihui20.testhttp.service;

import com.example.lihui20.testhttp.model.Data;

import java.util.List;

import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;


/**
 * Created by lihui20 on 2016/12/6.
 */
public interface HttpService {
    /*
    请求地址：http://v.juhe.cn/toutiao/index
请求参数：type=shehui&key=9f3097f4cbe47e8abb01ca3b92e49cda
请求方式：GET
     */
    @POST("toutiao/index")
    //   Call<List<Data>> getData(@Query("type") String type, @Query("key") String key);
    Observable<List<Data>> getData(@Query("type") String type, @Query("key") String key);

}
