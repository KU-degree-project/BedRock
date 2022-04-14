package com.bed.android.bedrock.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bed.android.bedrock.model.Croller
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentSearchResultBinding
import com.bed.android.bedrock.databinding.ListItemProductBinding
import com.bed.android.bedrock.model.ResultViewModel
import com.bed.android.bedrock.vmodel.ProductViewModel
import com.bed.android.bedrock.vmodel.SearchResultViewModel
import com.bumptech.glide.Glide


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG="SearchResultFragment"


class SearchResultFragment : Fragment(){

    private val resultViewModel : ResultViewModel by lazy{
        ViewModelProvider(this).get(ResultViewModel::class.java)
    }
    private lateinit var binding_result: FragmentSearchResultBinding

    private lateinit var productRecyclerView: RecyclerView
    private lateinit var croller: Croller
    private lateinit var searchText:String
    private var adapter: ProductAdapter? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchText=arguments?.getSerializable("SearchingText") as String

        Log.d(TAG,searchText)


        croller= Croller()
        resultViewModel.products= MutableLiveData()

        GlobalScope.launch(Dispatchers.IO){
            resultViewModel.products.postValue(croller.croll(searchText))

            Log.d(TAG,"number"+resultViewModel.products.value?.size.toString())
        }


    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding_result =
            DataBindingUtil.inflate(inflater,R.layout.fragment_search_result,container,false)
        val view=binding_result.root

        binding_result.viewModel= SearchResultViewModel()
        binding_result.viewModel!!.searchKeyword.postValue(searchText)

        binding_result.lifecycleOwner=viewLifecycleOwner


        resultViewModel.products.observe(
            viewLifecycleOwner,
            Observer{
                    products->
                products?.let{
                    updateUI(products.toMutableList())
                    binding_result.viewModel!!.searchCount.postValue("검색결과 "+products.size.toString()+"개")
                }
            }
        )

        productRecyclerView=
            view.findViewById(R.id.product_recycler_view) as RecyclerView
        productRecyclerView.layoutManager=
            LinearLayoutManager(context).apply{orientation= LinearLayoutManager.VERTICAL}


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



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



    override fun onDestroy() {
        super.onDestroy()

    }

    private inner class ProductHolder(private val binding: ListItemProductBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        init{
            binding.viewModel= ProductViewModel()
            binding.root.setOnClickListener(this)
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