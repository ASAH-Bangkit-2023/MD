package com.asahteam.md.remote.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @field:SerializedName("detail")
    val detail: List<DetailResponse>
)

data class DetailResponse(
    @field:SerializedName("msg")
    val message: String,

    @field:SerializedName("type")
    val type: String
)