package com.bed.android.bedrock.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentDetailtabPricelistBinding
import com.bed.android.bedrock.databinding.ListItemPriceBinding
import com.bed.android.bedrock.vmodel.PriceViewModel
import com.bed.android.bedrock.vmodel.ProductViewModel

private const val TAG="Tab_pricelist"


class Tab_pricelist(private val viewModel:ProductViewModel): Fragment() {

    private var callbacks:Callbacks?=null
    interface Callbacks{
        fun onPriceSelected(StoreUrl:String)
    }

    private lateinit var binding_price_list: FragmentDetailtabPricelistBinding
    private lateinit var priceRecyclerView: RecyclerView
    private var adapter: Tab_pricelist.PriceAdapter? = null


    private fun updateUI(prices: List<Triple<String, String, String>>){
        Log.d(TAG,"updateUI")

        if(adapter==null){
            Log.d(TAG,"updateUI_null")
            adapter=PriceAdapter(diffUtil)
            adapter?.submitList(prices)
            priceRecyclerView.adapter=adapter
            Log.d(TAG,prices.toString())

        }
        else{
            Log.d(TAG,"updateUI_submit")
            adapter?.submitList(prices)
            priceRecyclerView.adapter=adapter
        }

    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks=context as Callbacks
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


        binding_price_list.viewModel= viewModel
        binding_price_list.lifecycleOwner=viewLifecycleOwner


        priceRecyclerView=
            view.findViewById(R.id.recyclerview_price) as RecyclerView
        priceRecyclerView.layoutManager=
            LinearLayoutManager(context).apply{
                orientation= LinearLayoutManager.VERTICAL
            }








        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.product?.let {
            updateUI(it.priceList)
        }

    }

    private inner class PriceHolder(private val binding:ListItemPriceBinding):
        RecyclerView.ViewHolder(binding.root),View.OnClickListener{


        init{
            binding.viewModel= PriceViewModel()
            binding.root.setOnClickListener(this)
            }


        fun bind(thumbnailUrl:String,price:String,storeUrl:String){
            binding.apply{
                viewModel?.price?.postValue(price)
                viewModel?.imgUrl?.postValue(thumbnailUrl)
                viewModel?.storeUrl=storeUrl

                executePendingBindings()

            }
        }

        override fun onClick(v: View?) {
            callbacks?.onPriceSelected(binding.viewModel!!.storeUrl)
        }



    }



    private inner class PriceAdapter(diffCallback: DiffUtil.ItemCallback<Triple<String,String,String>>)
        : androidx.recyclerview.widget.ListAdapter<Triple<String,String,String>,PriceHolder>(diffCallback){

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
            holder.bind(getItem(position).first,getItem(position).second,getItem(position).third)
            Log.d(TAG,"onBindViewHolder")
        }
    }






    companion object{

        private val diffUtil = object : DiffUtil.ItemCallback<Triple<String,String,String>>() {

            override fun areContentsTheSame(oldItem: Triple<String,String,String>, newItem: Triple<String,String,String>): Boolean {
                return oldItem == newItem
            }
            override fun areItemsTheSame(oldItem: Triple<String,String,String>, newItem: Triple<String,String,String>) =
                oldItem.first == newItem.first
        }
    }
}