package com.bed.android.bedrock.vmodel

import androidx.lifecycle.MutableLiveData

class SearchResultViewModel {


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