package com.bed.android.bedrock.vmodel

import androidx.lifecycle.MutableLiveData
import com.bed.android.bedrock.model.Product

class ProductViewModel {

    val img: MutableLiveData<String?> = MutableLiveData()
    val lowestPrice: MutableLiveData<String?> = MutableLiveData()


    var product: Product?=null
        set(product){
            field=product
            img.postValue(product?.img)
            lowestPrice.postValue(product?.lowestPrice)

        }





}