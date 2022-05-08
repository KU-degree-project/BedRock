package com.bed.android.bedrock.model.vision

import java.io.Serializable

data class InputConfig(
    val gcsSource: GcsSource
) : Serializable