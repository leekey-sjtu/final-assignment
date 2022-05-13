package com.bytedance.sjtu.news

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface JDNewsService {

    @GET("get")
    fun getJDNews(
        @Query("channel") channel: String,
        @Query("num") num: String,
        @Query("start") start: String,
        @Query("appkey") appkey: String
    ): Call<JDNewsBean>

}