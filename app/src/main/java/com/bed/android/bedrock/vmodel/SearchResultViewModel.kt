package com.bed.android.bedrock.vmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bed.android.bedrock.model.Product

class SearchResultViewModel : ViewModel()
{

    lateinit var products: MutableLiveData<List<Product>>

    var searchKeyword:MutableLiveData<String> = MutableLiveData()
        set(value){
            field=value
            searchKeyword.postValue(value.toString())
        }
    var searchCount: MutableLiveData<String> =MutableLiveData()
        set(value){
            field=value
            searchCount.postValue(value.toString())
        }



}