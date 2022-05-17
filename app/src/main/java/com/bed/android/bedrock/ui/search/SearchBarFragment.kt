package com.bed.android.bedrock.ui.search


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentSearchBarBinding
import com.bed.android.bedrock.databinding.ListItemSearchrecordBinding
import com.bed.android.bedrock.model.SearchRecord
import com.bed.android.bedrock.ui.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "KeywordLoad"

class SearchBarFragment : BaseFragment<FragmentSearchBarBinding>(R.layout.fragment_search_bar) {

    interface Callbacks {
        fun onSearchBtnClicked(searchText: String)
    }

    private var callbacks: Callbacks? = null
    private val viewModel by viewModels<SearchBarViewModel>()
    private val adapter = SearchRecordAdapter(diffUtil)
//    private val animation = AnimationUtils.loadAnimation(context, R.anim.fade_inandout)

//    private val myPastKeywordViewModel: MyPastKeywordViewModel by lazy {
//        ViewModelProvider(this).get(MyPastKeywordViewModel::class.java)
//    }
//    private val popularKeywordViewModel: PopularKeywordViewModel by lazy {
//        ViewModelProvider(this).get(PopularKeywordViewModel::class.java)
//    }
//    private lateinit var animation: ObjectAnimator
//    private lateinit var recordRecyclerView: RecyclerView
//    private var adapter: SearchRecordAdapter? = SearchRecordAdapter(diffUtil)

    init {
        lifecycleScope.launchWhenResumed {
            viewModel.fetchPopularKeyword { list ->
                startBlinkAnimation(list)
            }
        }
    }

    private fun startBlinkAnimation(list: List<String>) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.fade_inandout)

        binding.popularKeywordTextView.apply {
            CoroutineScope(Dispatchers.Main).launch {
                startAnimation(animation)
                (0..Int.MAX_VALUE).forEach {
                    delay(4000)
                    text = list[it % 10]
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is Callbacks) {
            callbacks = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        bind {
            rKeywordRecyclerView.adapter = adapter
        }


//        binding.apply {
//            animation = ObjectAnimator.ofFloat(binding.popularKeywordTextView, "alpha", 0f, 1f, 1f, 1f, 1f, 0f).apply {
//                repeatCount = ObjectAnimator.INFINITE
//                addListener(object: AnimatorListenerAdapter(){
//                    override fun onAnimationRepeat(animation: Animator?) {
//                        super.onAnimationRepeat(animation)
//                        val new_val = popularKeywordViewModel.k_idx.value?.plus(1)?.mod(10)
//                        popularKeywordViewModel.k_idx.value = new_val
//                        popularKeywordViewModel.keyword.postValue(popularKeywordViewModel.k_idx.value?.let {
//                            popularKeywordViewModel.keywords.get(
//                                it
//                            )
//                        })
//                    }
//                })
//                duration = 6000
//                start()
//            }
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 최근 검색어 불러오기
//        loadPastKeywords()
//
//        // 인기 키워드 불러오기
//        popularKeywordViewModel.keyword = MutableLiveData()
//        popularKeywordViewModel.k_idx = MutableLiveData()
//        GlobalScope.launch(Dispatchers.IO) {
//            popularKeywordViewModel.keywords = croller.croll_keywords()
//            popularKeywordViewModel.k_idx.postValue(0)
//            popularKeywordViewModel.keyword.postValue(popularKeywordViewModel.keywords.get(0))
//        }
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentSearchBarBinding.inflate(layoutInflater, container, false)
//        binding.apply {
//            searchBtn.setOnClickListener {
//                addNewKeyword()
//                callbacks?.onSearchBtnClicked(searchBarEditText.text.toString())
//            }
//
//            popularKeywordTextView.setOnClickListener {
//                val texts = popularKeywordTextView.text.split(".")[1]
//                callbacks?.onSearchBtnClicked(texts.trim())
//            }
//        }
//
//        binding.viewModel = popularKeywordViewModel
//        binding.lifecycleOwner = viewLifecycleOwner
//        popularKeywordViewModel.keyword.observe(
//            viewLifecycleOwner,
//            Observer {
//                it?.let {
//                    binding.popularKeywordTextView.text = it
//                }
//            }
//        )
//        myPastKeywordViewModel.keyAndDate.observe(
//            viewLifecycleOwner,
//            Observer {
//                it?.let {
//                    updateUI(it.toMutableList())
//                }
//            }
//        )
//
//        recordRecyclerView = binding.rKeywordRecyclerView
//        recordRecyclerView.layoutManager = LinearLayoutManager(context)
//        recordRecyclerView.adapter = adapter
//
//        return binding.root
//    }

//    private fun updateUI(keyAndDate: MutableList<SearchRecord>) {
//        adapter?.submitList(keyAndDate)
//    }

//    private fun loadPastKeywords() {
//        val sharedPref = activity?.getSharedPreferences(
//            getString(R.string.keyword_info), Context.MODE_PRIVATE
//        ) ?: return
//        val keywords = sharedPref.getString("keywords", null)
//        keywords?.let {
//            val jsonArray = JSONArray(keywords)
//            for (i in 0 until jsonArray.length()) {
//                val tempObj = jsonArray.getJSONObject(i)
//                myPastKeywordViewModel.addRecord(SearchRecord(tempObj.getInt("id"), tempObj.getString("name"), tempObj.getString("date")))
//            }
//        }
//        Log.d(TAG, myPastKeywordViewModel.keyAndDate.value.toString())
//    }

//    private fun saveKeywords() {
//        val sharedPref = activity?.getSharedPreferences(
//            getString(R.string.keyword_info), Context.MODE_PRIVATE
//        ) ?: return
//        with(sharedPref) {
//            val jsonArray = JSONArray()
//            val keyAndDate = myPastKeywordViewModel.keyAndDate.value
//            keyAndDate?.let {
//                for (i in keyAndDate.indices) {
//                    val tempObj = JSONObject()
//                    tempObj.put("id", keyAndDate[i].recordId)
//                    tempObj.put("name", keyAndDate[i].productName)
//                    tempObj.put("date", keyAndDate[i].searchDate)
//                    jsonArray.put(tempObj)
//                }
//            }
//
//            with(edit()){
//                putString("keywords", jsonArray.toString())
//                apply()
//            }
//        }
//    }

//    private fun addNewKeyword() {
//        with (binding) {
//            val keyword = searchBarEditText.text.toString()
//            val date = DateFormat.format(getString(R.string.date_foramt), Calendar.getInstance().time).toString()
//            myPastKeywordViewModel.addRecord(SearchRecord(myPastKeywordViewModel.keyAndDate.value?.size ?: 0, keyword, date))
//        }
//    }


    class SearchRecordAdapter(diffCallback: DiffUtil.ItemCallback<SearchRecord>)
        : ListAdapter<SearchRecord, SearchRecordHolder>(diffCallback) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRecordHolder {
            val binding = ListItemSearchrecordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)

            return SearchRecordHolder(binding)
        }

        override fun onBindViewHolder(holder: SearchRecordHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    class SearchRecordHolder(
        private val binding: ListItemSearchrecordBinding,
        private val onClick: ((SearchRecord) -> Unit)? = null)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(searchRecord: SearchRecord) {

        }
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        animation.cancel()
//        saveKeywords()
//    }


    companion object {

        fun newInstance() = SearchBarFragment()

        private val diffUtil = object : DiffUtil.ItemCallback<SearchRecord>() {
            override fun areItemsTheSame(oldItem: SearchRecord, newItem: SearchRecord): Boolean {
                return oldItem.recordId == newItem.recordId
            }

            override fun areContentsTheSame(oldItem: SearchRecord, newItem: SearchRecord): Boolean {
                return oldItem == newItem
            }
        }
    }
}