package com.bed.android.bedrock

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.model.SearchRecord
import com.bed.android.bedrock.model.Store
import com.bed.android.bedrock.module.GlideApp
import com.bed.android.bedrock.ui.TabPriceList
import com.bed.android.bedrock.ui.adapter.SearchRecordAdapter
import com.bed.android.bedrock.ui.adapter.SearchResultAdapter

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, url: String?) {
    if (url != null) {
        GlideApp.with(imageView.context).load(url).into(imageView)
    }
}

@BindingAdapter("setRecordItems")
fun setRecordItems(recyclerView: RecyclerView?, list: List<SearchRecord>) {
    (recyclerView?.adapter as? SearchRecordAdapter)?.submitList(list)
}

@BindingAdapter("setSearchResultItems")
fun setSearchResultItems(recyclerView: RecyclerView?, list: List<Product>) {
    (recyclerView?.adapter as? SearchResultAdapter)?.submitList(list)
}

@BindingAdapter("setItems")
fun setItems(recyclerView: RecyclerView?, list: List<Store>) {
    (recyclerView?.adapter as? TabPriceList.PriceAdapter)?.submitList(list)
}
