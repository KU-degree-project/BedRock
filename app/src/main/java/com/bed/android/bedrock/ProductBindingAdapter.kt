package com.bed.android.bedrock

import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.module.GlideApp
import com.bed.android.bedrock.module.GlideModule
import com.bed.android.bedrock.ui.DetailBtnFragment
import com.bumptech.glide.Glide

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, url: String?) {
    if(url!=null){
        GlideApp.with(imageView.context).load(url).into(imageView)
    }
}


