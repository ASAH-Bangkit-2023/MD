package com.asahteam.md.remote.response

import com.google.gson.annotations.SerializedName

data class BlogResponse(

    @field:SerializedName("thumbnail")
    val thumbnail: String,

    @field:SerializedName("date_news")
    val dateNews: String,

    @field:SerializedName("author")
    val author: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("content")
    val content: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("news_id")
    val newsId: Int
)
