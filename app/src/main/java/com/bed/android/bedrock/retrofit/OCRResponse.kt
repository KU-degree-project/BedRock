package com.bed.android.bedrock.retrofit

import com.google.gson.annotations.SerializedName

data class OCRResponse(
    @SerializedName("result")
    val result: List<OCRResult?>
)
