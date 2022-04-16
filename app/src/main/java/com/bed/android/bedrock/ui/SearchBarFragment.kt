package com.bed.android.bedrock.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bed.android.bedrock.databinding.FragmentSearchBarBinding

class SearchBarFragment : Fragment() {

    interface Callbacks {
        fun onSearchBtnClicked(searchText:String)
    }

    var binding: FragmentSearchBarBinding? = null
    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBarBinding.inflate(layoutInflater, container, false)
        binding!!.apply {
            searchBtn.setOnClickListener {
                callbacks?.onSearchBtnClicked(searchBarEditText.text.toString())
            }
        }


        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding!!.apply {
//            searchBtn.setOnClickListener {
//                callbacks?.onSearchBtnClicked(searchBarEditText.text.toString())
//            }
//        }
    }

    companion object {
        fun newInstance() = SearchBarFragment()
    }
}