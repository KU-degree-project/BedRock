package com.bed.android.bedrock.model.vision

import java.io.Serializable

data class ReferenceImage(
    val name: String,
    val uri: String
) : Serializable