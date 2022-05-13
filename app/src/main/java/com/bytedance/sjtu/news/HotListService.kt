package com.bytedance.sjtu.news

import retrofit2.Call
import retrofit2.http.*

interface HotListService {

    @GET("get")
    fun getHotList(
        @Query("type") type: String,
        @Query("token") token: String,
    ): Call<HotListBean>

}