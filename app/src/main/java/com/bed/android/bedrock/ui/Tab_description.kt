package com.bed.android.bedrock.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentDetailtabDescBinding
import com.bed.android.bedrock.vmodel.ProductViewModel

class Tab_description(val viewModel: ProductViewModel) : Fragment(){

    private lateinit var binding_desc: FragmentDetailtabDescBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding_desc =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detailtab_desc,container,false)
        val view=binding_desc.root

        binding_desc.viewModel= viewModel
        binding_desc.lifecycleOwner=viewLifecycleOwner

        return view
    }
}