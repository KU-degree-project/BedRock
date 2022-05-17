package com.bed.android.bedrock

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bed.android.bedrock.model.SearchRecord
import com.bed.android.bedrock.model.Store
import com.bed.android.bedrock.module.GlideApp
import com.bed.android.bedrock.ui.TabPriceList
import com.bed.android.bedrock.ui.search.SearchBarFragment

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, url: String?) {
    if (url != null) {
        GlideApp.with(imageView.context).load(url).into(imageView)
    }
}

@BindingAdapter("setRecordItems")
fun setRecordItems(recyclerView: RecyclerView?, list: List<SearchRecord>) {
    (recyclerView?.adapter as? SearchBarFragment.SearchRecordAdapter)?.submitList(list)
}

@BindingAdapter("setItems")
fun setItems(recyclerView: RecyclerView?, list: List<Store>) {
    (recyclerView?.adapter as? TabPriceList.PriceAdapter)?.submitList(list)
}
