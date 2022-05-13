package com.bytedance.sjtu.news

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface NewsService {

    //获取新闻
    @GET("news")
    fun getNews(
        @Query("page") page: String,
        @Query("count") count: String
    ): Call<NewsBean>

    //获取评论
    @GET("comment")
    fun getComment(
        @Query("title") title: String
    ): Call<CommentBean>

    //发布评论
    @POST("comment")
    fun postComment(
        @Body postCommentInfo: PostCommentInfo
    ): Call<postCommentBean>

    //删除评论
    @DELETE("comment")
    fun deleteComment(
        @Query("student_id") student_id: String,
        @Query("title") title: String,
        @Query("comment_id") comment_id: String
    ): Call<deleteCommentBean>

}