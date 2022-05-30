package com.bed.android.bedrock.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bed.android.bedrock.R
import com.bed.android.bedrock.model.Croller
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.ui.product.ProductDetailFragment
import com.bed.android.bedrock.ui.search.SearchBarFragment
import com.bed.android.bedrock.ui.search.SearchResultFragment
import com.bed.android.bedrock.ui.search.result.TabPriceList

class MainActivity : AppCompatActivity(),
    SearchBarFragment.Callbacks,
    SearchResultFragment.Callbacks,
    TabPriceList.Callbacks {

    lateinit var searchFragment: SearchResultFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_BedRock)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isFragmentContainerEmpty = savedInstanceState == null

        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, MainFragment.newInstance())
                .commit()
        }

    }

    override fun onSearchBtnClicked(searchText: String) {

        val fragment = SearchResultFragment.newInstance(searchText)
        searchFragment=fragment
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("search")
            .commit()
    }


    override fun onProductSelected(product: Product) {
        val fragment = ProductDetailFragment.newInstance(product)

        supportFragmentManager
            .beginTransaction()
            .hide(searchFragment)
            .add(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onPriceSelected(storeUrl: String) {
        val fragment = StorePageFragment.newInstance(storeUrl)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        val croller = Croller()
    }


}
