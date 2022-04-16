package com.bed.android.bedrock.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.bed.android.bedrock.R

class SearchBarFragment_acw: Fragment() {


    interface Callbacks{
        fun onSearchBtnClicked(searchText : String)
    }



    private lateinit var searchBar: EditText
    private lateinit var searchBtn: Button
    private var callbacks: Callbacks?=null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks=context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_search,container,false)


        searchBar=view.findViewById(R.id.product_search_bar)
        searchBtn=view.findViewById(R.id.search_btn)

        searchBtn.setOnClickListener{
            callbacks?.onSearchBtnClicked(searchBar.text.toString())
        }

        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }


    companion object{
        fun newInstance()= SearchBarFragment_acw()
    }


}