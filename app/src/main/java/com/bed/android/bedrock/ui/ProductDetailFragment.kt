package com.bed.android.bedrock.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.bed.android.bedrock.model.Croller
import com.bed.android.bedrock.ui.MainActivity.Companion.croller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch




private const val TAG="ProductDetailFragment"

class ProductDetailFragment : Fragment(){

    private lateinit var productLink:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productLink=arguments?.getSerializable("productlink") as String

        Log.d(TAG,productLink)


        GlobalScope.launch(Dispatchers.IO){

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        fun newInstance(productLink: String): ProductDetailFragment {
            val args = Bundle().apply {
                putSerializable("productlink", productLink)
            }
            return ProductDetailFragment().apply {
                arguments = args
            }
        }
    }
}