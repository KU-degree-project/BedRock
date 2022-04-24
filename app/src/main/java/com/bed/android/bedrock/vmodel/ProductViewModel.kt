package com.bed.android.bedrock.vmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bed.android.bedrock.model.Product

class ProductViewModel: ViewModel() {

    val img: MutableLiveData<String?> = MutableLiveData()
    val lowestPrice: MutableLiveData<String?> = MutableLiveData()
    val productName:MutableLiveData<String?> = MutableLiveData()
    val productDescription:MutableLiveData<String?> = MutableLiveData()
    val productLink:MutableLiveData<String?> = MutableLiveData()
    val priceList: MutableLiveData<List<Triple<String,String, String>>> = MutableLiveData()

    var product: Product?=null
        set(product){
            field=product
            img.postValue(product?.img)
            lowestPrice.postValue(product?.lowestPrice)
            productName.postValue(product?.name)
            productDescription.postValue(product?.des)
            productLink.postValue(product?.product_link)
            priceList.postValue(product?.priceList)

        }





}