package com.bed.android.bedrock.vmodel

import androidx.lifecycle.MutableLiveData
import com.bed.android.bedrock.model.Product

class TabPriceViewModel {

    val lowestPrice: MutableLiveData<String?> = MutableLiveData()
    val priceList: MutableLiveData<List<Triple<String,String, String>>> = MutableLiveData()


    var product: Product?=null
        set(product){
            field=product
            lowestPrice.postValue(product?.lowestPrice)
            priceList.postValue(product?.priceList)

        }
}