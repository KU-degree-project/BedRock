package com.bed.android.bedrock.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bed.android.bedrock.R

class  MainActivity : AppCompatActivity(), SearchBarFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val isFragmentContainerEmpty= (savedInstanceState==null)
        if(isFragmentContainerEmpty){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, SearchBarFragment.newInstance())
                .addToBackStack(null)
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
}