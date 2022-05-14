package com.bed.android.bedrock.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentProductDetailBinding
import com.bed.android.bedrock.loadImage
import com.bed.android.bedrock.ui.MainActivity.Companion.croller
import com.bed.android.bedrock.vmodel.ProductViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.annotations.NotNull


private const val TAG="ProductDetailFragment"

class ProductDetailFragment()
    : BaseFragment<FragmentProductDetailBinding>(R.layout.fragment_product_detail){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel=ViewModelProvider(this).get(ProductViewModel::class.java)
        binding.viewModel?.product=arguments?.getParcelable("product")
        Log.d(TAG,binding.viewModel.toString())
        GlobalScope.launch(Dispatchers.IO){
            croller.croll_detail(binding.viewModel?.product!!)
               Log.d(TAG, "onCreate: ${binding.viewModel?.product}")
            GlobalScope.launch(Dispatchers.Main){
                loadImage(binding.productImage,binding.viewModel?.product?.img)
            }
        }

        val pagerAdapter=PagerAdapter(requireActivity())
        pagerAdapter.addFragment(Tab_description(binding.viewModel!!))
        pagerAdapter.addFragment(Tab_pricelist(binding.viewModel!!))
        binding.viewpager.adapter=pagerAdapter
        TabLayoutMediator(binding.tabDetail,binding.viewpager,true,true,pagerAdapter).attach()



    }
    private inner class PagerAdapter(private val fa:FragmentActivity)
        : FragmentStateAdapter(fa),TabLayoutMediator.TabConfigurationStrategy {

        var fragments:ArrayList<Fragment> =ArrayList()

        private val titles = listOf(
            getString(R.string.detail_info),
            getString(R.string.price_list)
        )

        override fun createFragment(position: Int): Fragment {

            return fragments[position]

        }



        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
            tab.text = titles[position]
        }

        fun addFragment(fragment:Fragment){
            fragments.add(fragment)
            notifyItemInserted(fragments.size-1)
        }
        fun removeFragment(){
            fragments.removeLast()
            notifyItemRemoved(fragments.size)
        }





    }

    companion object {

        fun newInstance(): ProductDetailFragment {
            return ProductDetailFragment()
        }
    }
}