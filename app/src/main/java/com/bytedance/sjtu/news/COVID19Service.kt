package com.bytedance.sjtu.news

import com.bytedance.sjtu.news.covidBean.COVID19Bean
import retrofit2.Call
import retrofit2.http.*

interface COVID19Service {

    @GET("list")
    fun getData(@Query("modules") modules: String): Call<COVID19Bean>

}