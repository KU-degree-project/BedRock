package com.bed.android.bedrock.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel : ViewModel(){
    lateinit var products: MutableLiveData<List<Product>>

}