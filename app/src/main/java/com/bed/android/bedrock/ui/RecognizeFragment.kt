package com.bed.android.bedrock.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentRecognizeBinding
import com.bed.android.bedrock.mlkit.TextRecognizeUtil
import com.bed.android.bedrock.util.BitmapUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecognizeFragment : BaseFragment<FragmentRecognizeBinding>(R.layout.fragment_recognize) {

    private val url = "https://cdn.011st.com/11dims/resize/600x600/quality/75/11src/pd/v2/1/0/4/3/1/7/omcKh/4235104317_B.jpg"
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
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = BitmapUtil.getBitmapFromUrl(url)

            bitmap?.let {
                val ocrResult = TextRecognizeUtil.getTextFromBitmapByRecognizer(it, ::callback)
                Log.d(TAG, "loadImageFromUrl: $ocrResult")
            }
        }
    }

    private fun callback(list: List<String>) {
        list.filter {
            it.contains("만")
            it.contains("")
        }


        binding.textRecognize.text = list.joinToString(" ")
    }

    companion object {

        private const val TAG = "RecognizeFragment"

        fun newInstance() = RecognizeFragment()

    }
}
