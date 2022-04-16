package com.bed.android.bedrock.vmodel

import androidx.lifecycle.MutableLiveData
import com.bed.android.bedrock.model.Product

class PriceViewModel {


    var url:MutableLiveData<String> = MutableLiveData()
        set(value){
            field=value
            url.postValue(value.toString())
        }
    var price: MutableLiveData<String> =MutableLiveData()
        set(value){
            field=value
            price.postValue(value.toString())
        }



}