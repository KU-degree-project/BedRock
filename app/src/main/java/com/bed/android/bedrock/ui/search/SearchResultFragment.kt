package com.bed.android.bedrock.ui.search


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentSearchResultBinding
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.ui.BaseFragment
import com.bed.android.bedrock.ui.adapter.SearchResultAdapter
import com.bed.android.bedrock.vmodel.ProductViewModel


class SearchResultFragment :
    BaseFragment<FragmentSearchResultBinding>(R.layout.fragment_search_result) {

    interface Callbacks {
        fun onProductSelected(viewModel: ProductViewModel)
    }

    private var callbacks: Callbacks? = null
    private val viewModel by viewModels<SearchResultViewModel>()
    private val adapter = SearchResultAdapter(diffUtil)

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is Callbacks) {
            callbacks = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        arguments?.let { bundle ->
            val keyword = bundle.getString("searchKeyword") ?: return@let

            viewModel.startCrawling(keyword, ::onCrawlingFinished)

            initView(keyword)
        }
    }

    private fun initView(keyword: String) {
        with(binding) {
            sflSample.startShimmer()
            searchKeywordTextview.text = keyword
            productRecyclerView.adapter = adapter
        }
    }

    private fun onCrawlingFinished(size: Int) {
        with(binding) {
            sflSample.apply {
                stopShimmer()
                visibility = View.GONE
            }

            searchNumTextview.text = getString(R.string.search_result, size)
        }
    }


    companion object {
        fun newInstance(searchText: String): SearchResultFragment {
            return SearchResultFragment().apply {
                arguments = Bundle().apply {
                    putString("searchKeyword", searchText)
                }
            }
        }

        private val diffUtil = object : DiffUtil.ItemCallback<Product>() {

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Product, newItem: Product) =
                oldItem.id == newItem.id
        }
    }

}
