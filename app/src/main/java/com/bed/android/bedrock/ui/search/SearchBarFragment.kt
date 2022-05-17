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

class SearchBarFragment : BaseFragment<FragmentSearchBarBinding>(R.layout.fragment_search_bar) {

    interface Callbacks {
        fun onSearchBtnClicked(searchText: String)
    }

    private var callbacks: Callbacks? = null
    private val viewModel by viewModels<SearchBarViewModel>()
    private val adapter = SearchRecordAdapter(diffUtil) {
        callbacks?.onSearchBtnClicked(it.productName)
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
        initView()
    }

    private fun initView() {
        viewModel.loadSearchRecord()

        lifecycleScope.launch {
            viewModel.fetchPopularKeyword { list ->
                startBlinkAnimation(list)
            }
        }

        bind {
            rKeywordRecyclerView.adapter = adapter

            popularKeywordTextView.setOnClickListener {
                val keyword = popularKeywordTextView.text.split(".")[1].trim()
                callbacks?.onSearchBtnClicked(keyword)
            }
        }

        binding.searchBtn.setOnClickListener {
            viewModel.addSearchKeyword { keyword ->
                callbacks?.onSearchBtnClicked(keyword)
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


    class SearchRecordAdapter(
        diffCallback: DiffUtil.ItemCallback<SearchRecord>,
        private val onClick: ((SearchRecord) -> Unit)? = null
    ) : ListAdapter<SearchRecord, SearchRecordHolder>(diffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRecordHolder {
            val binding = ListItemSearchrecordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return SearchRecordHolder(binding, onClick)
        }

        override fun onBindViewHolder(holder: SearchRecordHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    class SearchRecordHolder(
        private val binding: ListItemSearchrecordBinding,
        private val onClick: ((SearchRecord) -> Unit)? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(searchRecord: SearchRecord) {
            binding.root.setOnClickListener { onClick?.invoke(searchRecord) }
            binding.item = searchRecord
        }
    }


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
