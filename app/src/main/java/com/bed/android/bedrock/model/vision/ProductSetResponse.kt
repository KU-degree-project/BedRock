package com.bed.android.bedrock.model.vision

import java.io.Serializable

data class ProductSetResponse(
    val done: Boolean,
    val metadata: Metadata,
    val name: String,
    val response: Response
) : Serializable