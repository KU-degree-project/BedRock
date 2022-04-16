package com.bed.android.bedrock.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentDetailtabPricelistBinding
import com.bed.android.bedrock.databinding.ListItemPriceBinding
import com.bed.android.bedrock.databinding.ListItemProductBinding
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.vmodel.PriceViewModel
import com.bed.android.bedrock.vmodel.ProductDetailViewModel
import com.bed.android.bedrock.vmodel.ProductViewModel
import com.bed.android.bedrock.vmodel.TabPriceViewModel
private const val TAG="Tab_pricelist"


class Tab_pricelist(val product: Product): Fragment() {


    private lateinit var binding_price_list: FragmentDetailtabPricelistBinding
    private lateinit var priceRecyclerView: RecyclerView
    private var adapter: Tab_pricelist.PriceAdapter? = null


    private fun updateUI(prices: List<Pair<String,String>>){
        Log.d(TAG,"updateUI")

        if(adapter==null){
            Log.d(TAG,"updateUI_null")
            adapter=PriceAdapter(diffUtil)
            adapter?.submitList(prices)
            priceRecyclerView.adapter=adapter

        }
        else{
            Log.d(TAG,"updateUI_submit")
            adapter?.submitList(prices)
            priceRecyclerView.adapter=adapter
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding_price_list =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detailtab_pricelist,container,false)
        val view=binding_price_list.root

        binding_price_list.viewModel= TabPriceViewModel()
        binding_price_list.viewModel!!.product=product
        binding_price_list.lifecycleOwner=viewLifecycleOwner


        priceRecyclerView=
            view.findViewById(R.id.recyclerview_price) as RecyclerView
        priceRecyclerView.layoutManager=
            LinearLayoutManager(context).apply{
                orientation= LinearLayoutManager.VERTICAL
            }

        binding_price_list.viewModel?.priceList?.observe(
            viewLifecycleOwner,
            Observer{ prices->
                prices?.let{
                    updateUI(prices.toMutableList())
                }
            }
        )





        return view
    }

    private inner class PriceHolder(private val binding:ListItemPriceBinding):
        RecyclerView.ViewHolder(binding.root){


        init{
            binding.viewModel= PriceViewModel()

            }


        fun bind(storeUrl:String,price:String){
            binding.apply{
                viewModel?.price?.postValue(price)
                viewModel?.url?.postValue(storeUrl)

                executePendingBindings()

            }
        }



    }



    private inner class PriceAdapter(diffCallback: DiffUtil.ItemCallback<Pair<String,String>>)
        : androidx.recyclerview.widget.ListAdapter<Pair<String,String>,PriceHolder>(diffCallback){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceHolder {
            val binding=DataBindingUtil.inflate<ListItemPriceBinding>(
                layoutInflater,
                R.layout.list_item_price,
                parent,
                false
            )

            binding.lifecycleOwner=viewLifecycleOwner

            return PriceHolder(binding)
        }



        override fun onBindViewHolder(holder: PriceHolder, position: Int) {
            holder.bind(getItem(position).first,getItem(position).second)
            Log.d(TAG,"onBindViewHolder")
        }
    }






    companion object{

        private val diffUtil = object : DiffUtil.ItemCallback<Pair<String,String>>() {

            override fun areContentsTheSame(oldItem: Pair<String,String>, newItem: Pair<String,String>): Boolean {
                return oldItem == newItem
            }
            override fun areItemsTheSame(oldItem: Pair<String,String>, newItem: Pair<String,String>) =
                oldItem.first == newItem.first
        }
    }
}