package com.bed.android.bedrock.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentWebviewBinding

private const val url="Store_page_url"


class StorePageFragment : Fragment(){
    private lateinit var storeUrl:Uri
    private lateinit var binding_webview: FragmentWebviewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeUrl=arguments?.getParcelable(url)?:Uri.EMPTY

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding_webview =
            DataBindingUtil.inflate(inflater, R.layout.fragment_webview,container,false)
        val web_view=binding_webview.webView

        web_view.settings.apply{
            javaScriptEnabled=true
            useWideViewPort=true
            loadWithOverviewMode=true
            builtInZoomControls=true
            supportZoom()

        }
        web_view.webViewClient= WebViewClient()
        web_view.webChromeClient= WebChromeClient()
        web_view.loadUrl(storeUrl.toString())


        return binding_webview.root
    }

    companion object{
        fun newInstance(storeUrl:String):StorePageFragment{
            return StorePageFragment().apply{
                arguments=Bundle().apply{
                    putParcelable(url, Uri.parse(storeUrl))
                }
            }
        }
    }
}