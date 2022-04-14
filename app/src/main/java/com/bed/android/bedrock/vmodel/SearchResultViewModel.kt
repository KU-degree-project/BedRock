package com.bed.android.bedrock.vmodel

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.ui.DetailBtnFragment
import com.bed.android.bedrock.ui.MainActivity
import com.bed.android.bedrock.ui.SearchBarFragment

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