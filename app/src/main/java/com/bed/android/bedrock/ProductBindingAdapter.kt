package com.bed.android.bedrock

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bed.android.bedrock.module.GlideApp

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, url: String?) {
    if(url!=null){
        GlideApp.with(imageView.context).load(url).into(imageView)
    }
}


