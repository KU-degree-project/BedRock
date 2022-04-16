package com.bed.android.bedrock.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bed.android.bedrock.R
import com.bed.android.bedrock.model.Croller
import com.bed.android.bedrock.model.Product

class  MainActivity : AppCompatActivity(), SearchBarFragment.Callbacks,SearchResultFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isFragmentContainerEmpty= (savedInstanceState==null)
        if(isFragmentContainerEmpty){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, SearchBarFragment.newInstance())
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


    override fun onProductSelected(product: Product) {
        val fragment=ProductDetailFragment.newInstance(product)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .addToBackStack(null) // backStack에 추가하여 crimelist를 다시 역 transaction함과 동시에 viewmodel도 유지시킬 수 있다.
            .commit()
    }

    companion object{
        val croller= Croller()
    }


}