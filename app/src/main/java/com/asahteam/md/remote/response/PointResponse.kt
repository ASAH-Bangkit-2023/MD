package com.asahteam.md.remote.response

import com.google.gson.annotations.SerializedName

data class PointResponse(

	@field:SerializedName("total_points")
	val totalPoints: Int,

	@field:SerializedName("username")
	val username: String
)
