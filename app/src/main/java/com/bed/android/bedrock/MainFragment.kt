package com.bed.android.bedrock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bed.android.bedrock.databinding.FragmentMainBinding

class MainFragment: Fragment() {

    var binding:FragmentMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.apply {
            searchBarView.setOnClickListener {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container, SearchBarFragment.newInstance())
                    addToBackStack("MainFragment")
                    commit()
                }
            }
        }
    }



    companion object{
        fun newInstance() = MainFragment()
    }
}