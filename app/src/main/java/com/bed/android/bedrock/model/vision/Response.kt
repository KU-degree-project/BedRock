package com.bed.android.bedrock.model.vision

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Response(
    @SerializedName("@type")
    val type: String,
    val referenceImages: List<ReferenceImage>,
    val statuses: List<Statuse>
) : Serializable