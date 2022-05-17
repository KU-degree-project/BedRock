package com.bed.android.bedrock.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentProductDetailBinding
import com.bed.android.bedrock.model.Product
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


private const val TAG = "ProductDetailFragment"

class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding>(R.layout.fragment_product_detail) {

    private val viewModel by activityViewModels<ProductDetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val product = it.getParcelable("product") as? Product ?: return@let

            viewModel.startCrawl(product)
        }
//
//        binding.viewModel=ViewModelProvider(this).get(ProductViewModel::class.java)
//        binding.viewModel?.product=arguments?.getParcelable("product")
//        Log.d(TAG,binding.viewModel.toString())
//        GlobalScope.launch(Dispatchers.IO){
//            croller.croll_detail(binding.viewModel?.product!!)
//               Log.d(TAG, "onCreate: ${binding.viewModel?.product}")
//            GlobalScope.launch(Dispatchers.Main){
//                loadImage(binding.productImage,binding.viewModel?.product?.img)
//            }
//        }
//
        val pagerAdapter = PagerAdapter(requireActivity())
        pagerAdapter.addFragment(Tab_description(binding.viewModel!!))
        pagerAdapter.addFragment(TabPriceList())
        binding.viewpager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabDetail, binding.viewpager, true, true, pagerAdapter).attach()

    }

    private inner class PagerAdapter(private val fa: FragmentActivity)
        : FragmentStateAdapter(fa), TabLayoutMediator.TabConfigurationStrategy {

        var fragments: ArrayList<Fragment> = ArrayList()

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

        fun addFragment(fragment: Fragment) {
            fragments.add(fragment)
            notifyItemInserted(fragments.size - 1)
        }

        fun removeFragment() {
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