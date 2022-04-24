package com.bed.android.bedrock.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentSearchResultBinding
import com.bed.android.bedrock.databinding.ListItemProductBinding
import com.bed.android.bedrock.ui.MainActivity.Companion.croller
import com.bed.android.bedrock.vmodel.ProductViewModel
import com.bed.android.bedrock.vmodel.SearchResultViewModel


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG="SearchResultFragment"


class SearchResultFragment : Fragment(){

    private val resultViewModel : SearchResultViewModel by lazy{
        ViewModelProvider(this).get(SearchResultViewModel::class.java)
    }

    private lateinit var bindingResult: FragmentSearchResultBinding

    private lateinit var productRecyclerView: RecyclerView
    private lateinit var searchText:String
    private var adapter: ProductAdapter? = null

    private var callbacks:Callbacks?=null
    interface Callbacks{
        fun onProductSelected(viewModel: ProductViewModel)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks=context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchText=arguments?.getSerializable("SearchingText") as String

        Log.d(TAG,searchText)


        resultViewModel.products= MutableLiveData()
        GlobalScope.launch(Dispatchers.IO){
            resultViewModel.products.postValue(croller.croll_list(searchText))
            Log.d(TAG,"number"+resultViewModel.products.value?.size.toString())
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         bindingResult =DataBindingUtil.inflate(inflater,R.layout.fragment_search_result,container,false)

        val view=bindingResult.root

        bindingResult.viewModel= SearchResultViewModel()
        bindingResult.viewModel!!.searchKeyword.postValue(": "+searchText)
        bindingResult.lifecycleOwner=viewLifecycleOwner

        showSampleData(true)

        resultViewModel.products.observe(viewLifecycleOwner) { products ->
            products?.let {
                showSampleData(false)
                updateUI(products.toMutableList())
                bindingResult.viewModel!!.searchCount.postValue("검색결과 " + products.size.toString() + "개")
            }
        }



        productRecyclerView=
            view.findViewById(R.id.product_recycler_view) as RecyclerView
        productRecyclerView.layoutManager=
            LinearLayoutManager(context).apply{
                orientation= LinearLayoutManager.VERTICAL
            }





        return view
    }

    private fun updateUI(products: List<Product>){
        Log.d(TAG,"updateUI")
        if(adapter==null){
            Log.d(TAG,"updateUI_null")
            adapter=ProductAdapter(diffUtil)
            adapter?.submitList(products)
            productRecyclerView.adapter=adapter

        }
        else{
            Log.d(TAG,"updateUI_submit")
            adapter?.submitList(products)
            productRecyclerView.adapter=adapter
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private inner class ProductHolder(private val binding: ListItemProductBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        var flag=false

        init{
            binding.viewModel= ProductViewModel()
            binding.root.setOnClickListener(this)


            binding.detailBtn.setOnClickListener {
                if (!flag) {
                    binding.textProductName.text = binding.viewModel!!.product!!.des
                    binding.textProductName.setOnTouchListener{v:View,event:MotionEvent->
                        v.parent.requestDisallowInterceptTouchEvent(true)

                        false

                    }
                    binding.textProductName.movementMethod=ScrollingMovementMethod()

                    flag = true

                } else {
                    binding.textProductName.text = binding.viewModel!!.product!!.name

                    binding.textProductName.setOnTouchListener(null)
                    binding.textProductName.movementMethod=null
                    flag = false
                }
            }



        }

        fun bind(product:Product){
            binding.apply{
                viewModel?.product=product

                executePendingBindings()
                Log.d(TAG,"bind${product.lowestPrice}")
            }
        }

        override fun onClick(p0: View?) {
            //제품 클릭 시 상세페이지 넘어가기
            callbacks?.onProductSelected(binding.viewModel!!)

        }



    }



    private inner class ProductAdapter(diffCallback: DiffUtil.ItemCallback<Product>)
        : androidx.recyclerview.widget.ListAdapter<Product,ProductHolder>(diffCallback){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
            val binding=DataBindingUtil.inflate<ListItemProductBinding>(
                layoutInflater,
                R.layout.list_item_product,
                parent,
                false
            )

            binding.lifecycleOwner=viewLifecycleOwner

            return ProductHolder(binding)
        }



        override fun onBindViewHolder(holder: ProductHolder, position: Int) {
            holder.bind(getItem(position))
            Log.d(TAG,"onBindViewHolder")
        }
    }


    //facebook shimmer library - 검색중 animation
    private fun showSampleData(isLoading:Boolean){
        if (isLoading) {
            bindingResult.sflSample.startShimmer()
            bindingResult.sflSample.visibility = View.VISIBLE
            bindingResult.productRecyclerView.visibility = View.GONE
        } else {
            bindingResult.sflSample.stopShimmer()
            bindingResult.sflSample.visibility = View.GONE
            bindingResult.productRecyclerView.visibility = View.VISIBLE
        }
    }



    companion object{
        fun newInstance(searchText:String): SearchResultFragment {
            val args= Bundle().apply{
                putSerializable("SearchingText",searchText)
            }
            return SearchResultFragment().apply{
                arguments=args
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