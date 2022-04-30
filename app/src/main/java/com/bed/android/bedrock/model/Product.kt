package com.bed.android.bedrock.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    var id:String="",
    var img:String="",
    var des:String="",
    var name:String="",
    var priceList:List<Triple<String,String,String>>,
    var product_link:String="",
    var lowestPrice:String) : Parcelable