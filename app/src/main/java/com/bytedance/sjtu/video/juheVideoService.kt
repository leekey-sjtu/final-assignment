package com.bytedance.sjtu.video

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface juheVideoService {

    @GET("billboard")
    fun getVideo(
        @Query("key") key: String,
        @Query("type") type: String,
        @Query("size") size: Int
    ): Call<juheVideoBean>

}