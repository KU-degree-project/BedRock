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
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

class RecognizeFragment : BaseFragment<FragmentRecognizeBinding>(R.layout.fragment_recognize) {

    private val url = "http://gdimg.gmarket.co.kr/2394422222/still/600?ver=1652430296"
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
        Log.d(TAG, list.toString())

        val target = 1078000.0 // 크롤링해서 정가 가져왔다고 가정
        val target_short = (target / 10000).toInt() // 만 단위로 나누기

        val t_len = target.toInt().toString().length
        val ts_len = target_short.toString().length

        // 퍼센티지 판단
        var p_filtered = list.filter {
            it.contains("%")
        }
        p_filtered = p_filtered.map {
            it.replace("[^\\d]".toRegex(), "") // 숫자 이외의 문자 다 지우기
        }
        p_filtered = p_filtered.filter {
            it.isNotBlank() // 공백 지우기
            it.toInt() < 100
        }
        p_filtered.sortedBy { // 정렬
            -it.toInt()
        }
        Log.d(TAG, "percentage filtered : $p_filtered")
        var p_val = target
        if (p_filtered.isNotEmpty()){
            p_val *= ((100.0 - p_filtered[0].toDouble()) / 100) // 쿠폰의 퍼센티지가 온전하게 할인되지 않는 경우도 있어서 고려 필요
        }

        // 가격으로 판단
        var filtered = mutableListOf<String>()
        list.map {
            it.replace("\n", " ") // 줄 내림 없애기
        }.forEach {
            filtered.addAll(it.split(" "))
        }

        filtered = filtered.map {
            it.replace("[^\\d]".toRegex(), "")
        }.filter {
            it.isNotBlank() // 공백 지우기
            it.length in ts_len-1..t_len
        }.toMutableList()

        Log.d(TAG, "price filtered : $filtered")

        var t_val = target
        var ts_val = target_short

        val t_limit = t_val * 0.7 // 상식적인 한계 (30% 할인가)
        val ts_limit = ts_val * 0.7
        Log.d(TAG, "limits : $t_limit, $ts_limit")
        filtered.forEach {
            if (it.length in ts_len-1..ts_len) {
                if (it.toInt() >= ts_limit) {
                    ts_val = min(ts_val, it.toInt())
                }
            }
            else if (it.length in t_len-1..t_len) {
                if (it.toInt() >= t_limit) {
                    t_val = min(t_val, it.toDouble())
                }
            }
        }
        ts_val *= 10000

        // 계산한 퍼센티지 가격, 추출된 가격 비교
        var min_val = min(ts_val, t_val.toInt())
        if (min_val * 0.95 >= p_val) { // 퍼센티지만 주어진 경우 or 쿠폰 한도 걸려있는 경우
            min_val = p_val.toInt()
        }

        Log.d(TAG, "target : $t_val // shortcut : $ts_val // percent : $p_val")

        binding.textRecognize.text = list.joinToString(" ") + "\n***추출된 최저가 : $min_val***"
    }

    companion object {

        private const val TAG = "RecognizeFragment"

        fun newInstance() = RecognizeFragment()

    }
}
