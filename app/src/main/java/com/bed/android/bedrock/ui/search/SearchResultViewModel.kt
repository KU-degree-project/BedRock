package com.bed.android.bedrock.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.ui.MainActivity.Companion.croller
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchResultViewModel : ViewModel() {

    private val _productList = MutableLiveData<List<Product>>(emptyList())
    val productList: LiveData<List<Product>> = _productList

    fun startCrawling(keyword: String, callback: (Int) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = croller.croll_list(keyword)

            _productList.postValue(result)

            withContext(Dispatchers.Main) {
                callback(result.size)
            }
        }
    }
}
