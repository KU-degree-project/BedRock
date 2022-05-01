package com.bed.android.bedrock.vmodel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bed.android.bedrock.model.SearchRecord

class MyPastKeywordViewModel:ViewModel() {
    private val records = mutableListOf<SearchRecord>()
    var keyAndDate = MutableLiveData<List<SearchRecord>>()

    fun addRecord(record: SearchRecord) {
        records.add(record)
        keyAndDate.value = records
    }

    fun removeRecord(record: SearchRecord) {
        records.remove(record)
        keyAndDate.value = records
    }
}