package com.bed.android.bedrock.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentRecognizeBinding
import com.bed.android.bedrock.mlkit.TextRecognizeUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class RecognizeFragment : BaseFragment<FragmentRecognizeBinding>(R.layout.fragment_recognize) {

    private val url = "http://gdimg.gmarket.co.kr/2392558284/still/600?ver=1647941445"
    private val requestListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            Log.d(TAG, "onLoadFailed: failed to load")
            return true
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            resource?.let {
                binding.imageRecognize.setImageDrawable(it)
                TextRecognizeUtil.getTextFromBitmapByRecognizer(it.toBitmap(), ::callback)
            }

            return true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadImageFromUrl()
    }

    private fun loadImageFromUrl() {
        bind {
            Glide.with(root).load(url).addListener(requestListener).into(imageRecognize)
        }
    }

    private fun callback(list: List<String>) {
        binding.textRecognize.text = list.joinToString(" ")
    }

    companion object {

        private const val TAG = "RecognizeFragment"

        fun newInstance() = RecognizeFragment()

    }
}