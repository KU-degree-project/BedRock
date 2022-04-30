package com.bed.android.bedrock.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bed.android.bedrock.R
import com.bed.android.bedrock.model.Croller
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.vmodel.ProductViewModel
import com.google.android.material.tabs.TabLayout

class  MainActivity : AppCompatActivity(),
    SearchBarFragment.Callbacks,
    SearchResultFragment.Callbacks,
    Tab_pricelist.Callbacks{
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_BedRock)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isFragmentContainerEmpty= (savedInstanceState==null)
        if(isFragmentContainerEmpty){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, MainFragment.newInstance())
                .commit()
        }

    }

    override fun onSearchBtnClicked(searchText: String) {

        val fragment= SearchResultFragment.newInstance(searchText)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onProductSelected(viewModel :ProductViewModel) {
        val fragment=ProductDetailFragment.newInstance()
        var bundle=Bundle()
        bundle.putParcelable("product",viewModel.product)
        fragment.arguments=bundle
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onPriceSelected(storeUrl: String) {
        val fragment=StorePageFragment.newInstance(storeUrl)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object{
        val croller= Croller()
    }


}