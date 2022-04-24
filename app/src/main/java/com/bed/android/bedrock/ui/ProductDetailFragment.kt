package com.bed.android.bedrock.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentProductDetailBinding
import com.bed.android.bedrock.ui.MainActivity.Companion.croller
import com.bed.android.bedrock.vmodel.ProductViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch




private const val TAG="ProductDetailFragment"

class ProductDetailFragment(var viewModel: ProductViewModel) : Fragment(){

    private lateinit var binding_detail:FragmentProductDetailBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        GlobalScope.launch(Dispatchers.IO){
            croller.croll_detail(viewModel.product!!)
            GlobalScope.launch(Dispatchers.Main){
               // loadImage(binding_detail.productImage,product.img)
            }
          //  Log.d(TAG,product.priceList.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding_detail =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail,container,false)
        val view=binding_detail.root
    //    Log.d(TAG,product.toString())
        binding_detail.viewModel= viewModel

        binding_detail.lifecycleOwner=viewLifecycleOwner



        val viewpager=binding_detail.viewpager
        val pagerAdapter=PagerAdapter(requireActivity())
        pagerAdapter.addFragment(Tab_description(binding_detail.viewModel!!))
        pagerAdapter.addFragment(Tab_pricelist(binding_detail.viewModel!!))



        viewpager?.adapter=pagerAdapter
        TabLayoutMediator(binding_detail.tabDetail,viewpager,true,true,pagerAdapter).attach()




        return view
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
        fun newInstance(viewModel: ProductViewModel): ProductDetailFragment {
            return ProductDetailFragment(viewModel)
        }
    }
}