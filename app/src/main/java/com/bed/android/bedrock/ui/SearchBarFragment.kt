package com.bed.android.bedrock.ui


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentSearchBarBinding
import com.bed.android.bedrock.ui.MainActivity.Companion.croller
import com.bed.android.bedrock.vmodel.PopularKeywordViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "KeywordLoad"

class SearchBarFragment : Fragment() {

    interface Callbacks {
        fun onSearchBtnClicked(searchText:String)
    }
    private var callbacks: Callbacks? = null
    private lateinit var binding: FragmentSearchBarBinding
    private val popularKeywordViewModel: PopularKeywordViewModel by lazy {
        ViewModelProvider(this).get(PopularKeywordViewModel::class.java)
    }
    private lateinit var animation: ObjectAnimator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        popularKeywordViewModel.keyword = MutableLiveData()
        popularKeywordViewModel.k_idx = MutableLiveData()
        GlobalScope.launch(Dispatchers.IO) {
            popularKeywordViewModel.keywords = croller.croll_keywords()
            popularKeywordViewModel.k_idx.postValue(0)
            popularKeywordViewModel.keyword.postValue(popularKeywordViewModel.keywords.get(0))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBarBinding.inflate(layoutInflater, container, false)
        binding.apply {
            searchBtn.setOnClickListener {
                callbacks?.onSearchBtnClicked(searchBarEditText.text.toString())
            }
        }

        binding.viewModel = popularKeywordViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        popularKeywordViewModel.keyword.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    binding.popularKeywordTextview.text = it
                }
            }
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            animation = ObjectAnimator.ofFloat(binding.popularKeywordTextview, "alpha", 1f, 0f).apply {
                repeatMode = ObjectAnimator.REVERSE
                repeatCount = ObjectAnimator.INFINITE
                addListener(object: AnimatorListenerAdapter(){
                    override fun onAnimationRepeat(animation: Animator?) {
                        super.onAnimationRepeat(animation)
                        val new_val = popularKeywordViewModel.k_idx.value?.plus(1)?.mod(10)
                        popularKeywordViewModel.k_idx.value = new_val
                        popularKeywordViewModel.keyword.postValue(popularKeywordViewModel.k_idx.value?.let {
                            popularKeywordViewModel.keywords.get(
                                it
                            )
                        })
                    }
                })
            duration = 2000
            start()
            }
        }
    }

    companion object {
        fun newInstance() = SearchBarFragment()
    }
}