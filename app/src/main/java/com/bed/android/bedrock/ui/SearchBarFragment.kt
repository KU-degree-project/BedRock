package com.bed.android.bedrock.ui


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentSearchBarBinding
import com.bed.android.bedrock.databinding.ListItemSearchrecordBinding
import com.bed.android.bedrock.model.SearchRecord
import com.bed.android.bedrock.ui.MainActivity.Companion.croller
import com.bed.android.bedrock.vmodel.MyPastKeywordViewModel
import com.bed.android.bedrock.vmodel.PopularKeywordViewModel
import com.bed.android.bedrock.vmodel.SearchRecordViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import java.util.stream.Collectors
import kotlin.math.min

private const val TAG = "KeywordLoad"

class SearchBarFragment : Fragment() {

    interface Callbacks {
        fun onSearchBtnClicked(searchText:String)
    }
    private var callbacks: Callbacks? = null
    private lateinit var binding: FragmentSearchBarBinding
    private val myPastKeywordViewModel: MyPastKeywordViewModel by lazy {
        ViewModelProvider(this).get(MyPastKeywordViewModel::class.java)
    }
    private val popularKeywordViewModel: PopularKeywordViewModel by lazy {
        ViewModelProvider(this).get(PopularKeywordViewModel::class.java)
    }
    private lateinit var animation: ObjectAnimator
    private lateinit var recordRecyclerView: RecyclerView
    private var adapter: SearchRecordAdapter? = SearchRecordAdapter(diffUtil)


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 최근 검색어 불러오기
        loadPastKeywords()

        // 인기 키워드 불러오기
        popularKeywordViewModel.keyword = MutableLiveData()
        popularKeywordViewModel.k_idx = MutableLiveData()
        GlobalScope.launch(Dispatchers.IO) {
            popularKeywordViewModel.keywords = croller.croll_keywords()
            popularKeywordViewModel.k_idx.postValue(0)
            popularKeywordViewModel.keyword.postValue(popularKeywordViewModel.keywords.get(0))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBarBinding.inflate(layoutInflater, container, false)
        binding.apply {
            searchBtn.setOnClickListener {
                addNewKeyword()
                callbacks?.onSearchBtnClicked(searchBarEditText.text.toString())
            }

            popularKeywordTextView.setOnClickListener {
                val texts = popularKeywordTextView.text.split(".")[1]
                callbacks?.onSearchBtnClicked(texts.trim())
            }
        }

        binding.viewModel = popularKeywordViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        popularKeywordViewModel.keyword.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    binding.popularKeywordTextView.text = it
                }
            }
        )
        myPastKeywordViewModel.keyAndDate.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    updateUI(it.toMutableList())
                }
            }
        )

        recordRecyclerView = binding.rKeywordRecyclerView
        recordRecyclerView.layoutManager = LinearLayoutManager(context)
        recordRecyclerView.adapter = adapter

        return binding.root
    }

    private fun updateUI(keyAndDate: MutableList<SearchRecord>) {
        adapter?.submitList(keyAndDate)
    }

    private fun loadPastKeywords() {
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.keyword_info), Context.MODE_PRIVATE
        ) ?: return
        val keywords = sharedPref.getString("keywords", null)
        keywords?.let {
            val jsonArray = JSONArray(keywords)
            for (i in 0 until jsonArray.length()) {
                val tempObj = jsonArray.getJSONObject(i)
                myPastKeywordViewModel.addRecord(SearchRecord(tempObj.getInt("id"), tempObj.getString("name"), tempObj.getString("date")))
            }
        }
        Log.d(TAG, myPastKeywordViewModel.keyAndDate.value.toString())
    }

    private fun saveKeywords() {
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.keyword_info), Context.MODE_PRIVATE
        ) ?: return
        with(sharedPref) {
            val jsonArray = JSONArray()
            val keyAndDate = myPastKeywordViewModel.keyAndDate.value
            keyAndDate?.let {
                for (i in keyAndDate.indices) {
                    val tempObj = JSONObject()
                    tempObj.put("id", keyAndDate[i].recordId)
                    tempObj.put("name", keyAndDate[i].productName)
                    tempObj.put("date", keyAndDate[i].searchDate)
                    jsonArray.put(tempObj)
                }
            }

            with(edit()){
                putString("keywords", jsonArray.toString())
                apply()
            }
        }
    }

    private fun addNewKeyword() {
        with (binding) {
            val keyword = searchBarEditText.text.toString()
            val date = DateFormat.format(getString(R.string.date_foramt), Calendar.getInstance().time).toString()
            myPastKeywordViewModel.addRecord(SearchRecord(myPastKeywordViewModel.keyAndDate.value?.size ?: 0, keyword, date))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            animation = ObjectAnimator.ofFloat(binding.popularKeywordTextView, "alpha", 0f, 1f, 1f, 1f, 1f, 0f).apply {
                repeatCount = ObjectAnimator.INFINITE
                addListener(object: AnimatorListenerAdapter(){
                    override fun onAnimationRepeat(animation: Animator?) {
                        super.onAnimationRepeat(animation)
                        val new_val = popularKeywordViewModel.k_idx.value?.plus(1)?.mod(10)
                        popularKeywordViewModel.k_idx.value = new_val
                        popularKeywordViewModel.keyword.postValue(popularKeywordViewModel.k_idx.value?.let {
                            popularKeywordViewModel.keywords.get(
                                it
                            )
                        })
                    }
                })
                duration = 6000
                start()
            }
        }
    }

    private inner class SearchRecordHolder(private val binding: ListItemSearchrecordBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.viewModel = SearchRecordViewModel()
            binding.root.setOnClickListener(this)
        }

        fun bind(searchRecord:SearchRecord) {
            binding.apply {
                viewModel?.searchRecord = searchRecord
                executePendingBindings()
            }
        }

        override fun onClick(p0: View?) {
            callbacks?.onSearchBtnClicked(binding.rKeywordTextView.text.toString())
        }
    }

    private inner class SearchRecordAdapter(diffCallback: DiffUtil.ItemCallback<SearchRecord>)
        :ListAdapter<SearchRecord, SearchRecordHolder>(diffCallback) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRecordHolder {
            val binding = DataBindingUtil.inflate<ListItemSearchrecordBinding>(
                layoutInflater,
                R.layout.list_item_searchrecord,
                parent,
                false)

            binding.lifecycleOwner = viewLifecycleOwner
            return SearchRecordHolder(binding)
        }

        override fun onBindViewHolder(holder: SearchRecordHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        animation.cancel()
        saveKeywords()
    }


    companion object {
        fun newInstance() = SearchBarFragment()

        private val diffUtil = object:DiffUtil.ItemCallback<SearchRecord>(){
            override fun areItemsTheSame(oldItem: SearchRecord, newItem: SearchRecord): Boolean {
                return oldItem.recordId == newItem.recordId
            }

            override fun areContentsTheSame(oldItem: SearchRecord, newItem: SearchRecord): Boolean {
                return oldItem == newItem
            }
        }
    }
}