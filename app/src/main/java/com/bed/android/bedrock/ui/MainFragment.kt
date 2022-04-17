package com.bed.android.bedrock.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import com.bed.android.bedrock.R
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
            // 검색창 눌러서 프래그먼트 전환 되기전에 애니메이션
            searchBarView.setOnClickListener {
                searchBarView.text = "" // 안내 텍스트 지우기
                // 검색창을 위한 Animation
                ObjectAnimator.ofFloat(searchBarView, "translationY", -((root.height - searchBarView.height) / 2 - TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 8f, requireContext().resources.displayMetrics))).apply {
                        duration = 200
                        addListener(object:AnimatorListenerAdapter(){
                            override fun onAnimationEnd(animation: Animator?) {
                                super.onAnimationEnd(animation)
                                parentFragmentManager.beginTransaction().apply {
                                    replace(R.id.fragment_container, SearchBarFragment.newInstance())
                                    addToBackStack("MainFragment")
                                    commit()
                                }
                            }
                        })
                    start() }

                // 타이틀 글자 위로 올려보내기
                ObjectAnimator.ofFloat(titleText, "translationY", -1000f).apply {
                    duration = 200
                    start()
                }
            }
        }
    }



    companion object{
        fun newInstance() = MainFragment()
    }

}