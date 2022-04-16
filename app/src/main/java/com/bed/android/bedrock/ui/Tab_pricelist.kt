package com.bed.android.bedrock.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentDetailtabPricelistBinding
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.vmodel.ProductDetailViewModel
import com.bed.android.bedrock.vmodel.TabPriceViewModel

class Tab_pricelist(val product: Product): Fragment() {


    private lateinit var binding_price_list: FragmentDetailtabPricelistBinding

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

        return view
    }
}