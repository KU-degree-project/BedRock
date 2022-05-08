package com.bed.android.bedrock.model.vision

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Metadata(
    @SerializedName("@type")
    val type: String,
    val endTime: String,
    val state: String,
    val submitTime: String
) : Serializable