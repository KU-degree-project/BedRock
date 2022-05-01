package com.bed.android.bedrock.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Store(
    val thumbnail:String,
    val price:String,
    val storeLink:String
) : Parcelable