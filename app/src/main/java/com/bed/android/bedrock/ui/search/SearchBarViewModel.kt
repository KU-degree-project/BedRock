package com.bed.android.bedrock.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bed.android.bedrock.di.Injector
import com.bed.android.bedrock.model.SearchRecord
import com.bed.android.bedrock.ui.MainActivity.Companion.croller
import kotlinx.coroutines.*
import org.json.JSONArray

class SearchBarViewModel : ViewModel() {

    private val sharedPreferences = Injector.getSharedPreferences()

    private val _searchRecord = MutableLiveData<List<SearchRecord>>()
    val searchRecord: LiveData<List<SearchRecord>>
        get() = _searchRecord

    private var popularKeywordJob: Job? = null

    init {
        loadSearchRecord()
    }

    private fun loadSearchRecord() {
        val list = mutableListOf<SearchRecord>()

        val keywords = sharedPreferences.getString("keywords", null) ?: return

        val jsonArray = JSONArray(keywords)

        (0 until jsonArray.length()).forEach {
            val json = jsonArray.getJSONObject(it)

            list.add(SearchRecord(
                json.getInt("id"),
                json.getString("name"),
                json.getString("date")
            ))
        }

        _searchRecord.postValue(list)
    }

    fun fetchPopularKeyword(callback: (List<String>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = croller.croll_keywords()

            withContext(Dispatchers.Main) {
                callback(list)
            }
        }
    }

    override fun onCleared() {
        popularKeywordJob?.cancel()

        super.onCleared()
    }
}