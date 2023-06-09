package com.asahteam.md.remote.response

import com.google.gson.annotations.SerializedName

data class ScanResponse (
    @field:SerializedName("gcs_image_path")
    val gcsImagePath : String,

    @field:SerializedName("prediction_waste")
    val predictionWaste : String,

    @field:SerializedName("message")
    val message : String,

    @field:SerializedName("action")
    val action : String




)