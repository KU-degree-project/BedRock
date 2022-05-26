package com.bed.android.bedrock.retrofit

import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface KakaoApi {
    @Multipart
    @Headers(
        "Authorization: ${OCRConstants.AUTH_HEADER}"
    )
    @POST("/v2/vision/text/ocr")
    fun getOCR(@Part image : MultipartBody.Part) : Call<OCRResponse>
}