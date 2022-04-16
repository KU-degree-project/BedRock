package com.bed.android.bedrock.vmodel

import androidx.lifecycle.MutableLiveData
import com.bed.android.bedrock.model.Product

class TabDescViewModel {
    val productDescription: MutableLiveData<String?> = MutableLiveData()


    var product: Product?=null
        set(product){
            field=product
            productDescription.postValue(product?.des)

        }
}