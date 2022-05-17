package com.bed.android.bedrock.ui

import androidx.lifecycle.ViewModel
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.ui.MainActivity.Companion.croller
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {


    fun startCrawl(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            val result =  croller.croll_detail(product)


        }
    }
}