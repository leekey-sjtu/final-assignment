package com.bytedance.sjtu

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun getRetrofit(): Retrofit {

    val baseUrl = "https://bd-open-lesson.bytedance.com/api/invoke/"
    val client = OkHttpClient.Builder()
        .connectTimeout(15000, TimeUnit.MILLISECONDS)  //预留足够的时间连接服务器
        .readTimeout(15000, TimeUnit.MILLISECONDS)  //预留足够的时间处理数据，否则偶尔出现超时java.net.SocketTimeoutException: timeout
        .build()
    val factory = GsonConverterFactory.create()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(factory)
        .build()

}

fun getRetrofit(baseUrl: String): Retrofit {  //设置新的baseUrl

    val client = OkHttpClient.Builder()
        .connectTimeout(15000, TimeUnit.MILLISECONDS)  //预留足够的时间连接服务器
        .readTimeout(15000, TimeUnit.MILLISECONDS)  //预留足够的时间处理数据，否则偶尔出现超时java.net.SocketTimeoutException: timeout
        .build()
    val factory = GsonConverterFactory
        .create()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(factory)
        .build()

}