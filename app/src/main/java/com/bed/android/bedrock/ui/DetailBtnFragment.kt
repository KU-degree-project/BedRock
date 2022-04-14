package com.bed.android.bedrock.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bed.android.bedrock.R

class DetailBtnFragment:DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=layoutInflater.inflate(R.layout.detail_button_popup,container,false)





        return view
    }






    companion object {
        fun newInstance(searchText: String): SearchResultFragment {
            val args = Bundle().apply {
                putSerializable("SearchingText", searchText)
            }
            return SearchResultFragment().apply {
                arguments = args
            }
        }
    }
}